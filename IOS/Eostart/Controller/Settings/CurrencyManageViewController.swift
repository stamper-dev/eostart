//
//  CurrencyManageViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 18..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class CurrencyManageViewController: BaseViewController , UITableViewDelegate, UITableViewDataSource {

    

    @IBOutlet weak var mainTableview: UITableView!
    
    var currency = ["currency_krw".localized(),
                    "currency_usd".localized(),
                    "currency_btc".localized()]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.mainTableview.delegate = self
        self.mainTableview.dataSource = self
        self.mainTableview.separatorStyle = UITableViewCellSeparatorStyle.none
        self.mainTableview.register(UINib(nibName: "CurrencyViewCell", bundle: nil), forCellReuseIdentifier: "CurrencyViewCell")
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.setNavigationBarHidden(false, animated: animated)
        navigationItem.title = "set_currency".localized()
    }
    

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return currency.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell:CurrencyViewCell? = tableView.dequeueReusableCell(withIdentifier:"CurrencyViewCell") as? CurrencyViewCell
        cell?.currencyLabel.text = currency[indexPath.row]
        
        if(indexPath.row == 0) {
            cell?.currencyImage.image = UIImage(named: "krw_ic")
        } else if (indexPath.row == 1) {
            cell?.currencyImage.image = UIImage(named: "usd_ic")
        } else {
            cell?.currencyImage.image = UIImage(named: "btc_ic")
        }
        
    
        if(BaseDao.instance.getUserCurrency() - 1 == indexPath.row) {
            cell?.currencyChecked.image = UIImage(named: "checkic_on")
        } else {
            cell?.currencyChecked.image = UIImage(named: "checkic_off")
        }
            
        
        if(indexPath.row == currency.count - 1) {
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
            BaseDao.instance.setUserCurrency(1)
            
        } else if (indexPath.row == 1) {
            BaseDao.instance.setUserCurrency(2)
            
        } else if (indexPath.row == 2) {
            BaseDao.instance.setUserCurrency(3)
            
        }
        let old = NSDate().addingTimeInterval(-86400.0)
        BaseDao.instance.setLastCheckTime(old)
        self.mainTableview.reloadData()
    }

}
