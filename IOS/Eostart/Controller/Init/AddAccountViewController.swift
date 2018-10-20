//
//  AddAccountViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 30..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import Alamofire
import Toaster

class AddAccountViewController: BaseViewController , UITextFieldDelegate{

    @IBOutlet weak var topImageView: UIImageView!
    @IBOutlet weak var descriptionLabel: UILabel!
    @IBOutlet weak var accountInput: InputTextField!
    @IBOutlet weak var countLabel: UILabel!
    @IBOutlet weak var confirmBtn: BottomButton!
    @IBOutlet weak var bottomBtnConstraint: NSLayoutConstraint!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.accountInput.autocorrectionType = .no
        self.accountInput.delegate = self
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard (_:)))
        self.view.addGestureRecognizer(tapGesture)
        
        self.accountInput.addTarget(self, action: #selector(textFieldDidChange(_:)), for: .editingChanged)
       
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.setNavigationBarHidden(false, animated: animated)
        self.navigationController?.navigationBar.backgroundColor = UIColor(hexString: "#3F4863")
        self.navigationItem.title = "title_add_account".localized();

        self.accountInput.textfiledNormal()
        self.confirmBtn.isEnabled = false
        self.textFieldDidChange(accountInput)
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        guard let text = textField.text else { return true }
        let newLength = text.count + string.count - range.length
        return newLength <= 12
    }
    
    func textFieldDidEndEditing(_ textField: UITextField, reason: UITextFieldDidEndEditingReason) {
        self.accountInput.textfiledNormal()
    }
    
    @objc func textFieldDidChange(_ textField: UITextField) {
        if let userinput = accountInput.text {
            if(userinput.count == 0) {
                self.accountInput.textfiledCorrect()
                self.descriptionLabel.textColor = UIColor(hexString: "#2359B8")
                topImageView.image = UIImage(named: "restoreac_ic")
                confirmBtn.isEnabled = false;
                
            } else if(userinput.count < 12) {
                if userinput.matches("^[_a-z1-5]+$") {
                    self.accountInput.textfiledCorrect()
                    self.descriptionLabel.textColor = UIColor(hexString: "#2359B8")
                    topImageView.image = UIImage(named: "restoreac_ic")
                    confirmBtn.isEnabled = false;
                } else {
                    self.accountInput.textfiledError()
                    self.descriptionLabel.textColor = UIColor(hexString: "#FF5656")
                    topImageView.image = UIImage(named: "restoreacerror_ic")
                    confirmBtn.isEnabled = false;
                }
                
            } else {
                if userinput.matches("^[_a-z1-5]{12}$") {
                    self.accountInput.textfiledCorrect()
                    self.descriptionLabel.textColor = UIColor(hexString: "#2359B8")
                    topImageView.image = UIImage(named: "restoreac_ic")
                    confirmBtn.isEnabled = true;
                } else {
                    self.accountInput.textfiledError()
                    self.descriptionLabel.textColor = UIColor(hexString: "#FF5656")
                    topImageView.image = UIImage(named: "restoreacerror_ic")
                    confirmBtn.isEnabled = false;
                }
            }
            
            let attrs1 = [NSAttributedStringKey.font : countLabel.font, NSAttributedStringKey.foregroundColor : UIColor(hexString: "#2359B8")]
            let attrs2 = [NSAttributedStringKey.font : countLabel.font, NSAttributedStringKey.foregroundColor : UIColor(hexString: "#B3B3B3")]
            let attributedString1 = NSMutableAttributedString(string:String(userinput.count), attributes:attrs1 as [NSAttributedStringKey : Any])
            let attributedString2 = NSMutableAttributedString(string:"/12", attributes:attrs2 as [NSAttributedStringKey : Any])
        
            attributedString1.append(attributedString2)
            self.countLabel.attributedText = attributedString1
        }
        
    }
    
    @IBAction func onConfirmClick(_ sender: BottomButton) {
        self.view.endEditing(true)
        self.showWaittingAlert()
        self.reqUserInfo(userId: accountInput.text!)
    }
    
    
    @objc func dismissKeyboard (_ sender: UITapGestureRecognizer) {
        self.view.endEditing(true)
    }
    
    
    func reqUserInfo(userId: String) {
        let parameters: Parameters = ["account_name": userId]
        let request = Alamofire.request(URL_PATH_BP_GET_ACCOUNT,
                                        method: .post,
                                        parameters: parameters,
                                        encoding: JSONEncoding.default)
        request.responseJSON { (response) in
            self.hideWaittingAlert()
            if let status = response.response?.statusCode {
                switch(status){
                case 200:
                    let addKeyController = UIStoryboard(name: "Init", bundle: nil).instantiateViewController(withIdentifier: "AddKeyViewController") as! AddKeyViewController
                    addKeyController.userAccount = userId
                    self.navigationItem.title = ""
                    self.navigationController?.pushViewController(addKeyController, animated: true)
                    
                default:
                    Toast(text: "error_no_account".localized(), duration: Delay.short).show()
                }
            }
            
        }
    }
}

extension String {
    func matches(_ regex: String) -> Bool {
        return self.range(of: regex, options: .regularExpression, range: nil, locale: nil) != nil
    }
}
