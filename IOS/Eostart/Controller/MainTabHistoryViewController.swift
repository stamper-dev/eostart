//
//  MainTabHistoryViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 3..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import Alamofire
import DropDown
import Toaster

class MainTabHistoryViewController: BaseViewController, UITableViewDelegate, UITableViewDataSource {
    
    let pageCnt:CLong = 50;
    
    @IBOutlet weak var titleLayer: UIView!
    @IBOutlet weak var titleAccountLabel: UILabel!
    @IBOutlet weak var titleView: UIView!
    @IBOutlet weak var historyTableView: UITableView!
    @IBOutlet weak var noneHistoryView: UIView!
    
    var refresher: UIRefreshControl!
    var dimView: UIView?
    let window = UIApplication.shared.keyWindow!
    let dropDown = DropDown()
    var user: User?
    
    var histories = Array<NSDictionary>()
    var hasMore = false
    var loading = false
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.refresher = UIRefreshControl()
        self.refresher.addTarget(self, action: #selector(handleRefresh), for: .valueChanged)
        self.refresher.tintColor = UIColor(hexString: "#2359B8")
        self.historyTableView.addSubview(refresher)
        
        dimView = UIView(frame: window.bounds)
        dimView!.backgroundColor = UIColor.black
        dimView!.alpha  = 0.6
        
        self.historyTableView.delegate = self
        self.historyTableView.dataSource = self
        self.historyTableView.separatorStyle = UITableViewCellSeparatorStyle.none
        self.historyTableView.register(UINib(nibName: "TransactionViewCell", bundle: nil), forCellReuseIdentifier: "TransactionViewCell")
        
        }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.setNavigationBarHidden(true, animated: animated)
        self.navigationController?.navigationBar.topItem?.title = "";
        
