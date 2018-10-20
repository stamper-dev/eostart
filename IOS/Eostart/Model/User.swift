//
//  User.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 3..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import Foundation

public class User {
    
    var user_id: Int64 = -1
    var user_account: String = ""
    var user_info: String = ""
    
    init(account: String, info: String ) {
        self.user_account = account;
        self.user_info = info
    }
    
    init(id: Int64, account: String, info: String ) {
        self.user_id = id;
        self.user_account = account;
        self.user_info = info
    }
    
    init(_ dictionary: [String: Any]) {
        self.user_id = dictionary["id"] as? Int64 ?? -1
        self.user_account = dictionary["account"] as? String ?? ""
        self.user_info = dictionary["info"] as? String ?? ""
    }
}
