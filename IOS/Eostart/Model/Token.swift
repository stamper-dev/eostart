//
//  Token.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 2..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import Foundation

public class Token {
    
    var token_id: Int64 = -1
    var token_name: String = ""
    var token_symbol: String = ""
    var token_iconUrl: String = ""
    var token_contractAddr: String = ""
    var token_decimals: Int = 4
    var token_userAmount: NSDecimalNumber = NSDecimalNumber.notANumber
    
    init() {
        
    }
    
    init(id: Int64, name:String, symbol:String, iconUrl:String, contractAdd:String, decimal:Int) {
        self.token_id = id;
        self.token_name = name;
        self.token_symbol = symbol;
        self.token_iconUrl = iconUrl;
        self.token_contractAddr = contractAdd;
        self.token_decimals = decimal;
    }
    
    init(_ dictionary: [String: Any]) {
        self.token_id = dictionary["id"] as? Int64 ?? -1
        self.token_name = dictionary["name"] as? String ?? ""
        self.token_symbol = dictionary["symbol"] as? String ?? ""
        self.token_iconUrl = dictionary["iconUrl"] as? String ?? ""
        self.token_contractAddr = dictionary["contractAddr"] as? String ?? ""
        self.token_decimals = Int(dictionary["decimals"] as? String ?? "4") ?? 4
    }
    
}
