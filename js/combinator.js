"use strict";
const util = require('util');

class ParseResult {
  constructor(next) {
    this.next = next;
  }
}

class ParseSuccess extends ParseResult {
  constructor(value, next) {
    super(next);
    this.value = value;
  }

  isSuccess() {
    return true;
  }
}

class ParseFailure extends ParseResult {
  constructor(next) {
    super(next);
  }

  isSuccess() {
    return false;
  }
}

class Parser {
  parse(input) {
    return new ParseFailure(input);
  }

  or(rhs) {
    return new OrParser(this, rhs);
  }

  cat(rhs) {
    return new CatParser(this, rhs);
  }

  map(fun) {
    return new MapParser(this, fun);
  }

  flatMap(fun) {
    return new FlatMapParser(this, fun);
  }

  rep() {
    return new RepeatParser(this);
  }

  chainl(q) {
  }

  chainlI(p, q) {
    const value = this.cat((q.cat(p).rep())).map((value) => {
      const x = value[0];
      const xs = value[1];
      let a = x;
      while(xs.length > 0) {
        let f = xs[0][0];
        let b = xs[0][1];
        a = f(a, b);
        xs.shift();
      }
      return a;
    });
    return value;
  }
  chainl(q) {
    return this.chainlI(this, q);
  }
}

class RepeatParser extends Parser {
  constructor(parser) {
    super();
    this.parser = parser;
  }
  parse(input) {
    let rest = input;
    let values = [];
    while(true) {
      let r = this.parser.parse(rest);
      if(!r.isSuccess()) return new ParseSuccess(values, rest);
      values.push(r.value);
      rest = r.next;
    }
  }
}

class RegExpParser extends Parser {
  constructor(regexString) {
    super();
    this.regexString = regexString;
  }

  parse(input) {
    const regex = new RegExp(this.regexString, "g");
    const array = regex.exec(input);
    if(array != null && array.index == 0) {
      return new ParseSuccess(array[0], input.substring(array[0].length))
    }else {
      return new ParseFailure(input);
    }
  }
}

class FlatMapParser extends Parser {
  constructor(parser, fun) {
    super();
    this.parser = parser;
    this.fun = fun;
  }
  parse(input) {
    const r = this.parser.parse(input);
    if(!r.isSuccess()) return r;
    return this.fun(r.value).parse(r.next);
  }
}

class MapParser extends Parser {
  constructor(parser, fun) {
    super();
    this.parser = parser;
    this.fun = fun;
  }

  parse(input) {
    const r = this.parser.parse(input);
    if(!r.isSuccess()) return r;
    return new ParseSuccess(this.fun(r.value), r.next);
  }
}


class OrParser extends Parser {
  constructor(lhs, rhs) {
    super();
    this.lhs = lhs;
    this.rhs = rhs;
  }
  parse(input) {
    const r1 = this.lhs.parse(input);
    if(r1.isSuccess()) {
      return r1;
    }else{
      return this.rhs.parse(input);
    }
  }
}

class CatParser extends Parser {
  constructor(lhs, rhs) {
    super();
    this.lhs = lhs;
    this.rhs = rhs;
  }
  parse(input) {
    const r1 = this.lhs.parse(input);
    if(r1.isSuccess()) {
      const r2 = this.rhs.parse(r1.next);
      if(r2.isSuccess()) {
        return new ParseSuccess([r1.value, r2.value], r2.next);
      }else {
        return new ParseFailure(r1.next);
      }
    }else {
      return new ParseFailure(input);
    }
  }
}

class StringParser extends Parser {
  constructor(literal) {
    super();
    this.literal = literal;
  }
  parse(input) {
    if(input.startsWith(this.literal)) {
      return new ParseSuccess(this.literal, input.substring(this.literal.length));
    } else {
      return new ParseFailure(input);
    }
  }
}
class DelayedParser extends Parser {
  constructor(fun) {
    super();
    this.fun = fun;
  }
  parse(input) {
    const fun = this.fun
    return fun().parse(input);
  }
}

class ESCombinator {
  s(literal) {
    return new StringParser(literal);
  }
  r(regexString) {
    return new RegExpParser(regexString);
  }
  f(fun) {
    return new DelayedParser(fun);
  }
  rule(fun) {
    return new DelayedParser(fun);
  }
  chainl(p, q) {
    return p.chainl(q);
  }
}

module.exports=ESCombinator;
