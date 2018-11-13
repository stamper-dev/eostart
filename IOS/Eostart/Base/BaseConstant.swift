//
//  BaseConstant.swift
//  Eostart
//
//  Created by yongjoo on 2018. 9. 2..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import Foundation
import SQLite

// URLS
let WANNABIT_TEST_URL           = "https://wallet-api-dev.wannabit.io/";
let WANNABIT_PRUD_URL           = "https://wallet-api.wannabit.io/";
let BP_URL                      = "https://eos.greymass.com:443/";
let COIN_MARKET_CAP_URL         = "https://api.coinmarketcap.com/";
let EOS_PARK_URL                = "https://eospark.com/";
let EOS_PARK_API_URL            = "https://api.eospark.com/";
let EOS_PARK_API_KEY            = "61a82849325ae02328ee47863d5bca24";


let URL_PATH_VERSION_CHECK      = WANNABIT_PRUD_URL + "app/version/ios";
let URL_PATH_EOS_TOKEN_INFO     = WANNABIT_PRUD_URL + "eos/getTokenInfo";


let URL_PATH_BP_CHECK_ACCOUNTS  = BP_URL + "v1/history/get_key_accounts";
let URL_PATH_BP_GET_ACCOUNT     = BP_URL + "v1/chain/get_account";
let URL_PATH_BP_GET_ACTIONS     = BP_URL + "v1/history/get_actions";
let URL_PATH_BP_CHECK_BALANCE   = BP_URL + "v1/chain/get_currency_balance";
let URL_PATH_BP_GET_INFO        = BP_URL + "v1/chain/get_info";
let URL_PATH_BP_GET_BLOCK       = BP_URL + "v1/chain/get_block";
let URL_PATH_BP_ABI_JSON        = BP_URL + "v1/chain/abi_json_to_bin";
let URL_PATH_BP_PUSH_TX         = BP_URL + "v1/chain/push_transaction";
let URL_PATH_BP_GET_TABLE_ROW   = BP_URL + "v1/chain/get_table_rows";


let URL_EOS_TIC                 = COIN_MARKET_CAP_URL + "v2/ticker/";


let URL_PATH_PARK_ACTIONS       = EOS_PARK_URL + "interface_main";
let URL_PATH_PARK_ACTIONS2      = EOS_PARK_API_URL + "api/";



// app state
let NOTI_STATE_FINISH_LAUNCH    = Notification.Name("launched")
let NOTI_STATE_RESIGN           = Notification.Name("ResignActive")
let NOTI_STATE_BACKGROUND       = Notification.Name("EnterBackground")
let NOTI_STATE_FOREGROUND       = Notification.Name("EnterForeground")
let NOTI_STATE_ACTICE           = Notification.Name("BecomeActive")




//UserDefault Keys
let KEY_SET_CURRENCY            = "KEY_SET_CURRENCY"
let KEY_SET_LANGUAGE            = "KEY_SET_LANGUAGE"
let KEY_SET_APP_LOCK            = "KEY_SET_APP_LOCK"
let KEY_SET_APP_LOCKKEY         = "KEY_SET_APP_LOCKKEY"
let KEY_SET_BIO_AUTH            = "KEY_SET_BIO_AUTH"

let KEY_LAST_EOS_TIC            = "KEY_LAST_EOS_TIC";
let KEY_USER_INFO               = "KEY_USER_INFO";
let KEY_LAST_CHECK_TIME         = "KEY_LAST_CHECK_TIME";

let KEY_RECENT_ACCOUNT          = "KEY_RECENT_ACCOUNT"



//Action password
let ADD_USER                = "ADD_USER"
let DELETE_USER             = "DELETE_USER"
let SEND                    = "SEND"
let KEY_CHECK               = "KEY_CHECK"
let BUY_RAM_BYTE            = "BUY_RAM_BYTE"
let BUY_RAM                 = "BUY_RAM"
let SELL_RAM                = "SELL_RAM"
let DELEGATE                = "DELEGATE"
let UNDELEGATE              = "UNDELEGATE"




//DB for user
let DB_Users = Table("users")
let DB_User_Id = Expression<Int64>("id")
let DB_User_Name = Expression<String>("account")
let DB_User_Userinfo = Expression<String?>("userinfo")

//DB for tokens
let DB_Tokens = Table("tokens")
let DB_Token_Id = Expression<Int64>("id")
let DB_Token_Name = Expression<String>("name")
let DB_Token_Symbol = Expression<String>("symbol")
let DB_Token_IconUrl = Expression<String>("iconUrl")
let DB_Token_ContractAddr = Expression<String>("contractAddr")
let DB_Token_Decimals = Expression<Int64>("decimals")

//Byte
let CONSTANT_BYTE : NSDecimalNumber = 1;
let CONSTANT_KBYTE = CONSTANT_BYTE.multiplying(by: 1024)
let CONSTANT_MBYTE = CONSTANT_KBYTE.multiplying(by: 1024)
let CONSTANT_GBYTE = CONSTANT_MBYTE.multiplying(by: 1024)
let CONSTANT_TBYTE = CONSTANT_GBYTE.multiplying(by: 1024)

//Time
let CONSTANT_US : NSDecimalNumber = 1;
let CONSTANT_MS = CONSTANT_US.multiplying(by: 1000)
let CONSTANT_S = CONSTANT_MS.multiplying(by: 1000)
let CONSTANT_M = CONSTANT_S.multiplying(by: 60)
let CONSTANT_H = CONSTANT_M.multiplying(by: 60)
let CONSTANT_D = CONSTANT_H.multiplying(by: 24)
