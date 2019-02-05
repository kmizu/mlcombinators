package com.github.kmizu.haxanator;
import com.github.kmizu.haxanator.Tuple2.*;

class SeqParser<A, B> implements Parser<Tuple2<A, B>> {
  private var lhs: Parser<A>;
  private var rhs: Parser<B>;

  public function new(lhs: Parser<A>, rhs: Parser<B>) {
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public function parse(input: String): ParseResult<Tuple2<A, B>> {
    var resultLhs = lhs.parse(input);
    switch(resultLhs) {
      case Success(v1, n1):
        var resultRhs = rhs.parse(n1);
        switch(resultRhs) {
          case Failure(msg): 
            return Failure(msg);
          case Success(v2, n2):
            return Success(tuple2(v1, v2), n2);
        }
      case Failure(msg):
        return Failure(msg);
    }
  }
}
