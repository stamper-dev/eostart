//
//  CreateAccountViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 17..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import SafariServices

class CreateAccountViewController: BaseViewController {

    @IBOutlet weak var gifImage: UIImageView!
    @IBOutlet weak var addAccountBtn: BottomLeftButton!
    @IBOutlet weak var createAccountBtn: BottomRightButton!
    @IBAction func addAccountClicked(_ sender: UIButton) {
        let addAccountVc = UIStoryboard(name: "Init", bundle: nil).instantiateViewController(withIdentifier: "AddAccountViewController") as! AddAccountViewController
        self.navigationItem.title = ""
        self.navigationController?.pushViewController(addAccountVc, animated: true)
    }
    @IBAction func createAccountClicked(_ sender: UIButton) {
        guard let url = URL(string: "https://eostart.com") else { return }
        let safariViewController = SFSafariViewController(url: url)
        present(safariViewController, animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        gifImage.image = UIImage.gifImageWithName("logo_eos")
        
        addAccountBtn.isEnabled = true
        createAccountBtn.isEnabled = true
        
//        let statusBar = UIApplication.shared.value(forKeyPath: "statusBarWindow.statusBar") as! UIView
//        statusBar.backgroundColor = UIColor(hexString: "#3F4863")
//        UIApplication.shared.statusBarStyle = .lightContent
    }

}
