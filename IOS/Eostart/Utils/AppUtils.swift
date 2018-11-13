//
//  AppUtils.swift
//  Eostart
//
//  Created by yongjoo on 2018. 10. 5..
//  Copyright © 2018년 yongjoo. All rights reserved.
//

import Foundation
import UIKit

class AppUtils {

    static func appUtility() {
        print("appUtils")
    }
    
    
    static let handler4 = NSDecimalNumberHandler(roundingMode: NSDecimalNumber.RoundingMode.plain, scale: 4, raiseOnExactness: false, raiseOnOverflow: false, raiseOnUnderflow: false, raiseOnDivideByZero: false)
    static let handler2 = NSDecimalNumberHandler(roundingMode: NSDecimalNumber.RoundingMode.bankers, scale: 2, raiseOnExactness: false, raiseOnOverflow: false, raiseOnUnderflow: false, raiseOnDivideByZero: false)
    
    static func checkNAN(_ check: NSDecimalNumber) -> NSDecimalNumber{
        if(check.isEqual(to: NSDecimalNumber.notANumber)) {
            return NSDecimalNumber.zero
        }
        return check
    }
    
    
    static func getUserTotalAmount(_ input : NSDictionary) -> NSDecimalNumber {
        return getUserUnstakedAmount(input).adding(getUserStakedAmount(input)).adding(getUserRefundAmount(input))
    }
    
    static func getUserUnstakedAmount(_ input : NSDictionary) -> NSDecimalNumber {
        var result = NSDecimalNumber.zero
        if let value = input.value(forKeyPath: "core_liquid_balance") {
            result = NSDecimalNumber(string: String(describing: value).replacingOccurrences(of: "EOS", with: "").replacingOccurrences(of: " ", with: ""))
        }
        return checkNAN(result)
    }
    
    static func getUserStakedAmount(_ input : NSDictionary) -> NSDecimalNumber {
        var result = NSDecimalNumber.zero
        if let value = input.value(forKeyPath: "voter_info.staked") {
            result = NSDecimalNumber(string: String(describing: value))
            if(result.compare(NSDecimalNumber.zero) == .orderedDescending) {
                result = result.dividing(by: 10000).rounding(accordingToBehavior: handler4)
            } else {
                result = NSDecimalNumber.zero
            }
        }
        
        return checkNAN(result)
    }
    
    static func getUserRefundAmount(_ input : NSDictionary) -> NSDecimalNumber {
        var resultCpu = NSDecimalNumber.zero
        var resultNet = NSDecimalNumber.zero
        if let valueCpu = input.value(forKeyPath: "refund_request.cpu_amount") {
            resultCpu = checkNAN(NSDecimalNumber(string: String(describing: valueCpu).replacingOccurrences(of: "EOS", with: "").replacingOccurrences(of: " ", with: "")))
        }
        
        if let valueNet = input.value(forKeyPath: "refund_request.net_amount") {
            resultNet = checkNAN(NSDecimalNumber(string: String(describing: valueNet).replacingOccurrences(of: "EOS", with: "").replacingOccurrences(of: " ", with: "")))
        }
        return resultCpu.adding(resultNet)
    }
    
    
    static func getUserRamProgress(_ input : NSDictionary) -> Int {
        var result: Int = 0
        if let quota = input.object(forKey: "ram_quota") as? Int, let usage = input.object(forKey: "ram_usage") as? Int {
            if (quota == 0 || usage == 0) {
                result = 0
            } else {
                result = Int(Double(usage) / Double(quota) * 100)
            }

        }
        return result
    }
    
    static func getUserRamInfo(_ input : NSDictionary) -> String {
        if let quota = input.object(forKey: "ram_quota") as? Int, let usage = input.object(forKey: "ram_usage") as? Int {
            return formatByte(NSDecimalNumber.init(value: usage)) + " / " + formatByte(NSDecimalNumber.init(value: quota))
        }
        return "N/A"
    }
    
    static func getUserCpuProgress(_ input : NSDictionary) -> Int {
        var result: Int = 0
        if let used = input.value(forKeyPath: "cpu_limit.used") as? Int ,  let max = input.value(forKeyPath: "cpu_limit.max") as? Int{
            if (used == 0 || max == 0) {
                result = 0
            } else {
                result = Int(Double(used) / Double(max) * 100)
            }
        }
        return result
    }
    
