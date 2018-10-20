//
//  BottomButton.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 2..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class BottomButton: UIButton {

    required init(coder aDecoder: NSCoder){
        super.init(coder: aDecoder)!
        self.layer.cornerRadius = 0
        self.titleLabel?.font = UIFont(name:"HelveticaNeue-Bold", size: 17.0)
        self.tintColor = UIColor.clear
        self.isEnabled = false
        self.alpha = 1.0;
    }
    
    
    override var isEnabled: Bool {
        didSet {
            if (self.isEnabled == false) {
//                self.backgroundColor = UIColor(hexString: "#D6D6D6")
//                self.titleLabel?.textColor = UIColor(hexString: "#959595")
                self.tintColor = UIColor(hexString: "#959595")
                self.backgroundColor = UIColor(hexString: "#D6D6D6")
                self.titleLabel?.textColor = UIColor(hexString: "#959595")
                self.titleLabel?.tintColor = UIColor(hexString: "#959595")
                
            } else {
//                self.backgroundColor = UIColor.clear
//                self.titleLabel?.textColor = UIColor(hexString: "#FFFFFF")
                self.tintColor = UIColor(hexString: "#FFFFFF")
                self.backgroundColor = UIColor(hexString: "#2359B8")
                self.titleLabel?.textColor = UIColor(hexString: "#FFFFFF")
                self.titleLabel?.tintColor = UIColor(hexString: "#FFFFFF")
            }
        }
    }
}


