//
//  BaseDao.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 2..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import Foundation
import SQLite

final class BaseDao : NSObject{
    
    static let instance = BaseDao()
    
    var database: Connection!
    
    public override init() {
        super.init();
        if database == nil {
            self.initdb();
        }
    }
    
    func setUserInfo(_ userinfo :NSDictionary) {
        let encodedData = NSKeyedArchiver.archivedData(withRootObject: userinfo)
        UserDefaults.standard.setValue(encodedData, forKey: KEY_USER_INFO)
    }
    
    func getUserInfo() -> NSDictionary? {
        if let value = UserDefaults.standard.object(forKey: KEY_USER_INFO) {
            let decoded = value as! Data
            return NSKeyedUnarchiver.unarchiveObject(with: decoded) as? NSDictionary
        }
        return nil
    }
    
    func setEosTic(_ tic :NSDictionary) {
        let encodedData = NSKeyedArchiver.archivedData(withRootObject: tic)
        UserDefaults.standard.setValue(encodedData, forKey: KEY_LAST_EOS_TIC)
    }
    
    func getEosTic() -> NSDictionary? {
        if let value = UserDefaults.standard.object(forKey: KEY_LAST_EOS_TIC) {
            let decoded = value as! Data
            return NSKeyedUnarchiver.unarchiveObject(with: decoded) as? NSDictionary
        }
        return nil
    }
    
    func setLastCheckTime(_ date :NSDate) {
        UserDefaults.standard.set(date, forKey: KEY_LAST_CHECK_TIME)
    }
    
    func getLastCheckTime() -> NSDate? {
        return UserDefaults.standard.object(forKey: KEY_LAST_CHECK_TIME) as? NSDate
    }
    
    func getUserLangauage() -> Int {
        return UserDefaults.standard.integer(forKey: KEY_SET_LANGUAGE)
    }
    
    func getUserLangauageS() ->String {
        if(getUserLangauage() == 1) {
            return "ko"
        } else if (getUserLangauage() == 2) {
            return "en"
        } else {
            if(Locale.current.languageCode == "ko") {
                return "ko"
            }
            return ""
        }
    }
    
    func setUserLangauage(_ lang : Int) {
        UserDefaults.standard.set(lang, forKey: KEY_SET_LANGUAGE)
    }
    
    func getUserCurrency() -> Int {
        if(UserDefaults.standard.integer(forKey: KEY_SET_CURRENCY) != 0) {
            return UserDefaults.standard.integer(forKey: KEY_SET_CURRENCY)
        } else{
            if(Locale.current.languageCode == "ko") {
                return 1
            } else {
                return 2
            }
        }
    }
    
    func getUserCurrencyS() ->String {
        if(getUserCurrency() == 1) {
            return "KRW"
        } else if (getUserCurrency() == 2) {
            return "USD"
        } else {
            return "BTC"
        }
    }
    
    func setUserCurrency(_ lang : Int) {
        UserDefaults.standard.set(lang, forKey: KEY_SET_CURRENCY)
    }
    
    func setRecentAccountId(_ id : Int64) {
        UserDefaults.standard.set(id, forKey: KEY_RECENT_ACCOUNT)
    }
    
    func getRecentAccountId() -> Int64 {
        return Int64(UserDefaults.standard.integer(forKey: KEY_RECENT_ACCOUNT))
    }

    
    func getUsingAppLock() -> Bool {
        return UserDefaults.standard.bool(forKey: KEY_SET_APP_LOCK)
    }
    
    func setUsingAppLock(_ use:Bool) {
        UserDefaults.standard.set(use, forKey: KEY_SET_APP_LOCK)
    }
    
    func getUsingAppLockKey() -> String? {
        return UserDefaults.standard.string(forKey: KEY_SET_APP_LOCKKEY)
    }
    
    func setUsingAppLockKey(_ key:String) {
        UserDefaults.standard.set(key, forKey: KEY_SET_APP_LOCKKEY)
    }
    
    func getUsingBioLock() -> Bool {
        UserDefaults.standard.register(defaults: [KEY_SET_BIO_AUTH : true])
        return UserDefaults.standard.bool(forKey: KEY_SET_BIO_AUTH)
    }
    
    func setUsingBioLock(_ use:Bool) {
        UserDefaults.standard.set(use, forKey: KEY_SET_BIO_AUTH)
    }
    
    
    
