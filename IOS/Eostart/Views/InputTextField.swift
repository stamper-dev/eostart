//
//  InputTextField.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 2..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class InputTextField: UITextField {

    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */
    
//    override func layoutSubviews() {
//        layer.cornerRadius = cornerRadius
//        let shadowPath = UIBezierPath(roundedRect: bounds, cornerRadius: cornerRadius)
//
//        layer.masksToBounds = false
//        layer.shadowColor = shadowColor?.cgColor
//        layer.shadowOffset = CGSize(width: shadowOffsetWidth, height: shadowOffsetHeight);
//        layer.shadowOpacity = shadowOpacity
//        layer.shadowPath = shadowPath.cgPath
//    }
    
    let border = CALayer()
    var isCorrect = false;
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.layer.cornerRadius = 0.0
        self.backgroundColor = UIColor.white
        self.setLeftPaddingPoints(5)
        self.setRightPaddingPoints(5)
        self.textfiledNormal()
    }
    
    func textfiledNormal() {
        self.layer.borderColor = UIColor(hexString: "#ECECEC").cgColor
        self.layer.borderWidth = 1.5
        self.layer.cornerRadius =  0
        
        self.border.removeFromSuperlayer()
        border.frame = CGRect(x: 0, y: self.frame.height - 2, width: self.frame.width, height: 2)
        border.backgroundColor = UIColor(hexString: "#ECECEC").cgColor
        layer.addSublayer(border)
        isCorrect = false;
    }
    
    func textfiledCorrect() {
        self.layer.borderColor = UIColor(hexString: "#ECECEC").cgColor
        self.layer.borderWidth = 1.5
        
        self.border.removeFromSuperlayer()
        border.frame = CGRect(x: 0, y: self.frame.height - 4, width: self.frame.width, height: 4)
        border.backgroundColor = UIColor(hexString: "#2359B8").cgColor
        self.layer.addSublayer(border)
        isCorrect = true;
    }
    
    func textfiledError() {
        self.layer.borderColor = UIColor(hexString: "#ECECEC").cgColor
        self.layer.borderWidth = 1.5
        
        self.border.removeFromSuperlayer()
        border.frame = CGRect(x: 0, y: self.frame.height - 4, width: self.frame.width, height: 4)
        border.backgroundColor = UIColor(hexString: "#FF5656").cgColor
        self.layer.addSublayer(border)
        isCorrect = false;
    }

}

extension UITextField {
    func setLeftPaddingPoints(_ amount:CGFloat){
        let paddingView = UIView(frame: CGRect(x: 0, y: 0, width: amount, height: self.frame.size.height))
        self.leftView = paddingView
        self.leftViewMode = .always
    }
    func setRightPaddingPoints(_ amount:CGFloat) {
        let paddingView = UIView(frame: CGRect(x: 0, y: 0, width: amount, height: self.frame.size.height))
        self.rightView = paddingView
        self.rightViewMode = .always
    }
}


