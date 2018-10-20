//
//  SettingTableViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 14..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import LocalAuthentication

class SettingTableViewController: UITableViewController {

    @IBOutlet weak var accountLable: UILabel!
    @IBOutlet weak var currencyLabel: UILabel!
    @IBOutlet weak var languageLabel: UILabel!
    @IBOutlet weak var currencySelected: UILabel!
    @IBOutlet weak var languageSelected: UILabel!
    
    @IBOutlet weak var appLockLabel: UILabel!
    @IBOutlet weak var bioLockLabel: UILabel!
    @IBOutlet weak var applockSwitch: UISwitch!
    @IBOutlet weak var biolockSwitch: UISwitch!
    @IBOutlet weak var bioImg: UIImageView!
    @IBAction func appLockSwitch(_ sender: UISwitch) {
        self.toggleAppLock(sender.isOn)
        
    }
    @IBAction func bioLockSwitch(_ sender: UISwitch) {
        BaseDao.instance.setUsingBioLock(sender.isOn)
        self.checkBioLock()
        self.tableView.reloadData()
    }
    
    @IBOutlet weak var homePageLabel: UILabel!
    @IBOutlet weak var telegramLabel: UILabel!
    
    @IBOutlet weak var githubLabel: UILabel!
    @IBOutlet weak var licenceLabel: UILabel!
    @IBOutlet weak var termsLabel: UILabel!
    
