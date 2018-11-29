//
//  WelcomeViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 17..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class WelcomeViewController: UIViewController, WelcomePageViewControllerDelegate{

    
    
    @IBOutlet weak var pageControl: UIPageControl!
    @IBOutlet weak var beforeBtn: UIButton!
    @IBOutlet weak var nextBtn: UIButton!
    @IBAction func beforeBtnClick(_ sender: UIButton) {
        self.beforeBtn.isEnabled = false
        self.nextBtn.isEnabled = false
        
        if(pageControl.currentPage == 0) {
            self.onStartCreate()
            
        } else if (pageControl.currentPage == 1) {
            welcomePageViewController?.scrollToViewController(index: 0)
            
        } else if (pageControl.currentPage == 2) {
            welcomePageViewController?.scrollToViewController(index: 1)
            
        }
        
    }
    @IBAction func nextBtnClick(_ sender: UIButton) {
        self.beforeBtn.isEnabled = false
        self.nextBtn.isEnabled = false
        if(pageControl.currentPage == 0) {
            welcomePageViewController?.scrollToViewController(index: 1)

        } else if (pageControl.currentPage == 1) {
            welcomePageViewController?.scrollToViewController(index: 2)

        } else if (pageControl.currentPage == 2) {
            self.onStartCreate()

        }
    }
    
    var welcomePageViewController: WelcomePageViewController? {
        didSet {
            welcomePageViewController?.welcomeDelegate = self
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        beforeBtn.setTitle("skip".localized(), for: .normal)
        nextBtn.setTitle("next".localized(), for: .normal)
        
        let statusBar = UIApplication.shared.value(forKeyPath: "statusBarWindow.statusBar") as! UIView
        statusBar.backgroundColor = UIColor(hexString: "#FFFFFF")
        UIApplication.shared.statusBarStyle = .default

    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let welcomePageViewController = segue.destination as? WelcomePageViewController {
            self.welcomePageViewController = welcomePageViewController
        }
    }
    
    
    
    func welcomePageViewController(_ welcomePageViewController: WelcomePageViewController, didUpdatePageCount count: Int) {
        self.pageControl.numberOfPages = count
    }
    
    func welcomePageViewController(_ welcomePageViewController: WelcomePageViewController, didUpdatePageIndex index: Int) {
        print("didUpdatePageIndex ", index)
        if(index == 0) {
            beforeBtn.setTitle("skip".localized(), for: .normal)
            nextBtn.setTitle("next".localized(), for: .normal)
            
        } else if (index == 1) {
            beforeBtn.setTitle("back".localized(), for: .normal)
            nextBtn.setTitle("next".localized(), for: .normal)
            
        } else if (index == 2) {
            beforeBtn.setTitle("back".localized(), for: .normal)
            nextBtn.setTitle("start".localized(), for: .normal)
            
        }
        self.beforeBtn.isEnabled = true
        self.nextBtn.isEnabled = true
        self.pageControl.currentPage = index
    }
    
    
    func onStartCreate() {
        let createVc : UIViewController = UIStoryboard(name: "Init", bundle: nil).instantiateViewController(withIdentifier: "CreateAccount")
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        appDelegate.window?.rootViewController = createVc
        self.present(createVc, animated: true, completion: nil)
    }
}
