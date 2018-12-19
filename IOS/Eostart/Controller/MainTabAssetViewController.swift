//
//  MainTabAssetViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 3..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import Alamofire
import DropDown

class MainTabAssetViewController: BaseViewController {

    var refresher: UIRefreshControl!
    var userData: NSDictionary!
    var eosTic: NSDictionary!
    
    @IBOutlet weak var titleLayer: UIView!
    @IBOutlet weak var titleAccountLabel: UILabel!
    @IBOutlet weak var titleView: UIView!
    
    
    @IBOutlet weak var mainScrollView: UIScrollView!
    @IBOutlet weak var totalEosLabel: UILabel!
    @IBOutlet weak var totalCashLabel: UILabel!
    @IBOutlet weak var unstakedAmountLabel: UILabel!
    @IBOutlet weak var stakedAmountLabel: UILabel!
    @IBOutlet weak var refundAmountLabel: UILabel!
    
    @IBOutlet weak var ramPregressView: LinearProgressView!
    @IBOutlet weak var cpuProgressView: LinearProgressView!
    @IBOutlet weak var netProgressView: LinearProgressView!
    @IBOutlet weak var ramDescriptionLabel: UILabel!
    @IBOutlet weak var cpuDescriptionLabel: UILabel!
    @IBOutlet weak var cpuValueLabel: UILabel!
    @IBOutlet weak var netDescriptionLabel: UILabel!
    @IBOutlet weak var netValueLabel: UILabel!
    
    @IBOutlet weak var EosRateLabel: UILabel!
    
    var dimView: UIView?
    let window = UIApplication.shared.keyWindow!
    let dropDown = DropDown()
    var user: User?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        refresher = UIRefreshControl()
        refresher.addTarget(self, action: #selector(handleRefresh), for: .valueChanged)
        refresher.tintColor = UIColor.white
        mainScrollView.addSubview(refresher)

        dimView = UIView(frame: window.bounds)
        dimView!.backgroundColor = UIColor.black
        dimView!.alpha  = 0.6
        
//        if let viewControllers = navigationController?.viewControllers {
//            print("stack " , viewControllers.count)
//        }

        
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
                self.userData = BaseDao.instance.getUserInfo()
                self.onUpdateView()
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
        dropDown.dismissMode = .onTap
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
    
        
        
        dropDown.willShowAction = { [unowned self] in
            self.window.addSubview(self.dimView!);
        }
        
        dropDown.cancelAction = { [unowned self] in
            self.dimView?.removeFromSuperview()
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
    
    func onUpdateView() {
        self.hideWaittingAlert()
        
        self.totalEosLabel.attributedText = AppUtils.displayAmout(AppUtils.getUserTotalAmount(self.userData), font: totalEosLabel.font, symbol: "EOS", deciaml: 4)
        self.totalCashLabel.text = AppUtils.getDisplayTotal(BaseDao.instance.getUserCurrencyS(), tic: BaseDao.instance.getEosTic()!, input: self.userData)
        
        self.unstakedAmountLabel.attributedText = AppUtils.displayAmout(AppUtils.getUserUnstakedAmount(self.userData), font: unstakedAmountLabel.font, symbol: "EOS", deciaml: 4)
        self.stakedAmountLabel.attributedText = AppUtils.displayAmout(AppUtils.getUserStakedAmount(self.userData), font: stakedAmountLabel.font, symbol: "EOS", deciaml: 4)
        self.refundAmountLabel.attributedText = AppUtils.displayAmout(AppUtils.getUserRefundAmount(self.userData), font: refundAmountLabel.font, symbol: "EOS", deciaml: 4)
        
        self.ramPregressView.setProgress(Float(AppUtils.getUserRamProgress(self.userData)), animated: true)
        self.ramDescriptionLabel.text = AppUtils.getUserRamInfo(self.userData)
        
        self.cpuProgressView.setProgress(Float(AppUtils.getUserCpuProgress(self.userData)), animated: true)
        self.cpuDescriptionLabel.text = AppUtils.getUserCpuInfo(self.userData)
        self.cpuValueLabel.attributedText = AppUtils.displayAmout(AppUtils.getUserCpuAmount(self.userData), font: cpuValueLabel.font, symbol: "EOS", deciaml: 4)
        
        self.netProgressView.setProgress(Float(AppUtils.getUserNetProgress(self.userData)), animated: true)
        self.netDescriptionLabel.text = AppUtils.getUserNetInfo(self.userData)
        self.netValueLabel.attributedText = AppUtils.displayAmout(AppUtils.getUserNetAmount(self.userData), font: netValueLabel.font, symbol: "EOS", deciaml: 4)
        
        self.EosRateLabel.text = "1 EOS = " + AppUtils.getDisplayEosRate(BaseDao.instance.getUserCurrencyS(), tic: BaseDao.instance.getEosTic()!)
    }
    
    
    @objc func handleRefresh() {
        self.reqEosTic()
    }

   
    @objc func titleAccountClick() {
        dropDown.show()
    }
    
    func reqEosTic() {
        let request = Alamofire
            .request(URL_EOS_TIC+"1765",
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
                self.userData = res as? NSDictionary
                self.refresher.endRefreshing()
                self.onUpdateView()
                
            case .failure(let error):
                self.refresher.endRefreshing()
                print(error)
            }
        }
    }
}
    
    
    
    
    
    
    
    
    
    
    

