//
//  BottomLeftButton.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 8..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class BottomLeftButton: UIButton {

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
                self.tintColor = UIColor(hexString: "#FFFFFF")
                self.backgroundColor = UIColor(hexString: "#D6D6D6")
                self.titleLabel?.textColor = UIColor(hexString: "#FFFFFF")
                self.titleLabel?.tintColor = UIColor(hexString: "#FFFFFF")
                
            } else {
                self.tintColor = UIColor(hexString: "#FFFFFF")
                self.backgroundColor = UIColor(hexString: "#5A6482")
                self.titleLabel?.textColor = UIColor(hexString: "#FFFFFF")
                self.titleLabel?.tintColor = UIColor(hexString: "#FFFFFF")
            }
        }
    }

}
