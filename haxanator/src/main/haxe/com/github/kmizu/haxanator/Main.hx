package com.github.kmizu.haxanator;

class Main {
  static public function main() {
    var p1 = new LiteralParser("100");
    var p2 = new LiteralParser("200");
    var p3 = new OrParser(p1, p2);
    var result = p3.parse("100000");
    trace(result);
    result = p3.parse("200000");
    trace(result);
  }
}
