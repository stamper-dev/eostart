//
//  LanguageManageViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 18..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
class LanguageManageViewController: BaseViewController , UITableViewDelegate, UITableViewDataSource {
    
    
    @IBOutlet weak var mainTableView: UITableView!
    
    var language = [String?]();
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.mainTableView.delegate = self
        self.mainTableView.dataSource = self
        self.mainTableView.separatorStyle = UITableViewCellSeparatorStyle.none
        self.mainTableView.register(UINib(nibName: "LanguageViewCell", bundle: nil), forCellReuseIdentifier: "LanguageViewCell")
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.setNavigationBarHidden(false, animated: animated)
        self.initStringData()
    }
    
    func initStringData() {

        navigationItem.title = "set_language".localized()
        
        language = ["language_default".localized(),
                    "language_korean".localized(),
                    "language_english".localized()]
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return language.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell:LanguageViewCell? = tableView.dequeueReusableCell(withIdentifier:"LanguageViewCell") as? LanguageViewCell
        cell?.languageLabel.text = language[indexPath.row]
        
        if(BaseDao.instance.getUserLangauage() == indexPath.row) {
            cell?.languageCheckBox.image = UIImage(named: "checkic_on")
        } else {
            cell?.languageCheckBox.image = UIImage(named: "checkic_off")
        }
        
        if(indexPath.row == language.count - 1) {
            cell?.separateLine.isHidden = true
        } else {
            cell?.separateLine.isHidden = false
        }
        return cell!
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 54
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if (indexPath.row == 0) {
            BaseDao.instance.setUserLangauage(0)
            
        } else if (indexPath.row == 1) {
            BaseDao.instance.setUserLangauage(1)
            
        } else if (indexPath.row == 2) {
            BaseDao.instance.setUserLangauage(2)
            
        }
        initStringData()
        self.mainTableView.reloadData()
    }
    
}
