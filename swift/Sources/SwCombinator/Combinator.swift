//
//  Combinator.swift
//  SwCombinator
//
//  Created by Kota Mizushima on 2019/02/03.
//

import Foundation

enum Result<A> {
    case Success(value: A, next: String)
    case Failure(next: String)
}
class Parser<A> {
    let function: (String) -> Result<A>
    init(function: @escaping (String) -> Result<A>) {
        self.function = function
    }
    
    func parse(input: String) -> Result<A> {
        return self.function(input)
    }
}
typealias P<A> = Parser<A>
extension Parser {
    func rep0() -> Parser<Array<A>> {
        return Parser<Array<A>> { (input: String) in
            func rep(input: String) -> Result<Array<A>> {
                switch self.function(input) {
                case let .Success(value, next1):
                    switch(rep(input:next1)) {
                    case let .Success(result, next2):
                        var newArray = result
                        newArray.insert(value, at:0)
                        return .Success(value:newArray, next:next2)
                    default:
                        abort()
                }
                case let .Failure(next):
                    return .Success(value:[], next:next)
                }
            }
            return rep(input:input)
        }
    }
    
    func plus<B>(right: Parser<B>) -> Parser<(A, B)> {
        return Parser<(A, B)> { (input: String) in
            let result1 = self.function(input)
            switch result1 {
            case let .Success(value1, next1):
                let result2 = right.function(next1)
                switch result2 {
                case let .Success(value2, next2):
                    return .Success(value:(value1, value2), next: next2)
                default:
                    return .Failure(next: next1)
                }
            default:
                return .Failure(next: input)
            }
        }
    }
    
    func or(right: Parser<A>) -> Parser<A> {
        return Parser<A> {(input: String) in
            switch self.function(input) {
            case let .Success(value, next):
                return .Success(value:value, next:next)
            case let .Failure(next):
                return right.function(next)
            }
        }
    }
    
    func map<B>(translator: @escaping (A) -> B) -> Parser<B> {
        return Parser<B> { (input: String) in
            switch self.function(input) {
            case let .Success(value, next):
                return .Success(value:translator(value), next:next)
            case let .Failure(next):
                return .Failure(next:next)
            }
        }
    }
}
func rule<A>(body: @escaping () -> Parser<A>) -> Parser<A> {
    return Parser<A> {(input: String) in
        return body().function(input)
    }
}
func +<A, B>(lhs: Parser<A>, rhs: Parser<B>) -> Parser<(A, B)> {
    return lhs.plus(right:rhs)
}
func |<A>(lhs: Parser<A>, rhs: Parser<A>) -> Parser<A> {
    return lhs.or(right:rhs)
}
func s(literal: String) -> Parser<String> {
    return Parser<String> {(input: String) in
        let literalLength = literal.lengthOfBytes(using:String.Encoding.utf8)
        let inputLength = input.lengthOfBytes(using: String.Encoding.utf8)
        if literalLength > 0 && inputLength == 0 {
            return .Failure(next:input)
        } else if input.starts(with: literal) {
            let from = input.index(input.startIndex, offsetBy:literalLength)
            return .Success(value:literal, next:String(input[from...]))
        } else {
            return .Failure(next:input)
        }
    }
}
