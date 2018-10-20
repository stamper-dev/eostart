//
//  TransactionViewCell.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 10..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class TransactionViewCell: UITableViewCell {

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    @IBOutlet weak var countLabel: UILabel!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var otherUserLabel: UILabel!
    @IBOutlet weak var txidLabel: UILabel!
    @IBOutlet weak var memoLabel: UILabel!
    @IBOutlet weak var typeLabel: UILabel!
    @IBOutlet weak var amountLabel: UILabel!
    
}
