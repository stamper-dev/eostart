//
//  KeyCheckViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 10..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit
import Toaster

class KeyCheckViewController: BaseViewController {

    @IBOutlet weak var warnningLable: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    @IBOutlet weak var keyLabel: UILabel!
    @IBOutlet weak var copyBtn: UIButton!
    @IBOutlet weak var doneBtn: BottomButton!
    @IBAction func copyClick(_ sender: UIButton) {
        UIPasteboard.general.string = key
        Toast(text: "key_copied".localized(), duration: Delay.short).show()
        
    }
    @IBAction func doneClick(_ sender: BottomButton) {
        let mainTabController : UIViewController = UIStoryboard(name: "MainTabStoryboard", bundle: nil).instantiateViewController(withIdentifier: "MainTabViewController")
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        appDelegate.window?.rootViewController = mainTabController
        self.present(mainTabController, animated: true, completion: nil)
    }
    
    var key:String = "";
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        warnningLable.text = "key_check_des1".localized()
        descriptionLabel.text = "key_check_des2".localized()
        keyLabel.text = key
        doneBtn.isEnabled = true
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.setNavigationBarHidden(false, animated: animated)
        self.navigationItem.title = "title_check_key".localized()
    }

}
