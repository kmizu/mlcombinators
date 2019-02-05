package com.github.kmizu.haxanator;

enum ParseResult<T> {
  Success<T>(value: T, next: String);
  Failure(message: String);
}
