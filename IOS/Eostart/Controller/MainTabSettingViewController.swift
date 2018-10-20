//
//  MainTabSettingViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 3..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class MainTabSettingViewController: BaseViewController {

    @IBOutlet weak var titleLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.navigationBar.topItem?.title = "";
        self.navigationController?.setNavigationBarHidden(true, animated: animated)
    }
}
