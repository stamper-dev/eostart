//
//  CurrencyViewCell.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 19..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class CurrencyViewCell: UITableViewCell {

    @IBOutlet weak var currencyImage: UIImageView!
    @IBOutlet weak var currencyLabel: UILabel!
    @IBOutlet weak var currencyChecked: UIImageView!
    @IBOutlet weak var separateLine: UIView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
