//
//  AppLockSetViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 18..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import Toaster
import LocalAuthentication


enum AppLockSetMode: Int {
    case Enable     = 0
    case Disable    = 1
}

class AppLockSetViewController: UIViewController {
    
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
    var mode: AppLockSetMode = AppLockSetMode.Enable
    var isConfirm: Bool?
    var userInsert: String = "" {
        willSet(newVal){
            self.onUpdatePinImage(count: newVal.count)
        }
    }
    var userConfirm: String  = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.deciamlBtns = [self.deciamlBtn0, self.deciamlBtn1, self.deciamlBtn2,
                            self.deciamlBtn3, self.deciamlBtn4, self.deciamlBtn5,
                            self.deciamlBtn6, self.deciamlBtn7, self.deciamlBtn8, self.deciamlBtn9]
        self.pinImgs = [self.pin0Img, self.pin1Img, self.pin2Img, self.pin3Img]

    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.setNavigationBarHidden(false, animated: false)
        self.initView()
    }
    
    func initView() {
        self.onRefreshKeyBoard()
        if (mode == AppLockSetMode.Enable) {
            self.topTitleLabel.text = "top_title_applock_enable".localized()
            self.descriptionLabel.text = "top_title_applock_description".localized()
            self.navigationItem.title = "title_app_lock".localized()
            
        } else if (mode == AppLockSetMode.Disable) {
            self.topTitleLabel.text = "top_title_applock_unlock".localized()
            self.descriptionLabel.text = ""
            self.navigationItem.title = "title_app_lock".localized()
            
        }
        
        isConfirm = false
        userInsert = ""
        userConfirm = ""
        
    }

    
    func initConfirmView() {
        self.onRefreshKeyBoard()
        self.topTitleLabel.text = "top_title_applock_confirm".localized()
        
        isConfirm = true
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
        if (mode == AppLockSetMode.Enable) {
            if(isConfirm == true) {
                if(userConfirm == userInsert) {
                    BaseDao.instance.setUsingAppLockKey(userConfirm)
                    BaseDao.instance.setUsingAppLock(true)
                    Toast(text: "msg_applock_enabeled".localized(), duration: Delay.short).show()
                    self.onUserCancel()
                    
                } else {
                    Toast(text: "error_two_password_differ".localized(), duration: Delay.short).show()
                    self.initView()
                }
                
            } else {
                userConfirm = userInsert
                self.initConfirmView()
            }
            
        } else if (mode == AppLockSetMode.Disable) {
            if(userInsert == BaseDao.instance.getUsingAppLockKey()) {
                BaseDao.instance.setUsingAppLock(false)
                Toast(text: "msg_applock_disabeled".localized(), duration: Delay.short).show()
                self.onUserCancel()
                
            } else {
                Toast(text: "error_two_password_differ".localized(), duration: Delay.short).show()
                self.initView()
                
            }
            
        }
    }
    
    func onUserCancel() {
       self.navigationController?.popViewController(animated: true)
    }
}
