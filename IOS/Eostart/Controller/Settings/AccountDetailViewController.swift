//
//  AccountDetailViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 8..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class AccountDetailViewController: BaseViewController {
    
    @IBOutlet weak var accountLabel: UILabel!
    @IBOutlet weak var warnningView: CardView!
    @IBOutlet weak var descrip1: UILabel!
    @IBOutlet weak var descrip2: UILabel!
    @IBOutlet weak var actionBtn: UIButton!
    @IBOutlet weak var deleteBtn: BottomButton!
    
    @IBAction func actionClick(_ sender: UIButton) {
        if(BaseDao.instance.hasKeyUser(account: (user?.user_account)!)) {
            let passwordController =  UIStoryboard(name: "Password", bundle: nil).instantiateViewController(withIdentifier: "PasswordViewController") as! PasswordViewController
            
            passwordController.mTarget = KEY_CHECK
            passwordController.mAccount = user?.user_account
            self.navigationItem.title = ""
            self.navigationController?.pushViewController(passwordController, animated: true)
            
        } else {
            let alertController = UIAlertController(title: "add_key".localized(), message: "key_add_des".localized(), preferredStyle: UIAlertControllerStyle.alert)
            let cancelAction = UIAlertAction(title: "cancel".localized(), style: UIAlertActionStyle.default) { (result : UIAlertAction) -> Void in
            }
            let okAction = UIAlertAction(title: "add_key2".localized(), style: UIAlertActionStyle.destructive) { (result : UIAlertAction) -> Void in
                let addKeyController = UIStoryboard(name: "Init", bundle: nil).instantiateViewController(withIdentifier: "AddKeyViewController") as! AddKeyViewController
                addKeyController.userAccount = self.user?.user_account
                self.navigationItem.title = ""
                self.navigationController?.pushViewController(addKeyController, animated: true)
            }
            alertController.addAction(cancelAction)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    @IBAction func deleteClick(_ sender: UIButton) {
        if(BaseDao.instance.hasKeyUser(account: (user?.user_account)!)) {
            let passwordController =  UIStoryboard(name: "Password", bundle: nil).instantiateViewController(withIdentifier: "PasswordViewController") as! PasswordViewController
            
            passwordController.mTarget = DELETE_USER
            passwordController.mAccount = user?.user_account
            self.navigationItem.title = ""
            self.navigationController?.pushViewController(passwordController, animated: true)
            
        } else {
            let alertController = UIAlertController(title: "delete_account".localized(), message: "delete_des".localized(), preferredStyle: UIAlertControllerStyle.alert)
            let cancelAction = UIAlertAction(title: "cancel".localized(), style: UIAlertActionStyle.default) { (result : UIAlertAction) -> Void in
            }
            let okAction = UIAlertAction(title: "delete_account".localized(), style: UIAlertActionStyle.destructive) { (result : UIAlertAction) -> Void in
                self.deleteAccount((self.user?.user_account)!)
            }
            alertController.addAction(cancelAction)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    var user: User?

    override func viewDidLoad() {
        super.viewDidLoad()
        
        accountLabel.text = user?.user_account
        
        descrip1.text = "key_check_des1".localized()
        descrip2.text = "key_check_des2".localized()
        
        if(BaseDao.instance.hasKeyUser(account: (user?.user_account)!)) {
            warnningView.isHidden = false
            actionBtn.setTitle("check_key".localized(), for: .normal);
            actionBtn.setTitle("check_key".localized(), for: .disabled);
        } else  {
            warnningView.isHidden = true
            actionBtn.setTitle("add_key".localized(), for: .normal);
            actionBtn.setTitle("add_key".localized(), for: .disabled);
        }
        deleteBtn.setTitle("delete_account".localized(), for: .normal);
        deleteBtn.setTitle("delete_account".localized(), for: .disabled);
        
        actionBtn.isEnabled = true
        deleteBtn.isEnabled = true

    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.setNavigationBarHidden(false, animated: animated)
        navigationItem.title = "set_account".localized()
    }

}
