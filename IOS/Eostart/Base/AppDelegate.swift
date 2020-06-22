//
//  AppDelegate.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 2..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import DropDown

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    var launched:Bool = false

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        
        UINavigationBar.appearance().shadowImage = UIImage()
        UINavigationBar.appearance().setBackgroundImage(UIImage(), for: .default)
        UINavigationBar.appearance().barStyle = .blackOpaque
//        let statusBar = UIApplication.shared.value(forKeyPath: "statusBarWindow.statusBar") as! UIView
//        statusBar.backgroundColor = UIColor(hexString: "#3F4863")
        
        DropDown.startListeningToKeyboard()
        return true
    }

    func applicationWillResignActive(_ application: UIApplication) {
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        if( application.topViewController!.isKind(of: AppLockViewController.self) ||
            application.topViewController!.isKind(of: PasswordViewController.self) ||
            application.topViewController!.isKind(of: IntroViewController.self) ||
            application.topViewController!.isKind(of: AppLockSetViewController.self)) {

        } else {
            if(BaseDao.instance.getUsingAppLock() == true) {
                let appLockVC = AppLockViewController(nibName: "AppLockViewController", bundle: nil)
                appLockVC.modalPresentationStyle = .overFullScreen
                application.topViewController!.present(appLockVC, animated: true, completion: nil)
            }
        }
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        if (application.topViewController!.isKind(of: AppLockViewController.self)) {
            NotificationCenter.default.post(name: NOTI_STATE_FOREGROUND, object: nil, userInfo: nil)
        }
    }
    

    func applicationWillTerminate(_ application: UIApplication) {
    }


    
}
extension UIApplication{
    var topViewController: UIViewController?{
        if keyWindow?.rootViewController == nil{
            return keyWindow?.rootViewController
        }
        
        var pointedViewController = keyWindow?.rootViewController
        
        while  pointedViewController?.presentedViewController != nil {
            switch pointedViewController?.presentedViewController {
            case let navagationController as UINavigationController:
                pointedViewController = navagationController.viewControllers.last
            case let tabBarController as UITabBarController:
                pointedViewController = tabBarController.selectedViewController
            default:
                pointedViewController = pointedViewController?.presentedViewController
            }
        }
        return pointedViewController
        
    }
}


