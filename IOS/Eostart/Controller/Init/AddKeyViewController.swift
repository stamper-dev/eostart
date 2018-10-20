//
//  AddKeyViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 3..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import Alamofire
import Toaster

class AddKeyViewController: BaseViewController, UITextFieldDelegate {
    
    

    @IBOutlet weak var keyInput: InputTextField!
    @IBOutlet weak var pasteBtn: UIButton!
    @IBOutlet weak var confirmBtn: BottomButton!
    @IBOutlet weak var bottomBtnConstraint: NSLayoutConstraint!
    
    var userAccount:String?
    
    @IBAction func onClickPaste(_ sender: UIButton) {
        if let myString = UIPasteboard.general.string {
            self.keyInput.text = myString
            self.textFieldDidChange(keyInput)
        } else {
            Toast(text: "error_no_clipboard".localized(), duration: Delay.short).show()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.keyInput.autocorrectionType = .no
        self.keyInput.delegate = self
        self.keyInput.setRightPaddingPoints(60)
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard (_:)))
        self.view.addGestureRecognizer(tapGesture)
        
        self.keyInput.addTarget(self, action: #selector(textFieldDidChange(_:)), for: .editingChanged)

    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.navigationBar.tintColor = UIColor.white;
        self.navigationController?.navigationBar.backgroundColor = UIColor(hexString: "#3F4863")
        self.navigationItem.title = "title_add_key".localized()
        
        self.keyInput.textfiledNormal()
        self.confirmBtn.isEnabled = false
        self.textFieldDidChange(keyInput)
    }
    
    
    @objc func textFieldDidChange(_ textField: UITextField) {
        if let userinput = keyInput.text {
            if(userinput.count == 0) {
                self.confirmBtn.setTitle("skip".localized(), for: .normal);
                self.confirmBtn.setTitle("skip".localized(), for: .disabled);
                self.confirmBtn.isEnabled = true;
                self.keyInput.textfiledNormal()
            } else {
                self.confirmBtn.setTitle("confirm".localized(), for: .normal);
                self.confirmBtn.setTitle("confirm".localized(), for: .disabled);
                if userinput.matches("^[_a-zA-Z0-9]{51}$") {
                    self.confirmBtn.isEnabled = true;
                    self.keyInput.textfiledCorrect()
                    
                } else {
                    self.confirmBtn.isEnabled = false;
                    self.keyInput.textfiledError()
                }
            }
        }
    }
    
    @objc func dismissKeyboard (_ sender: UITapGestureRecognizer) {
        self.view.endEditing(true)
    }
    
    @IBAction func onConfirmClick(_ sender: UIButton) {
        self.view.endEditing(true)
        if(sender.titleLabel?.text == "confirm".localized()) {
            do {
                let importedPk = try PrivateKey(keyString: self.keyInput.text ?? "")
                let importedPub = PublicKey(privateKey: importedPk!)
                print("importedPub ", importedPub.rawPublicKey())
                self.showWaittingAlert()
                self.reqCheckKey(key: importedPub.rawPublicKey())
            } catch {
                print("ERROR")
                Toast(text: "error_invalid_privatekey".localized(), duration: Delay.short).show()
            }
            
        } else {
            self.addUserWithOutKey()
        }
    }
    
    
    func reqCheckKey(key: String) {
        let parameters: Parameters = ["public_key": key]
        let request = Alamofire.request(URL_PATH_BP_CHECK_ACCOUNTS,
                                        method: .post,
                                        parameters: parameters,
                                        encoding: JSONEncoding.default)
        request.responseJSON { (response) in
            self.hideWaittingAlert()
            if let status = response.response?.statusCode {
                switch(status){
                case 200:
                    break
                    
                default:
                    Toast(text: "error_invalid_privatekey".localized(), duration: Delay.short).show()
                    return
                }
                
                if let result = response.result.value {
                    let JSON = result as! NSDictionary
                    let accountArray = JSON.object(forKey: "account_names") as! Array<String>
                    if(accountArray.contains(self.userAccount!)) {
                        self.addUserWithKey()
                    } else {
                        Toast(text: "error_invalid_privatekey".localized(), duration: Delay.short).show()
                    }
                }
            }
        }
    }
    
    func addUserWithOutKey() {
        print("addUserWithOutKey")
        if let already = BaseDao.instance.selectUserByAccount(account: self.userAccount!) {
            BaseDao.instance.setRecentAccountId(already.user_id)
            
        } else {
            let newUser = User(account: self.userAccount!, info: "")
            BaseDao.instance.insertUser(user: newUser)
            BaseDao.instance.setRecentAccountId((BaseDao.instance.selectUserByAccount(account: self.userAccount!)?.user_id)!)
        }
        
        let old = NSDate().addingTimeInterval(-86400.0)
        BaseDao.instance.setLastCheckTime(old)
        
        let mainTabVC = UIStoryboard(name: "MainTabStoryboard", bundle: nil).instantiateViewController(withIdentifier: "MainTabViewController") as! MainTabViewController
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        appDelegate.window?.rootViewController = mainTabVC
        self.present(mainTabVC, animated: true, completion: nil)
    
    }
    
    func addUserWithKey() {
        print("addUserWithKey")
        let passwordController =  UIStoryboard(name: "Password", bundle: nil).instantiateViewController(withIdentifier: "PasswordViewController") as! PasswordViewController
        
        passwordController.mTarget = ADD_USER
        passwordController.mAccount = self.userAccount!
        passwordController.mKey = self.keyInput.text!
        self.navigationItem.title = ""
        self.navigationController?.pushViewController(passwordController, animated: true)
    }
    
}