        self.noneHistoryView.isHidden = true
        self.onRefreshUserData()
        
    }
    
    func onRefreshUserData() {
        let recentuserId = BaseDao.instance.getRecentAccountId()
        var users = BaseDao.instance.selectAllUsers()
        if users.count < 1 {
            print("ERROR")
        } else if (recentuserId < 0) {
            user = users[0]
        } else {
            user = users[0]
            for inuser in users {
                if(inuser.user_id == recentuserId) {
                    user = inuser
                    break
                }
            }
        }
        
        titleAccountLabel.text = user?.user_account
        
        self.showWaittingAlert()
        self.onRequestActionsInit(userId: (user?.user_account)!)
        
        
        
        var dropmenu = [String]()
        for inuser in users {
            dropmenu.append(inuser.user_account)
        }
        if(dropmenu.count < 5) {
            dropmenu.append("new user")
        }
        self.titleView.addGestureRecognizer(UITapGestureRecognizer(target: self, action:  #selector(self.titleAccountClick)))
        dropDown.anchorView = self.titleLayer
        dropDown.dismissMode = .automatic
        dropDown.bottomOffset = CGPoint(x: 0, y:(dropDown.anchorView?.plainView.bounds.height)!)
        dropDown.dataSource = dropmenu
        
        dropDown.cellNib = UINib(nibName: "PopupAccountViewCell", bundle: nil)
        dropDown.cellHeight = 54
        dropDown.separatorColor = UIColor.clear
        dropDown.customCellConfiguration = { (index: Index, item: String, cell: DropDownCell) -> Void in
            guard let cell = cell as? PopupAccountViewCell else { return }
            
            if(index == self.dropDown.dataSource.count - 1) {
                cell.userView.isHidden = true
                cell.addView.isHidden = false
            } else {
                cell.account.text = users[index].user_account
                if(BaseDao.instance.hasKeyUser(account: users[index].user_account)) {
                    cell.keyImg.image = UIImage(named: "key_ic")
                } else  {
                    cell.keyImg.image = UIImage(named: "key_ic_none")
                }
                cell.userView.isHidden = false
                cell.addView.isHidden = true
            }
        }
        
        dropDown.cancelAction = { [unowned self] in
            self.dimView?.removeFromSuperview()
        }
        
        dropDown.willShowAction = { [unowned self] in
            self.window.addSubview(self.dimView!);
        }
        
        dropDown.selectionAction = { [unowned self] (index: Int, item: String) in
            self.dimView?.removeFromSuperview()
            if(index == dropmenu.count - 1  && item == "new user") {
                let addAccountVC : UIViewController = UIStoryboard(name: "Init", bundle: nil).instantiateViewController(withIdentifier: "AddAccountViewController")
                addAccountVC.hidesBottomBarWhenPushed = true
                self.navigationItem.title = ""
                self.navigationController?.pushViewController(addAccountVC, animated: true)
                
            } else {
                let old = NSDate().addingTimeInterval(-86400.0)
                BaseDao.instance.setLastCheckTime(old)
                BaseDao.instance.setRecentAccountId(users[index].user_id)
                self.onRefreshUserData()
            }
            
        }
    }
    
    @objc func handleRefresh() {
        self.onRequestActionsInit(userId: (user?.user_account)!)
    }
    
    @objc func titleAccountClick() {
        dropDown.show()
    }

    
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return histories.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let history = histories[indexPath.row]
        let cell:TransactionViewCell? = tableView.dequeueReusableCell(withIdentifier:"TransactionViewCell") as? TransactionViewCell
        cell?.countLabel.text = String(AppUtils.getActionAccoutSeq(history)) + " / " + String(AppUtils.getActionAccoutSeq(self.histories[0]))
        cell?.dateLabel.text = AppUtils.getActionBlocktime(history)
        cell?.txidLabel.text = AppUtils.getActionTxid(history)
        cell?.memoLabel.text = AppUtils.getActionMemo(history)
        
        if(AppUtils.getActionName(history) == "transfer") {
            if (AppUtils.getActionFrom(history) == user?.user_account) {
                cell?.typeLabel.text = "Sent"
                cell?.amountLabel.textColor = UIColor(hexString: "#FF5656")
                cell?.amountLabel.text = "- " + AppUtils.getActionQuantity(history)
                cell?.otherUserLabel.text = AppUtils.getActionTo(history)
                
            } else if (AppUtils.getActionTo(history) == user?.user_account) {
                cell?.typeLabel.text = "Received"
                cell?.amountLabel.textColor = UIColor(hexString: "#00CF1C")
                cell?.amountLabel.text = AppUtils.getActionQuantity(history)
                cell?.otherUserLabel.text = AppUtils.getActionName(history)
            }
            
        } else if (AppUtils.getActionName(history).count > 0) {
            cell?.typeLabel.text = AppUtils.getActionName(history)
            cell?.amountLabel.text = ""
            cell?.memoLabel.text = AppUtils.getActionActMessage(history)
            cell?.otherUserLabel.text = AppUtils.getActionActAccount(history)
            
        } else {
            cell?.typeLabel.text = "ETC"
            cell?.amountLabel.text = ""
            cell?.otherUserLabel.text = ""
            cell?.otherUserLabel.text = ""
        }
        
        return cell!
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 180;
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        let lastItem = self.histories.count - 1
        if(indexPath.row == lastItem && self.hasMore && !self.loading) {
            var newStartoffset:CLong = 0
            var newOffset:CLong = pageCnt
            if(AppUtils.getActionAccoutSeq(self.histories[self.histories.count - 1]) <= pageCnt) {
                newOffset = AppUtils.getActionAccoutSeq(self.histories[self.histories.count - 1]) - 1;
            } else {
                newStartoffset = AppUtils.getActionAccoutSeq(self.histories[self.histories.count - 1]) - pageCnt  - 1;
            }
            self.onRequestActionsMore(userId: (self.user?.user_account)!, startOffset: newStartoffset, offset: newOffset)
        }
    }
    
    func onRequestActionsInit(userId: String) {
        self.loading = true
        self.hasMore = false
        let parameters: Parameters = ["account_name":userId, "pos":-1, "offset":-pageCnt]
        let request = Alamofire
            .request(URL_PATH_BP_GET_ACTIONS,
                     method: .post,
                     parameters: parameters,
                     encoding: JSONEncoding.default);
        request.responseJSON { (response) in
            if let status = response.response?.statusCode {
                switch(status){
                case 200:
                    break
                    
                default:
                    self.hideWaittingAlert()
                    Toast(text: "error_network".localized(), duration: Delay.short).show()
                    return
                }
            }
            
            self.histories.removeAll()
            if let actions = (response.result.value as? NSDictionary)?.object(forKey: "actions") as? Array<NSDictionary> {
                let sortedDic = actions.sorted { (aDic, bDic) -> Bool in
                    return (aDic.object(forKey: "account_action_seq") as? CLong)! > (bDic.object(forKey: "account_action_seq") as? CLong)!
                }
                self.histories.append(contentsOf: sortedDic)
                
                if(actions.count >= self.pageCnt) {
                    self.hasMore = true
                }
                
                if (self.histories.count > 0) {
                    self.historyTableView.reloadData()
                    
                } else {
                    self.noneHistoryView.isHidden = false
                }
            }
            self.refresher.endRefreshing()
            self.hideWaittingAlert()
            self.loading = false
        }
    }
    
    
    func onRequestActionsMore(userId: String, startOffset:CLong, offset:CLong) {
        self.loading = true
        self.hasMore = false
        let parameters: Parameters = ["account_name":userId, "pos":startOffset, "offset":offset]
        let request = Alamofire
            .request(URL_PATH_BP_GET_ACTIONS,
                     method: .post,
                     parameters: parameters,
                     encoding: JSONEncoding.default);
        
        request.responseJSON { (response) in
            if let status = response.response?.statusCode {
                switch(status){
                case 200:
                    break
                    
                default:
                    self.hideWaittingAlert()
                    Toast(text: "error_network".localized(), duration: Delay.short).show()
                    return
                }
            }
            
            if let actions = (response.result.value as? NSDictionary)?.object(forKey: "actions") as? Array<NSDictionary> {
                let sortedDic = actions.sorted { (aDic, bDic) -> Bool in
                    return (aDic.object(forKey: "account_action_seq") as? CLong)! > (bDic.object(forKey: "account_action_seq") as? CLong)!
                }
                self.histories.append(contentsOf: sortedDic)

                if(actions.count >= self.pageCnt) {
                    self.hasMore = true
                }

                if (self.histories.count > 0) {
                    self.historyTableView.reloadData()
                }
            }
            self.refresher.endRefreshing()
            self.hideWaittingAlert()
            self.loading = false
        }
        
    }
}
