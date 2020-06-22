//
//  AppLockViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 15..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import Toaster
import LocalAuthentication


class AppLockViewController: BaseViewController {
    
    @IBOutlet weak var topTitleLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    
    @IBOutlet weak var pin0Img: UIImageView!
    @IBOutlet weak var pin1Img: UIImageView!
    @IBOutlet weak var pin2Img: UIImageView!
    @IBOutlet weak var pin3Img: UIImageView!
    
    @IBOutlet weak var deciamlBtn0: UIButton!
    @IBOutlet weak var deciamlBtn1: UIButton!
    @IBOutlet weak var deciamlBtn2: UIButton!
    @IBOutlet weak var deciamlBtn3: UIButton!
    @IBOutlet weak var deciamlBtn4: UIButton!
    @IBOutlet weak var deciamlBtn5: UIButton!
    @IBOutlet weak var deciamlBtn6: UIButton!
    @IBOutlet weak var deciamlBtn7: UIButton!
    @IBOutlet weak var deciamlBtn8: UIButton!
    @IBOutlet weak var deciamlBtn9: UIButton!
    @IBOutlet weak var deciamlBtnBack: UIButton!
    
    @IBAction func onDecimalClick(_ sender: UIButton) {
        let value = (sender.titleLabel?.text)!
        userInsert.append(value)
        if(userInsert.count == 4) {
            self.onUserInsertFinish()
        }
    }
    
    @IBAction func onDeleteClick(_ sender: UIButton) {
        if(userInsert.count == 0) {
            if(isConfirm == true) {
                
            } else {
                self.onUserCancel()
            }
            
        } else {
            let subString = userInsert.prefix(userInsert.count - 1)
            userInsert = String(subString)
        }
    }
    
    var pinImgs: [UIImageView] = [UIImageView]()
    var deciamlBtns: [UIButton] = [UIButton]()
    var number: [String] = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]
    var isConfirm: Bool?
    var userInsert: String = "" {
        willSet(newVal){
            self.onUpdatePinImage(count: newVal.count)
        }
    }
    var cancelbio = false

    override func viewDidLoad() {
        super.viewDidLoad()
        self.deciamlBtns = [self.deciamlBtn0, self.deciamlBtn1, self.deciamlBtn2,
                            self.deciamlBtn3, self.deciamlBtn4, self.deciamlBtn5,
                            self.deciamlBtn6, self.deciamlBtn7, self.deciamlBtn8, self.deciamlBtn9]
        self.pinImgs = [self.pin0Img, self.pin1Img, self.pin2Img, self.pin3Img]
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.topTitleLabel.text = "top_title_applock_unlock".localized()
        self.descriptionLabel.text = ""
        NotificationCenter.default.addObserver(self, selector: #selector(self.bioAuth), name: NOTI_STATE_FOREGROUND, object: nil)

        self.initView()
    }
    
    
    override func viewWillDisappear(_ animated: Bool){
        super.viewWillDisappear(animated)
        NotificationCenter.default.removeObserver(self)
    }
    
    
    func initView() {
        self.onRefreshKeyBoard()
        isConfirm = false
        userInsert = ""
    }
    
    
    func onRefreshKeyBoard() {
        number.shuffle()
        for i in 0 ..< deciamlBtns.count {
            self.deciamlBtns[i].setTitle(number[i], for: .normal)
        }
    }
    
    func onUpdatePinImage(count:Int) {
        for i in 0 ..< 4 {
            if(i < count) {
                pinImgs[i].image = UIImage(named: "pin_img");
            } else {
                pinImgs[i].image = UIImage(named: "pin_img_none");
            }
        }
    }
    
    func onUserInsertFinish() {
        if(userInsert == BaseDao.instance.getUsingAppLockKey()) {
            self.onUserSuccessUnlock()
            
        } else {
            Toast(text: "error_two_password_differ".localized(), duration: Delay.short).show()
            self.initView()
            
        }
    }
    
    func onUserCancel() {
        exit(0)
    }
    
    func onUserSuccessUnlock() {
        if let presenter = presentingViewController as? IntroViewController {
            presenter.passed = true
            self.dismiss(animated: true) {
                presenter.reqVersionCheck()
            }
        } else {
            self.dismiss(animated: true, completion: nil)
        }
        
    }
    
    @objc func bioAuth() {
        if (self.cancelbio) {return}
        
        if(!BaseDao.instance.getUsingBioLock()) {
            return
        }
        let myContext = LAContext()
        let myLocalizedReasonString = "msg_applocked".localized()!

        var authError: NSError?
        if #available(iOS 8.0, macOS 10.12.1, *) {
            if myContext.canEvaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, error: &authError) {
                myContext.evaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, localizedReason: myLocalizedReasonString) { success, evaluateError in
                    DispatchQueue.main.async {
                        if success {
                            self.onUserSuccessUnlock()
                        } else {
                            self.cancelbio = true
                        }
                    }
                }
            } else {
                self.cancelbio = true
            }
        } else {
            self.cancelbio = true
        }
    }

}