    var hideBio = false
    var titleBio = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()

    }

    override func viewWillAppear(_ animated: Bool) {
        print("SettingTableViewController  viewWillAppear")
        super.viewWillAppear(animated)
        
        accountLable.text = "set_account".localized()
        currencyLabel.text = "set_currency".localized()
        languageLabel.text = "set_language".localized()
        
        
        let laContext = LAContext()
        let biometricsPolicy = LAPolicy.deviceOwnerAuthenticationWithBiometrics
        var error: NSError?
        
        applockSwitch.setOn(BaseDao.instance.getUsingAppLock(), animated: false)
        biolockSwitch.setOn(BaseDao.instance.getUsingBioLock(), animated: false)
        appLockLabel.text = "set_applock".localized()

        if (laContext.canEvaluatePolicy(biometricsPolicy, error: &error)) {
            if error != nil { return }
            if #available(iOS 11.0, *) {
                switch laContext.biometryType {
                case .faceID:
                    titleBio = "set_faceid".localized()!
                    bioImg.image = UIImage(named: "st_faceid")
                case .touchID:
                    titleBio = "set_touchid".localized()!
                    bioImg.image = UIImage(named: "st_fingerprint")
                case .none:
                    break
                }
            }
        }
        self.checkBioLock()
        
        
        homePageLabel.text = "set_homepage".localized()
        telegramLabel.text = "set_telegram".localized()
        
        githubLabel.text = "set_github".localized()
        licenceLabel.text = "set_licences".localized()
        termsLabel.text = "set_terms".localized()
        
        
        currencySelected.text = BaseDao.instance.getUserCurrencyS()
        if(BaseDao.instance.getUserLangauage() == 0) {
            languageSelected.text = "language_default".localized()
        } else if (BaseDao.instance.getUserLangauage() == 1) {
            languageSelected.text = "language_korean".localized()
        } else {
            languageSelected.text = "language_english".localized()
        }
        
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("didSelectRowAt ", indexPath.section, " ", indexPath.row);
        if (indexPath.section == 0) {
            if(indexPath.row == 0) {
                let accoutVC = AccountManageViewController(nibName: "AccountManageViewController", bundle: nil)
                accoutVC.hidesBottomBarWhenPushed = true
                self.navigationItem.title = ""
                self.navigationController?.pushViewController(accoutVC, animated: true)
                
            } else if (indexPath.row == 1) {
                let currencyVC = CurrencyManageViewController(nibName: "CurrencyManageViewController", bundle: nil)
                currencyVC.hidesBottomBarWhenPushed = true
                self.navigationItem.title = ""
                self.navigationController?.pushViewController(currencyVC, animated: true)
                
            } else if(indexPath.row == 2) {
                let languageVC = LanguageManageViewController(nibName: "LanguageManageViewController", bundle: nil)
                languageVC.hidesBottomBarWhenPushed = true
                self.navigationItem.title = ""
                self.navigationController?.pushViewController(languageVC, animated: true)
                
            }
            
        } else if(indexPath.section == 1) {
            if(indexPath.row == 0) {
                self.toggleAppLock(!applockSwitch.isOn)
                
            } else if (indexPath.row == 1) {
                let newValue = !biolockSwitch.isOn
                biolockSwitch.setOn(newValue, animated: true)
                BaseDao.instance.setUsingBioLock(newValue)
            }
            
        } else if(indexPath.section == 2) {
            if(indexPath.row == 0) {
                guard let url = URL(string: "https://wannabit.io/") else {
                    return
                }
                if #available(iOS 10.0, *) {
                    UIApplication.shared.open(url, options: [:], completionHandler: nil)
                } else {
                    UIApplication.shared.openURL(url)
                }
                
            } else if (indexPath.row == 1) {
                guard let url = URL(string: "https://t.me/joinchat/GTBI4UYr1-JkQRc7OwtvWA") else {
                    return
                }
                if #available(iOS 10.0, *) {
                    UIApplication.shared.open(url, options: [:], completionHandler: nil)
                } else {
                    UIApplication.shared.openURL(url)
                }
                
            }
            
        } else if(indexPath.section == 3) {
            if(indexPath.row == 0) {
//                guard let url = URL(string: "https://wannabit.io/") else {
//                    return
//                }
//                if #available(iOS 10.0, *) {
//                    UIApplication.shared.open(url, options: [:], completionHandler: nil)
//                } else {
//                    UIApplication.shared.openURL(url)
//                }
                
            } else if (indexPath.row == 1) {
//                guard let url = URL(string: "https://t.me/joinchat/GTBI4UYr1-JkQRc7OwtvWA") else {
//                    return
//                }
//                if #available(iOS 10.0, *) {
//                    UIApplication.shared.open(url, options: [:], completionHandler: nil)
//                } else {
//                    UIApplication.shared.openURL(url)
//                }
                
            } else if(indexPath.row == 2) {
                if(BaseDao.instance.getUserLangauageS() == "ko") {
                    guard let url = URL(string: "https://eostart.com/policy_ko") else {
                        return
                    }
                    if #available(iOS 10.0, *) {
                        UIApplication.shared.open(url, options: [:], completionHandler: nil)
                    } else {
                        UIApplication.shared.openURL(url)
                    }
                } else {
                    guard let url = URL(string: "https://eostart.com/policy_en") else {
                        return
                    }
                    if #available(iOS 10.0, *) {
                        UIApplication.shared.open(url, options: [:], completionHandler: nil)
                    } else {
                        UIApplication.shared.openURL(url)
                    }
                }
            }
            
        }
    }
    
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 1 && indexPath.row == 1) {
            if hideBio {
                return 0
            } else {
                return 50
            }
        }
        return super.tableView(tableView, heightForRowAt: indexPath)
    }
    
    func toggleAppLock(_ using:Bool) {
        let appLockSetVC = AppLockSetViewController(nibName: "AppLockSetViewController", bundle: nil)
        if(using) {
            appLockSetVC.mode = AppLockSetMode.Enable

        } else {
            appLockSetVC.mode = AppLockSetMode.Disable
        }
        appLockSetVC.hidesBottomBarWhenPushed = true
        self.navigationItem.title = ""
        self.navigationController?.pushViewController(appLockSetVC, animated: true)
    }
    
    
    func checkBioLock() {
        if(titleBio.count > 0 && BaseDao.instance.getUsingAppLock()) {
            bioLockLabel.text = titleBio
            hideBio = false
        } else {
            hideBio = true
        }
        self.tableView.reloadData()
    }

}