    static func getUserCpuInfo(_ input : NSDictionary) -> String {
        if let used = input.value(forKeyPath: "cpu_limit.used") as? Int ,  let max = input.value(forKeyPath: "cpu_limit.max") as? Int{
            return formatTime(NSDecimalNumber.init(value: used)) + " / " + formatTime(NSDecimalNumber.init(value: max))
        }
        return "N/A"
    }
    
    static func getUserCpuAmount(_ input : NSDictionary) -> NSDecimalNumber {
        var result = NSDecimalNumber.zero
        if let weight = input.value(forKeyPath: "total_resources.cpu_weight") {
            result = checkNAN(NSDecimalNumber(string: String(describing: weight).replacingOccurrences(of: "EOS", with: "").replacingOccurrences(of: " ", with: "")))
            
        }
        return  result
    }
    
    static func getUserNetProgress(_ input : NSDictionary) -> Int {
        var result: Int = 0
        if let used = input.value(forKeyPath: "net_limit.used") as? Int ,  let max = input.value(forKeyPath: "net_limit.max") as? Int{
            if (used == 0 || max == 0) {
                result = 0
            } else {
                result = Int(Double(used) / Double(max) * 100)
            }
        }
        return result
    }
    
    static func getUserNetInfo(_ input : NSDictionary) -> String {
        if let used = input.value(forKeyPath: "net_limit.used") as? Int ,  let max = input.value(forKeyPath: "net_limit.max") as? Int{
            return formatByte(NSDecimalNumber.init(value: used)) + " / " + formatByte(NSDecimalNumber.init(value: max))
        }
        return "N/A"
    }
    
    static func getUserNetAmount(_ input : NSDictionary) -> NSDecimalNumber {
        var result = NSDecimalNumber.zero
        if let weight = input.value(forKeyPath: "total_resources.net_weight") {
            result = checkNAN(NSDecimalNumber(string: String(describing: weight).replacingOccurrences(of: "EOS", with: "").replacingOccurrences(of: " ", with: "")))
        }
        return  result
    }
    
    
    static func getDisplayEosRate(_ currency: String, tic: NSDictionary) -> String {
        var result = ""
        let path = "data.quotes." + currency + ".price"
        if let price = tic.value(forKeyPath: path) {
            let priceValue = NSDecimalNumber(string: String(describing: price))
            result =  getFormatter(currency).string(from: priceValue)! + " " + currency
        }
        return result
    }
    
    static func getDisplayTotal(_ currency: String, tic: NSDictionary, input : NSDictionary) -> String {
        var result = ""
        let path = "data.quotes." + currency + ".price"
        if let price = tic.value(forKeyPath: path) {
            let total = NSDecimalNumber(string: String(describing: price)).multiplying(by: getUserTotalAmount(input))
            result =  getFormatter(currency).string(from: total)! + " " + currency
        }
        return result
    }
    
    static func getDisplayCash(_ currency: String, tic: NSDictionary, amount : NSDecimalNumber) -> String {
        var result = ""
        let path = "data.quotes." + currency + ".price"
        if let price = tic.value(forKeyPath: path) {
            let total = NSDecimalNumber(string: String(describing: price)).multiplying(by: amount)
            result =  getFormatter(currency).string(from: total)! + " " + currency
        }
        return result
    }
    
    
    
    
    
