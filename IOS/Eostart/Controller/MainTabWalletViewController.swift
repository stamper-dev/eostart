//
//  MainTabWalletViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 3..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage
import DropDown

class MainTabWalletViewController: BaseViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var titleLayer: UIView!
    @IBOutlet weak var titleAccountLabel: UILabel!
    @IBOutlet weak var titleView: UIView!
    @IBOutlet weak var tokensTableView: UITableView!
    
    var refresher: UIRefreshControl!
    var tokens = [Token]()
    var eosTic: NSDictionary!
    
    var dimView: UIView?
    let window = UIApplication.shared.keyWindow!
    let dropDown = DropDown()
    var user: User?
    var scrolling: Bool = false;
    var toUpdateRow = [IndexPath]()
    

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.refresher = UIRefreshControl()
        self.refresher.addTarget(self, action: #selector(handleRefresh), for: .valueChanged)
        self.refresher.tintColor = UIColor(hexString: "#2359B8")
        self.tokensTableView.addSubview(refresher)
        
        dimView = UIView(frame: window.bounds)
        dimView!.backgroundColor = UIColor.black
        dimView!.alpha  = 0.6

        self.tokensTableView.delegate = self
        self.tokensTableView.dataSource = self
        self.tokensTableView.separatorStyle = UITableViewCellSeparatorStyle.none
        self.tokensTableView.register(UINib(nibName: "TokensViewCell", bundle: nil), forCellReuseIdentifier: "TokensViewCell")
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.setNavigationBarHidden(true, animated: animated)
        self.navigationController?.navigationBar.topItem?.title = "";
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
        tokens = BaseDao.instance.selectAllTokens();
        
        self.showWaittingAlert()
        if(BaseDao.instance.getLastCheckTime() == nil) {
            self.reqEosTic()
        } else {
            let checked = UInt64(floor((BaseDao.instance.getLastCheckTime()?.timeIntervalSince1970)!)) + UInt64(60)
            let now     = UInt64(floor(NSDate().timeIntervalSince1970))
            if(checked < now) {
                self.reqEosTic()
            } else {
                self.eosTic = BaseDao.instance.getEosTic()
                self.tokensTableView.beginUpdates()
                self.tokensTableView.reloadData()
                self.tokensTableView.endUpdates()
                self.hideWaittingAlert()
            }
        }
        
        
        
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
        self.reqEosTic()
    }
    
    @objc func titleAccountClick() {
        dropDown.show()
    }

    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tokens.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let token = tokens[indexPath.row]
        let cell:TokensViewCell? = tableView.dequeueReusableCell(withIdentifier:"TokensViewCell") as? TokensViewCell

        Alamofire.request(token.token_iconUrl, method: .get).responseImage { response  in
            guard let image = response.result.value else {
                cell?.tokenImage.image = nil
                return
            }
            cell?.tokenImage.image = image
        }
        cell?.symbolLabel.text = token.token_symbol
        cell?.descriptionLabel.text = token.token_name
        
        cell?.amountLabel.attributedText = AppUtils.displayAmout(NSDecimalNumber.zero, font: (cell?.amountLabel.font)!, symbol: "", deciaml: token.token_decimals)
        if(token.token_userAmount != NSDecimalNumber.notANumber) {
            cell?.amountLabel.attributedText = AppUtils.displayAmout(token.token_userAmount, font: (cell?.amountLabel.font)!, symbol: "", deciaml: token.token_decimals)
            if(token.token_contractAddr == "eosio.token" && token.token_userAmount.compare(NSDecimalNumber.zero) == .orderedDescending) {
                cell?.valueLabel.text = AppUtils.getDisplayCash(BaseDao.instance.getUserCurrencyS(), tic: BaseDao.instance.getEosTic()!, amount: token.token_userAmount)
            } else {
                cell?.valueLabel.text = AppUtils.getDisplayCash(BaseDao.instance.getUserCurrencyS(), tic: BaseDao.instance.getEosTic()!, amount: NSDecimalNumber.zero)
            }
            
        } else {
            self.setAmount(indexPath, token: token)
        }

         return cell!
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 80;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let token = tokens[indexPath.row]
        if(token.token_userAmount != NSDecimalNumber.notANumber) {
            let walletDetailVC = WalletDetailViewController(nibName: "WalletDetailViewController", bundle: nil)
            walletDetailVC.hidesBottomBarWhenPushed = true
            walletDetailVC.token = token
            walletDetailVC.user = self.user
            self.navigationItem.title = ""
            self.navigationController?.pushViewController(walletDetailVC, animated: true)
        }
    }
    
    func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
        self.scrolling = true;
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        self.doReloadAfterScroll()
    }
    
    func doReloadAfterScroll() {
        self.scrolling = false
        
        self.tokensTableView.beginUpdates()
        self.tokensTableView.reloadRows(at: self.toUpdateRow, with: .none)
        self.tokensTableView.endUpdates()
        
        self.toUpdateRow.removeAll()
        
    }
    
    
    func reqEosTic() {
        let request = Alamofire .request(URL_EOS_TIC+"1765",
                                         method: .get,
                                         parameters: ["convert":BaseDao.instance.getUserCurrencyS()],
                                         encoding: URLEncoding.default,
                                         headers: [:]);
        request.responseJSON { (response) in
            switch response.result {
            case .success(let res):
                BaseDao.instance.setEosTic(res as! NSDictionary)
                self.eosTic = res as? NSDictionary
                self.reqUserInfo(userId: self.user!.user_account)

            case .failure(let error):
                self.refresher.endRefreshing()
                print(error)
            }
        }
    }
    
    func reqUserInfo(userId: String) {
        let parameters: Parameters = ["account_name": userId]
        let request = Alamofire.request(URL_PATH_BP_GET_ACCOUNT,
                                        method: .post,
                                        parameters: parameters,
                                        encoding: JSONEncoding.default)
        request.responseJSON { (response) in
            switch response.result {
            case .success(let res):
                BaseDao.instance.setLastCheckTime(NSDate())
                BaseDao.instance.setUserInfo(res as! NSDictionary)
                self.tokensTableView.reloadData()
                self.refresher.endRefreshing()
                self.hideWaittingAlert()
                
            case .failure(let error):
                self.refresher.endRefreshing()
                print(error)
            }
        }
    }
    
    
    
    func setAmount(_ indexPath: IndexPath, token: Token) {
        let parameters: Parameters = ["code": token.token_contractAddr, "account": self.user?.user_account, "symbol": token.token_symbol]
        let request = Alamofire.request(URL_PATH_BP_CHECK_BALANCE,
                                        method: .post,
                                        parameters: parameters,
                                        encoding: JSONEncoding.default)
        request.responseJSON { (response) in
            if let result = response.result.value {
                let arrayResult = result as! Array<String>
                var value = NSDecimalNumber.zero
                if(arrayResult.count > 0) {
                    value = NSDecimalNumber.init(string: arrayResult[0].replacingOccurrences(of: token.token_symbol, with: "").replacingOccurrences(of: " ", with: ""))
                }
                self.tokens[indexPath.row].token_userAmount = value
                
                let cell = self.tokensTableView.cellForRow(at: indexPath) as? TokensViewCell
                cell?.amountLabel.attributedText = AppUtils.displayAmout(value, font: (cell?.amountLabel.font)!, symbol: "", deciaml: token.token_decimals)
                cell?.amountLabel.setNeedsDisplay()
                
                if(token.token_contractAddr == "eosio.token" && token.token_userAmount.compare(NSDecimalNumber.zero) == .orderedDescending) {
                    cell?.valueLabel.text = AppUtils.getDisplayCash(BaseDao.instance.getUserCurrencyS(), tic: BaseDao.instance.getEosTic()!, amount: token.token_userAmount)
                } else {
                    cell?.valueLabel.text = AppUtils.getDisplayCash(BaseDao.instance.getUserCurrencyS(), tic: BaseDao.instance.getEosTic()!, amount: NSDecimalNumber.zero)
                }
                cell?.valueLabel.setNeedsDisplay()
            }
        }

    }

}