    func initdb() {
        do {
            let documentDirectory = try FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
            let fileUrl = documentDirectory.appendingPathComponent("wannabit").appendingPathExtension("sqlite3")
            let database = try Connection(fileUrl.path)
            self.database = database
            
            let createUserTable = DB_Users.create { (table) in
                table.column(DB_User_Id, primaryKey: true)
                table.column(DB_User_Name)
                table.column(DB_User_Userinfo)
            }
            
            let createTokensTable = DB_Tokens.create { (table) in
                table.column(DB_Token_Id, primaryKey: true)
                table.column(DB_Token_Name)
                table.column(DB_Token_Symbol)
                table.column(DB_Token_IconUrl)
                table.column(DB_Token_ContractAddr)
                table.column(DB_Token_Decimals)
            }
            
            do {
                try self.database.run(createUserTable)

                try self.database.run(createTokensTable)

            } catch {
                print(error)
            }
            
            
        } catch {
            print(error)
        }
    }
    
    
    public func selectAllTokens() -> Array<Token> {
        var result = Array<Token>()
        do {
            for dtoken in try database.prepare(DB_Tokens) {
                let token = Token(id: dtoken[DB_Token_Id], name: dtoken[DB_Token_Name], symbol: dtoken[DB_Token_Symbol], iconUrl: dtoken[DB_Token_IconUrl], contractAdd: dtoken[DB_Token_ContractAddr], decimal: Int(dtoken[DB_Token_Decimals]));
                result.append(token);
            }
        } catch {
            print(error)
        }
        return result;
    }
    
    public func deleteAllToken() {
        do {
            try database.run(DB_Tokens.delete())
        } catch {
           print(error)
        }
    }
    
    public func insertToken(newToken: Token) {
        let insertToken = DB_Tokens.insert(DB_Token_Name <- newToken.token_name,
                                        DB_Token_Symbol <- newToken.token_symbol,
                                        DB_Token_IconUrl <- newToken.token_iconUrl,
                                        DB_Token_ContractAddr <- newToken.token_contractAddr,
                                        DB_Token_Decimals <- Int64(newToken.token_decimals))
        do {
            try database.run(insertToken)
        } catch {
            print(error)
        }
    }
    
    public func insertTokens(newTokens: Array<Token>) {
        for newToken in newTokens {
            self.insertToken(newToken: newToken)
        }
    }
    
    
    
    
    
    
    public func selectAllUsers() -> Array<User> {
        var result = Array<User>()
        do {
            for duser in try database.prepare(DB_Users) {
                let user = User(id: duser[DB_User_Id], account: duser[DB_User_Name], info: duser[DB_User_Userinfo]!)
                result.append(user)
            }
        } catch {
            print(error)
        }
        return result;
    }
    
    public func selectUserById(id: Int64) -> User? {
        do {
            let query = DB_Users.filter(DB_User_Id == id)
            if let duser = try database.pluck(query) {
                return User(id: duser[DB_User_Id], account: duser[DB_User_Name], info: duser[DB_User_Userinfo]!)
            }
            return nil
        } catch {
            print(error)
        }
        return nil
    }
    
    public func selectUserByAccount(account: String) -> User? {
        do {
            let query = DB_Users.filter(DB_User_Name == account)
            if let duser = try database.pluck(query) {
                return User(id: duser[DB_User_Id], account: duser[DB_User_Name], info: duser[DB_User_Userinfo]!)
            }
            return nil
        } catch {
            print(error)
        }
        return nil
    }
    
    
    
    
    public func insertUser(user: User) -> Int64 {
        let insertUser = DB_Users.insert(DB_User_Name <- user.user_account,
                                           DB_User_Userinfo <- user.user_info)
        do {
            return try database.run(insertUser)
        } catch {
            print(error)
            return -1
        }
    }
    
    
    public func updateUser(user: User) -> Int64 {
        let target = DB_Users.filter(DB_User_Name == user.user_account)
        do {
            return try Int64(database.run(target.update(DB_User_Userinfo <- user.user_info)))
        } catch {
            print(error)
            return -1
        }
    }
    
    
    public func hasKeyUser(account :String) -> Bool {
        do {
            let query = DB_Users.filter(DB_User_Name == account)
            if let duser = try database.pluck(query) {
                if let info = duser[DB_User_Userinfo] {
                    if(info.count > 0) {
                        return true
                    } else {
                        return false
                    }
                }
                return false
            }
            return false
        } catch {
            return false
        }
    }
    
    public func deleteUser(account: String) {
        let query = DB_Users.filter(DB_User_Name == account)
        do {
            try database.run(query.delete())
        } catch {
            print(error)
        }
    }
}