    static func displayAmout(_ amount: NSDecimalNumber, font:UIFont , symbol:String, deciaml:Int) -> NSMutableAttributedString {
        let input = amount.rounding(accordingToBehavior: NSDecimalNumberHandler(roundingMode: NSDecimalNumber.RoundingMode.down, scale: Int16(deciaml), raiseOnExactness: false, raiseOnOverflow: false, raiseOnUnderflow: false, raiseOnDivideByZero: false))
        
        let nf = NumberFormatter()
        nf.minimumFractionDigits = deciaml
        nf.maximumFractionDigits = deciaml
        nf.numberStyle = .decimal
        let formatted = nf.string(from: input)
        
        if(symbol.count > 0) {
            let added = formatted! + " " + symbol
            let preString = added.substring(to: (added.count - symbol.count - deciaml - 1))
            let postString = added.substring(from: (added.count - symbol.count - deciaml - 1))
            
            let preAttrs = [NSAttributedStringKey.font : font]
            let postAttrs = [NSAttributedStringKey.font : font.withSize(CGFloat(Int(Double(font.pointSize) * 0.85)))]
            
            let attributedString1 = NSMutableAttributedString(string:preString, attributes:preAttrs as [NSAttributedStringKey : Any])
            let attributedString2 = NSMutableAttributedString(string:postString, attributes:postAttrs as [NSAttributedStringKey : Any])
            
            attributedString1.append(attributedString2)
            return attributedString1

        } else {
            let added = formatted
            let preString = added!.substring(to: (added!.count - deciaml))
            let postString = added!.substring(from: (added!.count - deciaml))
            
            let preAttrs = [NSAttributedStringKey.font : font]
            let postAttrs = [NSAttributedStringKey.font : font.withSize(CGFloat(Int(Double(font.pointSize) * 0.85)))]
            
            let attributedString1 = NSMutableAttributedString(string:preString, attributes:preAttrs as [NSAttributedStringKey : Any])
            let attributedString2 = NSMutableAttributedString(string:postString, attributes:postAttrs as [NSAttributedStringKey : Any])
            
            attributedString1.append(attributedString2)
            return attributedString1
        }
    }
    
    
    static func formatByte(_ input: NSDecimalNumber) -> String {
        var value = checkNAN(input)
        let nf = NumberFormatter()
        nf.minimumFractionDigits = 2
        nf.maximumFractionDigits = 2
        nf.numberStyle = .decimal
        
        if(value.compare(NSDecimalNumber.zero) == .orderedDescending) {
            if (value.compare(CONSTANT_TBYTE) == .orderedDescending) {
                value = value.dividing(by: CONSTANT_TBYTE).rounding(accordingToBehavior: handler2)
                return nf.string(from: value)! + " TB"
                
            } else if (value.compare(CONSTANT_GBYTE) == .orderedDescending) {
                value = value.dividing(by: CONSTANT_GBYTE).rounding(accordingToBehavior: handler2)
                return nf.string(from: value)! + " GB"
                
            } else if (value.compare(CONSTANT_MBYTE) == .orderedDescending) {
                value = value.dividing(by: CONSTANT_MBYTE).rounding(accordingToBehavior: handler2)
                return nf.string(from: value)! + " MB"
                
            } else {
                value = value.dividing(by: CONSTANT_KBYTE).rounding(accordingToBehavior: handler2)
                return nf.string(from: value)! + " KB"
                
            }
            
        } else {
            return "0.00 KB"
        }
    }
    
    
    static func formatTime(_ input: NSDecimalNumber) -> String {
        var value = checkNAN(input)
        if(value.compare(NSDecimalNumber.zero) == .orderedDescending) {
            if (value.compare(CONSTANT_D) == .orderedDescending) {
                value = value.dividing(by: CONSTANT_D).rounding(accordingToBehavior: handler2)
                return String(describing: value) + " Day"
                
            } else if (value.compare(CONSTANT_H) == .orderedDescending) {
                value = value.dividing(by: CONSTANT_H).rounding(accordingToBehavior: handler2)
                return String(describing: value) + " Hour"
                
            } else if (value.compare(CONSTANT_M) == .orderedDescending) {
                value = value.dividing(by: CONSTANT_M).rounding(accordingToBehavior: handler2)
                return String(describing: value) + " Min"
                
            } else if (value.compare(CONSTANT_S) == .orderedDescending) {
                value = value.dividing(by: CONSTANT_S).rounding(accordingToBehavior: handler2)
                return String(describing: value) + " Sec"
                
            } else if (value.compare(CONSTANT_MS) == .orderedDescending) {
                value = value.dividing(by: CONSTANT_MS).rounding(accordingToBehavior: handler2)
                return String(describing: value) + " ms"
                
            } else {
                value = value.rounding(accordingToBehavior: handler2)
                return String(describing: value) + " μs"
            }
            
        } else {
            return "0.00 μs"
        }
    }
    
