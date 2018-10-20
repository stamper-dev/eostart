//
//  LanguageViewCell.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 18..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class LanguageViewCell: UITableViewCell {

    @IBOutlet weak var languageLabel: UILabel!
    @IBOutlet weak var languageCheckBox: UIImageView!
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
