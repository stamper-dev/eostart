//
//  AccountViewCell.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 8..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class AccountViewCell: UITableViewCell {

    @IBOutlet weak var keyImg: UIImageView!
    @IBOutlet weak var accountLabel: UILabel!
    @IBOutlet weak var separator: UIView!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