    static func getFormatter(_ currency:String) -> NumberFormatter{
        let nf = NumberFormatter()
        if(currency == "KRW") {
            nf.minimumFractionDigits = 0
            nf.maximumFractionDigits = 0
        } else if (currency == "BTC") {
            nf.minimumFractionDigits = 8
            nf.maximumFractionDigits = 8
        }  else {
            nf.minimumFractionDigits = 2
            nf.maximumFractionDigits = 2
        }
        nf.numberStyle = .decimal
        return nf
    }
    
    
    static func changeTimeFormat(input: String) -> String {
        let eosFormatter = DateFormatter()
        eosFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        let localFormatter = DateFormatter()
        localFormatter.dateFormat = "yy/MM/dd HH:mm:ss"
        
        let fullDate = eosFormatter.date(from: input)
        return localFormatter.string(from: fullDate!)
    }
    
    
    static func userInputAmoutFormat(amount: NSDecimalNumber, deciaml:Int) -> String {
        let input = amount.rounding(accordingToBehavior: NSDecimalNumberHandler(roundingMode: NSDecimalNumber.RoundingMode.down, scale: Int16(deciaml), raiseOnExactness: false, raiseOnOverflow: false, raiseOnUnderflow: false, raiseOnDivideByZero: false))

        let nf = NumberFormatter()
        nf.minimumFractionDigits = deciaml
        nf.maximumFractionDigits = deciaml
        nf.numberStyle = .decimal
        return nf.string(from: input) ?? ""
    }
    
    
    
    
    
    static func getActionAccoutSeq(_ input : NSDictionary) -> CLong {
        var result: CLong = 0
        if let seq = input.object(forKey: "account_action_seq") as? CLong{
            result = seq
        }
        return result
    }
    
    static func getActionBlocktime(_ input : NSDictionary) -> String {
        var result: String = ""
        if let time = input.object(forKey: "block_time") as? String {
            result = self.changeTimeFormat(input: time)
        }
        return result
    }
    
    static func getActionTxid(_ input : NSDictionary) -> String {
        var result: String = ""
        if let txid = input.value(forKeyPath: "action_trace.trx_id") as? String{
            result = txid
        }
        return result
    }
    
    static func getActionMemo(_ input : NSDictionary) -> String {
        var result: String = ""
        guard let data = input.value(forKeyPath: "action_trace.act.data") else{
            return result
        }
        
        if (data is NSDictionary) {
            if let memo = input.value(forKeyPath: "action_trace.act.data.memo") as? String {
                result = memo
            }
        }
        return result
    }
    
    
    static func getActionName(_ input : NSDictionary) -> String {
        var result: String = ""
        if let name = input.value(forKeyPath: "action_trace.act.name") as? String{
            result = name
        }
        return result
    }
    
    
    static func getActionFrom(_ input : NSDictionary) -> String {
        var result: String = ""
        if let from = input.value(forKeyPath: "action_trace.act.data.from") as? String{
            result = from
        }
        return result
    }
    
    static func getActionTo(_ input : NSDictionary) -> String {
        var result: String = ""
        if let to = input.value(forKeyPath: "action_trace.act.data.to") as? String{
            result = to
        }
        return result
    }
    
    static func getActionQuantity(_ input : NSDictionary) -> String {
        var result: String = ""
        if let to = input.value(forKeyPath: "action_trace.act.data.quantity") as? String{
            result = to
        }
        return result
    }
    
    static func getActionActAccount(_ input : NSDictionary) -> String {
        var result: String = ""
        if let actor = input.value(forKeyPath: "action_trace.act.account") as? String{
            result = actor
        }
        return result
    }
    
    static func getActionActMessage(_ input : NSDictionary) -> String {
        
        var result: String = ""
        guard let data = input.value(forKeyPath: "action_trace.act.data") else {
            return result
        }
        
        if (data is NSDictionary) {
            if let message = input.value(forKeyPath: "action_trace.act.data.message") as? String{
                result = message
            }
        }
        return result
    }
    
}

extension String {
    func index(from: Int) -> Index {
        return self.index(startIndex, offsetBy: from)
    }
    
    func substring(from: Int) -> String {
        let fromIndex = index(from: from)
        return substring(from: fromIndex)
    }
    
    func substring(to: Int) -> String {
        let toIndex = index(from: to)
        return substring(to: toIndex)
    }
    
    func substring(with r: Range<Int>) -> String {
        let startIndex = index(from: r.lowerBound)
        let endIndex = index(from: r.upperBound)
        return substring(with: startIndex..<endIndex)
    }
}
