class Expression {
    static let E: P<Int> = rule { A }
    static let A: P<Int> = rule {
        (M + ((s(literal:"+") + M) | (s(literal:"-") + M)).rep0()).map{ (l, rs) in
            rs.reduce(l) { (l, b) in
                let (op, r) = b
                return op == "+" ? l + r : l - r
            }
        }
    }
    static let M: P<Int> = rule {
        (P + ((s(literal:"*") + P) | (s(literal:"/") + P)).rep0()).map{ (l, rs) in
            rs.reduce(l) { (l, b) in
                let (op, r) = b
                return op == "*" ? l * r : l / r
            }
        }
    }
    static let P: P<Int> = rule {
        (
            (s(literal:"(") + E + s(literal:")")).map {v in
                let ((_, r), _) = v
                return r
            }
        |   N)
    }
    static let N: P<Int> = rule { Digit.map { Int($0)! } }

    static let Digit: Parser<String> = rule {
        s(literal:"0") | s(literal:"1") | s(literal:"2") | s(literal:"3") | s(literal:"4")
      | s(literal:"5") | s(literal:"6") | s(literal:"7") | s(literal:"8") | s(literal:"9")
    }
}
let E = Expression.E
print(E.parse(input:"(1+2)*3"))
