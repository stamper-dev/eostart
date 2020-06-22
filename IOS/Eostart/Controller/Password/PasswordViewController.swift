//
//  PasswordViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 28..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import Alamofire
import CryptoSwift
import SwiftKeychainWrapper
import Toaster

class PasswordViewController: BaseViewController {
    
    @IBOutlet weak var topTitleLabel: UILabel!
    @IBOutlet weak var pin0Img: UIImageView!
    @IBOutlet weak var pin1Img: UIImageView!
    @IBOutlet weak var pin2Img: UIImageView!
    @IBOutlet weak var pin3Img: UIImageView!
    @IBOutlet weak var pin4Img: UIImageView!
    var pinImgs: [UIImageView] = [UIImageView]()
    @IBOutlet weak var descriptionLabel: UILabel!
    
    var mTarget: String?
    var mAccount: String?
    var mKey: String?
    var mTargetAccount: String?
    var mTargetAmount: String?
    var mTargetMemo: String?
    var mTargetToken: Token?
    var mTargetCpu: String?
    var mTargetNet: String?
    var mIsConfirmSequence: Bool?
    
    
    var mUserInsert: String = "" {
        willSet(newVal){
            self.onUpdatePinImage(count: newVal.count)
        }
    }
    var mUserConfirm: String  = ""

