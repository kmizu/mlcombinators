package com.github.kmizu.haxanator;

interface Parser<T> {
  public function parse(input: String): ParseResult<T>;
}
