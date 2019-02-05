package com.github.kmizu.haxanator;

class LiteralParser implements Parser<String> {
  private var literal: String;
  public function new(literal: String) {
    this.literal = literal;
  }
  public function parse(input: String): ParseResult<String> {
    var index = input.indexOf(literal);
    return if(index != 0)
      ParseResult.Failure("expected: " + literal);
    else
      ParseResult.Success(input.substring(0, literal.length), input.substring(literal.length));
  }
}
