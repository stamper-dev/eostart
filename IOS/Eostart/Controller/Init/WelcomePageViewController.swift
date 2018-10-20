//
//  WelcomePageViewController.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 17..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import UIKit

class WelcomePageViewController: UIPageViewController, UIPageViewControllerDataSource, UIPageViewControllerDelegate {

    weak var welcomeDelegate: WelcomePageViewControllerDelegate?

    lazy var orderedViewControllers: [UIViewController] = {
        return [self.newVc(viewController: "welcome0VC"),
                self.newVc(viewController: "welcome1VC"),
                self.newVc(viewController: "welcome2VC")]
    }()
    
    func newVc(viewController: String) -> UIViewController {
        return UIStoryboard(name: "Init", bundle: nil).instantiateViewController(withIdentifier: viewController)
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.dataSource = self
        self.delegate = self
        if let firstViewController = orderedViewControllers.first {
            setViewControllers([firstViewController], direction: .forward, animated: true, completion: nil)
        }
        
        welcomeDelegate?.welcomePageViewController(self, didUpdatePageCount: orderedViewControllers.count)
    }
    
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController? {
        
        guard let viewControllerIndex = orderedViewControllers.index(of: viewController) else {
            return nil
        }

        let previousIndex = viewControllerIndex - 1
        
        guard previousIndex >= 0 else {
            return nil
        }


        guard previousIndex >= 0 else {
            return nil
        }
        
        return orderedViewControllers[previousIndex]
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {
        guard let viewControllerIndex = orderedViewControllers.index(of: viewController) else {
            return nil
        }
        
        let nextIndex = viewControllerIndex + 1
        
        guard orderedViewControllers.count != nextIndex else {
            return nil
        }
        
        guard orderedViewControllers.count > nextIndex else {
            return nil
        }
        
        return orderedViewControllers[nextIndex]

    }
    
    func pageViewController(_ pageViewController: UIPageViewController, didFinishAnimating finished: Bool, previousViewControllers: [UIViewController], transitionCompleted completed: Bool) {
        if let firstViewController = viewControllers?.first,
            let index = orderedViewControllers.firstIndex(of: firstViewController) {
            self.welcomeDelegate?.welcomePageViewController(self, didUpdatePageIndex: index)
        }

    }
    
    func scrollToViewController(index newIndex: Int) {
        if let firstViewController = viewControllers?.first,
            let currentIndex = orderedViewControllers.firstIndex(of: firstViewController) {
            let direction: UIPageViewController.NavigationDirection = newIndex >= currentIndex ? .forward : .reverse
            let nextViewController = orderedViewControllers[newIndex]
            scrollToViewController(viewController: nextViewController, direction: direction)
        }
    }
    
    private func scrollToViewController(viewController: UIViewController, direction: UIPageViewController.NavigationDirection = .forward) {
        setViewControllers([viewController],
                           direction: direction,
                           animated: true,
                           completion: { (finished) -> Void in
                            self.notifyTutorialDelegateOfNewIndex()
        })
    }
    
    private func notifyTutorialDelegateOfNewIndex() {
        if let firstViewController = viewControllers?.first,
            let index = orderedViewControllers.firstIndex(of: firstViewController) {
            welcomeDelegate?.welcomePageViewController(self, didUpdatePageIndex: index)
        }
    }
}

protocol WelcomePageViewControllerDelegate: class {
    
    func welcomePageViewController(_ welcomePageViewController: WelcomePageViewController, didUpdatePageCount count: Int)
    
    func welcomePageViewController(_ welcomePageViewController: WelcomePageViewController, didUpdatePageIndex index: Int)
    
}