    override func viewDidLoad() {
        super.viewDidLoad()
        self.pinImgs = [self.pin0Img, self.pin1Img, self.pin2Img, self.pin3Img, self.pin4Img]
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(self.onUserInsert(_:)),
                                               name: Notification.Name("KeyboardClick"),
                                               object: nil)
        self.navigationController?.navigationBar.topItem?.title = "";
        self.initView()
    }
    
    
    func initView() {
        if (mTarget == ADD_USER) {
            topTitleLabel.text = "top_title_init_password".localized()
        } else {
            topTitleLabel.text = "top_title_verify_password".localized()
        }
        topTitleLabel.adjustsFontSizeToFitWidth = true
        
        mIsConfirmSequence = false
        mUserInsert = ""
        mUserConfirm = ""
        
        let value:[String: Int] = ["Page": 0]
        NotificationCenter.default.post(name: Notification.Name("KeyBoardPage"), object: nil, userInfo: value)
    }
    
    func initConfirmView() {
        topTitleLabel.text = "top_title_confirm_password".localized()
        topTitleLabel.adjustsFontSizeToFitWidth = true
        
        self.mIsConfirmSequence = true
        self.mUserConfirm = mUserInsert
        self.mUserInsert = ""
        
        NotificationCenter.default.post(name: Notification.Name("KeyboardShuffle"), object: nil, userInfo: nil)
        let value:[String: Int] = ["Page": 0]
        NotificationCenter.default.post(name: Notification.Name("KeyBoardPage"), object: nil, userInfo: value)
        
    }

    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    
    @objc func onUserInsert(_ notification: NSNotification) {
        if let string = notification.userInfo?["Keyboard"] as? String {
            if(string == "delete") {
                if(mUserInsert.count == 4) {
                    let subString = mUserInsert.prefix(mUserInsert.count - 1)
                    mUserInsert = String(subString)
                    
                    let value:[String: Int] = ["Page": 0]
                    let nc = NotificationCenter.default
                    nc.post(name: Notification.Name("KeyBoardPage"), object: nil, userInfo: value)
                    
                } else if (mUserInsert.count > 0) {
                    let subString = mUserInsert.prefix(mUserInsert.count - 1)
                    mUserInsert = String(subString)
                    
                } else {
                    print("back to stack")
                    
                }
                
            } else {
                mUserInsert.append(string)
                if(mUserInsert.count == 4) {
                    let value:[String: Int] = ["Page": 1]
                    let nc = NotificationCenter.default
                    nc.post(name: Notification.Name("KeyBoardPage"), object: nil, userInfo: value)
                    
                } else if(mUserInsert.count == 5) {
                    self.onUserInsertFinish()
                }
            }
        }
    }
    
    func onUpdatePinImage(count:Int) {
        for i in 0 ..< 5 {
            if(i < count) {
                pinImgs[i].image = UIImage(named: "pin_img");
            } else {
                pinImgs[i].image = UIImage(named: "pin_img_none");
            }
        }
    }
    
    func onUserInsertFinish() {
        if (mTarget == ADD_USER) {
            if(mIsConfirmSequence == true) {
                if(mUserConfirm == mUserInsert) {
                    self.onStartInsertUser()
                    
                } else {
                    Toast(text: "error_two_password_differ".localized(), duration: Delay.short).show()
                    NotificationCenter.default.post(name: Notification.Name("KeyboardShuffle"), object: nil, userInfo: nil)
                    self.initView()
                }
                
            } else {
                self.initConfirmView()
            }
            
        } else if (mTarget == KEY_CHECK) {
            self.onStartCheckKey()
            
        } else if (mTarget == DELETE_USER) {
            self.onStartDeleteUser()
            
        } else if (mTarget == SEND) {
            self.onStartSend()
        }
    }
    
    
    
    func onStartInsertUser() {
        self.showWaittingAlert()
        let queue = DispatchQueue.global()
        queue.async {
            let hashKey = (self.mAccount! + self.mUserInsert).sha1()
            let saveSuccessful: Bool = KeychainWrapper.standard.set(self.mKey!, forKey: hashKey)
            var result :Int64 = -1
            if(saveSuccessful) {
                let newUser = User(account: self.mAccount!, info: hashKey)
                if BaseDao.instance.selectUserByAccount(account: self.mAccount!) != nil {
                    result = BaseDao.instance.updateUser(user: newUser)
                    
                } else {
                    result = BaseDao.instance.insertUser(user: newUser)
                }
            }
            DispatchQueue.main.async(execute: {
                self.hideWaittingAlert()
                print("result : ", result)
                if(result > -1) {
                    let old = NSDate().addingTimeInterval(-86400.0)
                    BaseDao.instance.setLastCheckTime(old)
                    BaseDao.instance.setRecentAccountId((BaseDao.instance.selectUserByAccount(account: self.mAccount!)?.user_id)!)

                    let mainTabController : UIViewController = UIStoryboard(name: "MainTabStoryboard", bundle: nil).instantiateViewController(withIdentifier: "MainTabViewController")
                    let appDelegate = UIApplication.shared.delegate as! AppDelegate
                    appDelegate.window?.rootViewController = mainTabController
                    self.present(mainTabController, animated: true, completion: nil)
                    
                } else {
//                    print("!saveSuccessful")
                    Toast(text: "error_two_password_differ".localized(), duration: Delay.short).show()
                    self.initView()
                }
            })
        }
    }
    
    
    
    func onStartCheckKey() {
        self.showWaittingAlert()
        let queue = DispatchQueue.global()
        queue.async {
            let hashKey = (self.mAccount! + self.mUserInsert).sha1()
            print("hashKey : ", hashKey)
            if let key: String = KeychainWrapper.standard.string(forKey: hashKey) {
                DispatchQueue.main.async(execute: {
                    self.hideWaittingAlert()
                    let keyCheckVC = KeyCheckViewController(nibName: "KeyCheckViewController", bundle: nil) as KeyCheckViewController
                    keyCheckVC.key = key
                    self.navigationController?.pushViewController(keyCheckVC, animated: true)
                    
                })
            } else {
                DispatchQueue.main.async(execute: {
                    self.hideWaittingAlert()
                    Toast(text: "error_invalid_password".localized(), duration: Delay.short).show()
                    self.initView()
                })
            }
        }
        
    }
    
    
    func onStartDeleteUser() {
        self.showWaittingAlert()
        let queue = DispatchQueue.global()
        queue.async {
            let hashKey = (self.mAccount! + self.mUserInsert).sha1()
            if hashKey == BaseDao.instance.selectUserByAccount(account: self.mAccount!)?.user_info {
                DispatchQueue.main.async(execute: {
                    self.hideWaittingAlert()
                    self.deleteAccount(self.mAccount!)
                })
                
            } else {
                DispatchQueue.main.async(execute: {
                    self.hideWaittingAlert()
                    Toast(text: "error_invalid_password".localized(), duration: Delay.short).show()
                    self.initView()
                })
            }
        }
        
    }
    
    
    func onStartSend() {
        self.showWaittingAlert()
        let queue = DispatchQueue.global()
        queue.async {
            var resultVlaue: TransactionResult?
            var errorValue: Error?
            do {
                let hashKey = (self.mAccount! + self.mUserInsert).sha1()
                if let key: String = KeychainWrapper.standard.string(forKey: hashKey) {
                    let transfer = Transfer()
                    transfer.from = self.mAccount!
                    transfer.to = self.mTargetAccount!
                    transfer.quantity = self.mTargetAmount! + " " + (self.mTargetToken?.token_symbol)!
                    transfer.memo = self.mTargetMemo!
                    
                    let importedPk = try PrivateKey(keyString: key)
                    Currency.transferCurrency(transfer: transfer, code: (self.mTargetToken?.token_contractAddr)!, privateKey: importedPk!) { (result, error) in
                        errorValue = error
                        resultVlaue = result
                        if errorValue != nil {
                            Toast(text: String(describing: errorValue?.localizedDescription), duration: Delay.short).show()
                            self.hideWaittingAlert()
                            
                        } else {
                            Toast(text: "send_success".localized(), duration: Delay.short).show()
                            let mainTabController : UIViewController = UIStoryboard(name: "MainTabStoryboard", bundle: nil).instantiateViewController(withIdentifier: "MainTabViewController")
                            let appDelegate = UIApplication.shared.delegate as! AppDelegate
                            appDelegate.window?.rootViewController = mainTabController
                            self.present(mainTabController, animated: true, completion: nil)
                        }
                    }
                }
            } catch {
                DispatchQueue.main.async(execute: {
                    self.hideWaittingAlert()
                    Toast(text: "error_invalid_password".localized(), duration: Delay.short).show()
                })
            }
        
        }
            
    }
}
