//
//  WalletDetailViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 10..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage
import Toaster

class WalletDetailViewController: BaseViewController, UITableViewDelegate, UITableViewDataSource {

    let PageCnt:Int = 30;
    let PageInit:Int = 1;
    
    
    @IBOutlet weak var mainTableView: UITableView!
    @IBOutlet weak var sendBtn: BottomLeftButton!
    @IBOutlet weak var receiveBtn: BottomRightButton!
    @IBOutlet weak var noneHistoryView: UIView!
    
    @IBAction func sendClick(_ sender: BottomLeftButton) {
        if(BaseDao.instance.hasKeyUser(account: (user?.user_account)!)) {
            let sendVC = SendViewController(nibName: "SendViewController", bundle: nil)
            sendVC.token = token
            sendVC.user = self.user
            sendVC.recentList = self.getRecentList()
            self.navigationItem.title = ""
            self.navigationController?.pushViewController(sendVC, animated: true)
            
            
        } else {
            let alertController = UIAlertController(title: "add_key".localized(), message: "key_add_des".localized(), preferredStyle: UIAlertControllerStyle.alert)
            let cancelAction = UIAlertAction(title: "cancel".localized(), style: UIAlertActionStyle.default) { (result : UIAlertAction) -> Void in
            }
            let okAction = UIAlertAction(title: "add_key2".localized(), style: UIAlertActionStyle.destructive) { (result : UIAlertAction) -> Void in
                let addKeyController = UIStoryboard(name: "Init", bundle: nil).instantiateViewController(withIdentifier: "AddKeyViewController") as! AddKeyViewController
                addKeyController.userAccount = self.user?.user_account
                self.navigationItem.title = ""
                self.navigationController?.pushViewController(addKeyController, animated: true)
            }
            alertController.addAction(cancelAction)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
        
        
    }
    
    @IBAction func receiveClick(_ sender: BottomRightButton) {
    }
    
    
    
    var token: Token?
    var user: User?
    var transactions = Array<NSDictionary>()
    lazy var cPage = PageInit
    var totalSize: Int = 0
    var hasMore = false
    var loading = false

    override func viewDidLoad() {
        super.viewDidLoad()
        
        if(token?.token_userAmount != NSDecimalNumber.notANumber && token?.token_userAmount.compare(NSDecimalNumber.zero) == .orderedDescending) {
            self.sendBtn.isEnabled = true;
        } else {
            self.sendBtn.isEnabled = false;
        }
        self.receiveBtn.isEnabled = true;
        
        
        self.mainTableView.delegate = self
        self.mainTableView.dataSource = self
        self.mainTableView.separatorStyle = UITableViewCellSeparatorStyle.none
        self.mainTableView.register(UINib(nibName: "TransactionViewCell", bundle: nil), forCellReuseIdentifier: "TransactionViewCell")
        
        reqTransaction()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        self.navigationController?.setNavigationBarHidden(false, animated: animated)
        self.navigationItem.title = token?.token_symbol
        
        self.noneHistoryView.isHidden = true
        
        if(token?.token_userAmount.compare(NSDecimalNumber.zero) == .orderedDescending) {
            self.sendBtn.isEnabled = true
        } else {
            self.sendBtn.isEnabled = false
        }
    }
    
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if(section == 0) {
            let view = WalletHeader1(frame: CGRect(x: 0, y: 0, width: 0, height: 0))
            Alamofire.request(token!.token_iconUrl, method: .get).responseImage { response  in
                guard let image = response.result.value else {
                    view.symbolImg.image = nil
                    return
                }
                view.symbolImg.image = image
            }
            view.amountLabel.attributedText = AppUtils.displayAmout(token!.token_userAmount, font: view.amountLabel.font, symbol: token!.token_symbol, deciaml: token!.token_decimals)
            if(token!.token_contractAddr == "eosio.token" && token!.token_userAmount.compare(NSDecimalNumber.zero) == .orderedDescending) {
                view.valueLabel.text = AppUtils.getDisplayCash(BaseDao.instance.getUserCurrencyS(), tic: BaseDao.instance.getEosTic()!, amount: token!.token_userAmount)
            } else {
                view.valueLabel.text = AppUtils.getDisplayCash(BaseDao.instance.getUserCurrencyS(), tic: BaseDao.instance.getEosTic()!, amount: NSDecimalNumber.zero)
            }
            return view
            
        } else {
            let view = WalletHeader2(frame: CGRect(x: 0, y: 0, width: 0, height: 0))
            view.countLabel.text = String(totalSize)
            return view
        }
        
        
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if(section == 0) {
            return 120
        } else  {
            return 40
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 180;
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(section == 0) {
            return 0
        } else {
            return transactions.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let tx = transactions[indexPath.row]
        let cell:TransactionViewCell? = tableView.dequeueReusableCell(withIdentifier:"TransactionViewCell") as? TransactionViewCell
        
        cell?.countLabel.text = String(totalSize - indexPath.row) + " / " + String(totalSize)
        cell?.dateLabel.text = AppUtils.changeTimeFormat(input: tx.object(forKey: "timestamp") as! String)
        cell?.txidLabel.text = tx.object(forKey: "trx_id") as? String
        cell?.memoLabel.text = tx.object(forKey: "memo") as? String
        
        let sender = tx.object(forKey: "sender") as? String
        if(sender == user?.user_account) {
            cell?.otherUserLabel.text = tx.object(forKey: "receiver") as? String
            cell?.typeLabel.text = "Sent"
            cell?.amountLabel.textColor = UIColor(hexString: "#FF5656")
            cell?.amountLabel.text = "- " + (tx.object(forKey: "quantity") as! String) + " " + (tx.object(forKey: "symbol") as! String)
        } else {
            cell?.otherUserLabel.text = tx.object(forKey: "sender") as? String
            cell?.typeLabel.text = "Received"
            cell?.amountLabel.textColor = UIColor(hexString: "#00CF1C")
            cell?.amountLabel.text = tx.object(forKey: "quantity") as! String + " " + (tx.object(forKey: "symbol") as! String)
        }
        return cell!
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        let lastItem = self.transactions.count - 1
        if(indexPath.section == 1 && indexPath.row == lastItem && self.hasMore && !self.loading) {
            print("hitBottom")
            cPage = cPage + 1;
            self.reqTransaction()
        }
//        print("lastItem ", lastItem ,"indexPath.section ", indexPath.section , "  " , "indexPath.row", indexPath.row, "  " , "hasMore", hasMore, "   loading", loading)
    }
    
    
    
    func reqTransaction() {
        self.loading = true
        self.hasMore = false
        let parameters: Parameters = ["module": "account", "action": "get_account_related_trx_info",
                                      "apikey": EOS_PARK_API_KEY, "account": user!.user_account,
                                      "page": cPage, "size": PageCnt,
                                      "symbol": token!.token_symbol, "code": token!.token_contractAddr]
        
        let request = Alamofire.request(URL_PATH_PARK_ACTIONS2,
                                        method: .get,
                                        parameters: parameters,
                                        encoding: URLEncoding.default)
        
        request.responseJSON { (response) in
            if let status = response.response?.statusCode {
                switch(status){
                case 200:
                    break
                    
                default:
                    Toast(text: "error_network".localized(), duration: Delay.short).show()
                    return
                }
                
                if let result = (response.result.value as? NSDictionary)?.object(forKey: "data") as?  NSDictionary {
                    self.totalSize = result.object(forKey: "trace_count") as? Int ?? 0
                    UIView.performWithoutAnimation {
                        self.mainTableView.beginUpdates()
                        self.mainTableView.reloadSections(IndexSet(1..<2), with: UITableViewRowAnimation.none)
                        self.mainTableView.endUpdates()
                    }

                    let txs = result.object(forKey: "trace_list") as? Array<NSDictionary>
                    if (self.cPage == self.PageInit) {
                        self.transactions.removeAll()
                    }
                    self.transactions.append(contentsOf: txs!)

                    if(self.totalSize > self.transactions.count && txs?.count == self.PageCnt) {
                        self.hasMore = true
                    }

                    if (self.transactions.count > 0) {
                        self.mainTableView.reloadData()

                    } else {
                        self.noneHistoryView.isHidden = false
                    }



                } else {
                    self.hasMore = false
                }

                self.loading = false
            }
        }
    }
    
    func getRecentList() -> Array<String> {
        var result = Array<String>()
        for tx in self.transactions {
            let sender = tx.object(forKey: "sender") as? String
            let receiver = tx.object(forKey: "receiver") as? String
            if(sender == user?.user_account) {
                if(!result.contains(receiver!)) {
                    result.append(receiver!)
                }
            } else {
                if(!result.contains(sender!)) {
                    result.append(sender!)
                }
            }
            if(result.count > 4) {
                break
            }

        }
        
        return result
    }

}
