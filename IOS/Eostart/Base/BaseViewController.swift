//
//  BaseViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 2..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import MBProgressHUD
import Toaster

class BaseViewController: UIViewController {
        
    override func viewDidLoad() {
        super.viewDidLoad()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.startAvoidingKeyboard()
    }

    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        self.stopAvoidingKeyboard()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
    }
    
    
    public func showWaittingAlert() {
        let loadingNotification = MBProgressHUD.showAdded(to: self.view, animated: true)
        loadingNotification.bezelView.color = UIColor(hexString: "#6D6D6D")
        loadingNotification.bezelView.style = .blur
        loadingNotification.contentColor = UIColor(hexString: "#2359B8")
        loadingNotification.mode = MBProgressHUDMode.indeterminate
        loadingNotification.label.text = "Loading"
        
    }
    
    public func hideWaittingAlert() {
        MBProgressHUD.hideAllHUDs(for: self.view, animated: true)
    }
    
    
    public func deleteAccount(_ account: String) {
        
        do {
            BaseDao.instance.deleteUser(account: account)
            
            let old = NSDate().addingTimeInterval(-86400.0)
            BaseDao.instance.setLastCheckTime(old)
            
            if(BaseDao.instance.selectAllUsers().count <= 0) {
                BaseDao.instance.setUsingAppLock(false)
            }
            
            Toast(text: "delete_ok".localized(), duration: Delay.short).show()
            let introVC = UIStoryboard(name: "Init", bundle: nil).instantiateViewController(withIdentifier: "IntroViewController") as! IntroViewController
            let appDelegate = UIApplication.shared.delegate as! AppDelegate
            appDelegate.window?.rootViewController = introVC
            self.present(introVC, animated: true, completion: nil)
            
        } catch {
            
        }
        
    }
}

extension String {
    func localized() -> String? {
        if let path = Bundle.main.path(forResource:BaseDao.instance.getUserLangauageS(), ofType: "lproj") {
            if let bundle = Bundle(path: path) {
                return NSLocalizedString(self, tableName: nil, bundle: bundle, value: "", comment: "")
            }
        }
        return nil;
    }
}


extension UIView {
    var statusBar: UIView? {
        return value(forKey: "statusBar") as? UIView
    }
}

extension BaseViewController {
    
    func startAvoidingKeyboard() {
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(_onKeyboardFrameWillChangeNotificationReceived(_:)),
                                               name: NSNotification.Name.UIKeyboardWillChangeFrame,
                                               object: nil)
    }
    
    func stopAvoidingKeyboard() {
        NotificationCenter.default.removeObserver(self,
                                                  name: NSNotification.Name.UIKeyboardWillChangeFrame,
                                                  object: nil)
        
    }
    
    @objc private func _onKeyboardFrameWillChangeNotificationReceived(_ notification: Notification) {
        guard let userInfo = notification.userInfo,
            let keyboardFrame = (userInfo[UIKeyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue else {
                return
        }
        
        let keyboardFrameInView = view.convert(keyboardFrame, from: nil)
        let safeAreaFrame = view.safeAreaLayoutGuide.layoutFrame.insetBy(dx: 0, dy: -additionalSafeAreaInsets.bottom)
        let intersection = safeAreaFrame.intersection(keyboardFrameInView)
        
        let animationDuration: TimeInterval = (notification.userInfo?[UIKeyboardAnimationDurationUserInfoKey] as? NSNumber)?.doubleValue ?? 0
        let animationCurveRawNSN = notification.userInfo?[UIKeyboardAnimationCurveUserInfoKey] as? NSNumber
        let animationCurveRaw = animationCurveRawNSN?.uintValue ?? UIViewAnimationOptions.curveEaseInOut.rawValue
        let animationCurve = UIViewAnimationOptions(rawValue: animationCurveRaw)
        
        UIView.animate(withDuration: animationDuration, delay: 0, options: animationCurve, animations: {
            self.additionalSafeAreaInsets.bottom = intersection.height
            self.view.layoutIfNeeded()
        }, completion: nil)
    }
}
