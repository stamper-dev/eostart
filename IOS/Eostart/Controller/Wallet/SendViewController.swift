//
//  SendViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 11..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage
import Toaster

class SendViewController: BaseViewController, UITextFieldDelegate{
    
    @IBOutlet weak var mainScrollView: UIScrollView!
    @IBOutlet weak var tokenSymbolImg: UIImageView!
    @IBOutlet weak var tokenAmoutLabel: UILabel!
    @IBOutlet weak var tokenValueLabel: UILabel!
    @IBOutlet weak var targetAccountTextField: InputTextField!
    @IBOutlet weak var targetAmountTextField: InputTextField!
    @IBOutlet weak var targetMemoTextField: InputTextField!
    @IBOutlet weak var sendBtn: BottomButton!
    @IBOutlet weak var recentBtn: UIButton!
    @IBOutlet weak var valueLabel: UILabel!
    @IBOutlet weak var memoCntLabel: UILabel!
    @IBOutlet weak var sendBtnConstraint: NSLayoutConstraint!
    @IBAction func addClick(_ sender: UIButton) {
        var inputAmount = NSDecimalNumber(string: self.targetAmountTextField.text?.replacingOccurrences(of: ",", with: ""))
        if(inputAmount == NSDecimalNumber.notANumber) {
            inputAmount = NSDecimalNumber.zero
        }
        var result: NSDecimalNumber = NSDecimalNumber.zero
        if(sender.titleLabel?.text == "+ 1") {
            result = inputAmount.adding(NSDecimalNumber(string: "1"))
            
        } else if (sender.titleLabel?.text == "+ 10") {
            result = inputAmount.adding(NSDecimalNumber(string: "10"))
            
        } else if (sender.titleLabel?.text == "+ 50") {
            result = inputAmount.adding(NSDecimalNumber(string: "50"))
            
        } else if (sender.titleLabel?.text == "+ 100") {
            result = inputAmount.adding(NSDecimalNumber(string: "100"))
            
        } else {
            result = self.token!.token_userAmount
        }
        self.targetAmountTextField.text = AppUtils.userInputAmoutFormat(amount: result, deciaml: self.token!.token_decimals)
        self.textFieldDidChange(self.targetAmountTextField)
        self.upDateBottmEnabale()
    }
    @IBAction func recentClick(_ sender: UIButton) {
        if(self.recentList?.count ?? 0 > 0) {
            let alertController = UIAlertController(title: "recent_account".localized(), message: "", preferredStyle: UIAlertControllerStyle.alert)
            
            for other in recentList! {
                let btn5 = UIAlertAction(title: other, style: UIAlertActionStyle.default) { (result : UIAlertAction) -> Void in
                    self.targetAccountTextField.text = other
                    self.textFieldDidChange(self.targetAccountTextField)
                    self.upDateBottmEnabale()
                }
                alertController.addAction(btn5)
            }
            
            self.present(alertController, animated: true) {
                let tapGesture = UITapGestureRecognizer(target: self, action: #selector(self.dismissAlertController))
                alertController.view.superview?.subviews[0].addGestureRecognizer(tapGesture)
            }
            
        } else {
            Toast(text: "error_no_recent".localized(), duration: Delay.short).show()
        }
    }
    @IBAction func sendClick(_ sender: Any) {
        self.view.endEditing(true)
        
        let passwordController =  UIStoryboard(name: "Password", bundle: nil).instantiateViewController(withIdentifier: "PasswordViewController") as! PasswordViewController
        passwordController.mTarget = SEND
        passwordController.mAccount = user?.user_account
        passwordController.mTargetAccount = self.targetAccountTextField.text
        passwordController.mTargetAmount = self.targetAmountTextField.text?.replacingOccurrences(of: ",", with: "")
        passwordController.mTargetMemo = self.targetMemoTextField.text
        passwordController.mTargetToken = self.token
        self.navigationController?.pushViewController(passwordController, animated: true)
    }
    
    var token: Token?
    var user: User?
    var recentList: Array<String>?
    
    
    @IBOutlet weak var contentsView: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tokenAmoutLabel.attributedText = AppUtils.displayAmout(token!.token_userAmount, font: self.tokenAmoutLabel.font, symbol: token!.token_symbol, deciaml: token!.token_decimals)
        if(token!.token_contractAddr == "eosio.token" && token!.token_userAmount.compare(NSDecimalNumber.zero) == .orderedDescending) {
            self.tokenValueLabel.text = AppUtils.getDisplayCash(BaseDao.instance.getUserCurrencyS(), tic: BaseDao.instance.getEosTic()!, amount: token!.token_userAmount)
            self.valueLabel.isHidden = false
        } else {
            self.tokenValueLabel.text = AppUtils.getDisplayCash(BaseDao.instance.getUserCurrencyS(), tic: BaseDao.instance.getEosTic()!, amount: NSDecimalNumber.zero)
            self.valueLabel.isHidden = true
        }
        Alamofire.request(token!.token_iconUrl, method: .get).responseImage { response  in
            guard let image = response.result.value else {
                self.tokenSymbolImg.image = nil
                return
            }
            self.tokenSymbolImg.image = image
        }
        
        targetAccountTextField.delegate = self
        targetAmountTextField.delegate = self
        targetMemoTextField.delegate = self
        self.targetAccountTextField.addTarget(self, action: #selector(textFieldDidChange(_:)), for: .editingChanged)
        self.targetAmountTextField.addTarget(self, action: #selector(textFieldDidChange(_:)), for: .editingChanged)
        self.targetMemoTextField.addTarget(self, action: #selector(textFieldDidChange(_:)), for: .editingChanged)
        
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard (_:)))
        self.view.addGestureRecognizer(tapGesture)
        
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        self.navigationController?.setNavigationBarHidden(false, animated: animated)
        self.navigationItem.title = "Send"
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    
    @objc func dismissKeyboard (_ sender: UITapGestureRecognizer) {
        self.view.endEditing(true)
    }
    
    @objc func dismissAlertController(){
        self.dismiss(animated: true, completion: nil)
    }
    
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        guard let text = textField.text else { return true }
        
        if(textField == targetAccountTextField) {
            let newLength = text.count + string.count - range.length
            return newLength <= 12

        } else if (textField == targetAmountTextField) {
            if (text.contains(".") && string.contains(".") && range.length == 0) {
                 return false
            }
            if let index = text.range(of: ".")?.upperBound {
                if(text.substring(from: index).count > token!.token_decimals - 1 && range.length == 0) {
                    return false
                }
            }
        } else if (textField == targetMemoTextField) {
            let byteArray = [UInt8](text.utf8)
            if(byteArray.count > 255) { return false}
        }
        
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField, reason: UITextFieldDidEndEditingReason) {
        
        if (textField == targetAccountTextField) {
            
        } else if (textField == targetAmountTextField) {
            guard var input = textField.text else { return }
            input = input.replacingOccurrences(of: ",", with: "")
            
            if let amount = NSDecimalNumber(string: input) as? NSDecimalNumber {
                if(amount.compare(NSDecimalNumber.zero) == .orderedAscending || amount.compare(NSDecimalNumber.zero) == .orderedSame ) {
                    self.targetAmountTextField.text = ""
                    self.targetAmountTextField.textfiledNormal()
                } else {
                    self.targetAmountTextField.text = AppUtils.userInputAmoutFormat(amount: amount, deciaml: self.token!.token_decimals)
                }
            }
            
            
        }
        upDateBottmEnabale()
    }
    
    @objc func textFieldDidChange(_ textField: UITextField) {
        if (textField == targetAccountTextField) {
            if let userinput = textField.text {
                if(userinput.count == 0) {
                    self.targetAccountTextField.textfiledNormal()
                    
                } else if userinput.matches("^[_a-z1-5]{12}$") {
                    self.targetAccountTextField.textfiledCorrect()
                    
                } else {
                    self.targetAccountTextField.textfiledError()
                    
                }
            }
        } else if (textField == targetAmountTextField) {
            if var userinput = textField.text {
                userinput = userinput.replacingOccurrences(of: ",", with: "")
                if(userinput.count == 0) {
                    self.targetAmountTextField.textfiledNormal()

                } else if let inputAmount = NSDecimalNumber(string: userinput) as? NSDecimalNumber {
                    if(inputAmount == NSDecimalNumber.notANumber) {
                        self.targetAmountTextField.textfiledError()
                        
                    } else {
                        if(self.token!.token_userAmount.compare(inputAmount) == .orderedDescending ||
                            self.token!.token_userAmount.compare(inputAmount) == .orderedSame ) {
                            self.targetAmountTextField.textfiledCorrect()
                        } else {
                            self.targetAmountTextField.textfiledError()
                        }
                        
                        if(!self.valueLabel.isHidden) {
                            self.valueLabel.text = "~ " + AppUtils.getDisplayCash(BaseDao.instance.getUserCurrencyS(), tic: BaseDao.instance.getEosTic()!, amount: inputAmount)
                        }
                    }

                } else {
                    self.targetAmountTextField.textfiledError()
                }
            }
            
        } else if (textField == targetMemoTextField) {
            if let userinput = textField.text {
                if(userinput.count == 0) {
                    self.targetMemoTextField.textfiledNormal()
                } else {
                    self.targetMemoTextField.textfiledCorrect()
                }
                let byteArray = [UInt8](userinput.utf8)
                self.memoCntLabel.text = "(" + String(byteArray.count) + "/255)"
                
            }
        }
        upDateBottmEnabale()
        
    }
    
    func upDateBottmEnabale() {
        if(self.targetAccountTextField.isCorrect && self.targetAmountTextField.isCorrect) {
            self.sendBtn.isEnabled = true
        } else {
            self.sendBtn.isEnabled = false
        }
    }
    
}
