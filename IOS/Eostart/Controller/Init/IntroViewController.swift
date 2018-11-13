//
//  IntroViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 2..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import Alamofire
import CryptoSwift

class IntroViewController: UIViewController {
    
    let mainTabStoryboard = UIStoryboard(name: "MainTabStoryboard", bundle: nil)
    var passed:Bool = false;
    
    override func viewDidLoad() {
//        print("IntroViewController  viewDidLoad")
        super.viewDidLoad()
        passed = false;
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        print("IntroViewController  viewDidAppear ", isBeingPresented, isBeingDismissed)
        super.viewDidAppear(animated)
        
        if(BaseDao.instance.getUsingAppLock() == true && !passed) {
            let appLockVC = AppLockViewController(nibName: "AppLockViewController", bundle: nil)
            self.present(appLockVC, animated: true, completion: nil)
        } else {
            self.reqVersionCheck()
        }
    }
    

    
    func reqVersionCheck() {
        let request = Alamofire
            .request(URL_PATH_VERSION_CHECK,
                     method: .get,
                     parameters: [:],
                     encoding: URLEncoding.default,
                     headers: ["X-Auth-Token":"tester@wannabit.io=30335081442472=d3a275d23d96fba61af0e6529a495214"]);
        request.responseJSON { (response) in
            switch response.result {
            case .success(let res):
                let response = res as! NSDictionary
                
                let acceptableVersion = response.object(forKey: "acceptableVersion") as! Int
                let latestVersion = response.object(forKey: "latestVersion") as! Int
                let localVersion = Int(Bundle.main.infoDictionary!["CFBundleVersion"] as! String)!
                
//                print("acceptableVersion ", acceptableVersion , "  latestVersion" , latestVersion, "  localVersion", localVersion)
                
                if(localVersion < acceptableVersion) {
                    let alertController = UIAlertController(title: "update".localized(), message: "msg_update_force".localized(), preferredStyle: UIAlertControllerStyle.alert)
                    let cancelAction = UIAlertAction(title: "cancel".localized(), style: UIAlertActionStyle.default) { (result : UIAlertAction) -> Void in
                        exit(0)
                    }
                    let okAction = UIAlertAction(title: "update".localized(), style: UIAlertActionStyle.destructive) { (result : UIAlertAction) -> Void in
                        self.update()
                    }
                    alertController.addAction(cancelAction)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                    
                } else if(localVersion < latestVersion) {
                    let alertController = UIAlertController(title: "update".localized(), message: "msg_update_suggest".localized(), preferredStyle: UIAlertControllerStyle.alert)
                    let cancelAction = UIAlertAction(title: "cancel".localized(), style: UIAlertActionStyle.default) { (result : UIAlertAction) -> Void in
                        self.reqTokenList();
                    }
                    let okAction = UIAlertAction(title: "update".localized(), style: UIAlertActionStyle.destructive) { (result : UIAlertAction) -> Void in
                        self.update()
                    }
                    alertController.addAction(cancelAction)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                    
                } else {
                    self.reqTokenList();
                }
                
                
                
            case .failure(let error):
                print(error)
            }
        }
    }
    
    
    func reqTokenList() {
        let request = Alamofire.request(URL_PATH_EOS_TOKEN_INFO, method: .get, parameters: [:], encoding: URLEncoding.default, headers: [:]);
        request.responseJSON { (response) in
        switch response.result {
            case .success(let res):
                let response = res as! NSDictionary
                let tokenList = response.object(forKey: "tokenList") as? [[String: Any]]
    
                var model = Array<Token>()
                for token in tokenList!{
                    model.append(Token(token))
                }
            
                BaseDao.instance.deleteAllToken()
                BaseDao.instance.insertTokens(newTokens: model)
            
                if(BaseDao.instance.selectAllUsers().count > 0) {
                    self.onStartMainTab()
                } else {
                    self.onStartWellcome()
                }
            
            case .failure(let error):
                print(error)
            }
        }
    }
    
    func onStartWellcome() {
        let welcomeVC = UIStoryboard(name: "Init", bundle: nil).instantiateViewController(withIdentifier: "WelcomeViewController") as! WelcomeViewController
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        appDelegate.window?.rootViewController = welcomeVC
        self.present(welcomeVC, animated: true, completion: nil)
    }
    
    func onStartMainTab() {
        let mainTabVC = UIStoryboard(name: "MainTabStoryboard", bundle: nil).instantiateViewController(withIdentifier: "MainTabViewController") as! MainTabViewController
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        appDelegate.window?.rootViewController = mainTabVC
        self.present(mainTabVC, animated: true, completion: nil)
    }
    
    
    func update() {
        guard let url = URL(string: "") else {
            return
        }
        if #available(iOS 10.0, *) {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        } else {
            UIApplication.shared.openURL(url)
        }
    }
    
}
