//
//  KeyboardViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 28..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class KeyboardViewController: UIPageViewController, UIPageViewControllerDelegate, UIPageViewControllerDataSource {

    
    lazy var orderedViewControllers: [UIViewController] = {
        return [self.newVc(viewController: "DecimalKeyboard"),
                self.newVc(viewController: "AlpahabetKeyboard")]
    }()
    

    override func viewDidLoad() {
        super.viewDidLoad()

        self.dataSource = self
        if let firstViewController = orderedViewControllers.first {
            setViewControllers([firstViewController], direction: .forward, animated: true, completion: nil)
        }
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(self.setViewControllerForce(_:)),
                                               name: Notification.Name("KeyBoardPage"),
                                               object: nil)
        
    }

    func newVc(viewController: String) ->UIViewController {
        return UIStoryboard(name: "Password", bundle: nil).instantiateViewController(withIdentifier: viewController)
    }
    
    @objc func setViewControllerForce(_ notification: NSNotification) {
        if let page = notification.userInfo?["Page"] as? Int {
            if(page == 0) {
                if let firstViewController = orderedViewControllers.first {
                    setViewControllers([firstViewController], direction: .reverse, animated: true, completion: nil)
                }
            } else {
                if let secondController = orderedViewControllers.last {
                    setViewControllers([secondController], direction: .forward, animated: true, completion: nil)
                }
            }
        }
    }
    
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController? {
        return nil
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {
        return nil
    }
}
