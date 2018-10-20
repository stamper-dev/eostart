//
//  AccountManageViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 14..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class AccountManageViewController: BaseViewController , UITableViewDelegate, UITableViewDataSource {
    
    var users = [User]()
    

    @IBOutlet weak var mainTableView: UITableView!
    @IBOutlet weak var addAccountBtn: BottomLeftButton!
    @IBOutlet weak var createAccountBtn: BottomRightButton!
    @IBAction func addClick(_ sender: Any) {
        let addAccountVc = UIStoryboard(name: "Init", bundle: nil).instantiateViewController(withIdentifier: "AddAccountViewController") as! AddAccountViewController
        self.navigationItem.title = ""
        self.navigationController?.pushViewController(addAccountVc, animated: true)
    }
    @IBAction func createClick(_ sender: Any) {
        guard let url = URL(string: "http://www.eostart.com") else {
            return
        }
        if #available(iOS 10.0, *) {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        } else {
            UIApplication.shared.openURL(url)
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        users = BaseDao.instance.selectAllUsers()
        
        if(users.count >= 5) {
            self.addAccountBtn.isEnabled = false
        } else {
            self.addAccountBtn.isEnabled = true
        }
        self.createAccountBtn.isEnabled = true
        
        self.mainTableView.delegate = self
        self.mainTableView.dataSource = self
        self.mainTableView.separatorStyle = UITableViewCellSeparatorStyle.none
        self.mainTableView.register(UINib(nibName: "AccountViewCell", bundle: nil), forCellReuseIdentifier: "AccountViewCell")
    }

    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.setNavigationBarHidden(false, animated: animated)
        self.navigationItem.title = "set_account".localized()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return users.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell:AccountViewCell? = tableView.dequeueReusableCell(withIdentifier:"AccountViewCell") as? AccountViewCell
        cell?.accountLabel.text = users[indexPath.row].user_account
        if(BaseDao.instance.hasKeyUser(account: users[indexPath.row].user_account)) {
            cell?.keyImg.image = UIImage(named: "key_ic")
        } else  {
            cell?.keyImg.image = UIImage(named: "key_ic_none")
        }
        
        if(indexPath.row == users.count - 1) {
            cell?.separator.isHidden = true
        } else {
            cell?.separator.isHidden = false
        }
        
        return cell!
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 54;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let accountDetailVC = AccountDetailViewController(nibName: "AccountDetailViewController", bundle: nil)
        accountDetailVC.user = users[indexPath.row]
        self.navigationItem.title = ""
        self.navigationController?.pushViewController(accountDetailVC, animated: true)
    }
}
