//
//  PopupAccountViewCell.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 7..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import DropDown

class PopupAccountViewCell: DropDownCell {

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    @IBOutlet weak var userView: UIView!
    @IBOutlet weak var keyImg: UIImageView!
    @IBOutlet weak var account: UILabel!
    
    @IBOutlet weak var addView: UIView!
    
}
