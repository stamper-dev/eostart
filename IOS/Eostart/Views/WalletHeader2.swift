//
//  WalletHeader2.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 10..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import Foundation
import UIKit

class WalletHeader2: UIView {
    private let xibName = "WalletHeader2"
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.commonInit()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.commonInit()
    }
    
    override func prepareForInterfaceBuilder() {
        super.prepareForInterfaceBuilder()
        self.commonInit()
    }
    
    private func commonInit(){
        let view = Bundle.main.loadNibNamed(xibName, owner: self, options: nil)?.first as! UIView
        view.frame = self.bounds
        self.addSubview(view)
    }
    @IBOutlet weak var countLabel: UILabel!
}
