"use strict";
import test from 'ava';
import ESCombinator from '../combinator';
import util from 'util';

const c = new ESCombinator();
const f = c.f.bind(this);
const rule = c.rule.bind(this);

test("Hello, World!", t => {
  const p = c.s("Hello, World!");
  t.true(p.parse("Hello, World!").isSuccess());
  t.true(!p.parse("Hello, World?").isSuccess());
});

test("Hello or World", t => {
  const p = c.s("Hello").or(c.s("World"));
  t.true(p.parse("Hello").isSuccess());
  t.true(p.parse("World").isSuccess());
  t.true(!p.parse("HELLO").isSuccess());
  t.true(!p.parse("WORLD").isSuccess());
});

test("Hello and World", t => {
  const p = c.s("Hello").cat(c.s("World"));
  t.true(p.parse("HelloWorld").isSuccess());
  t.true(!p.parse("World").isSuccess());
  t.true(!p.parse("Hello").isSuccess());
  t.true(!p.parse("WORLD").isSuccess());
});


test("(Hello and World).map", t => {
  const p = c.s("Hello").cat(c.s("World")).map((x) => [x, x].toString());
  const r = p.parse("HelloWorld");
  t.true(r.isSuccess());
  t.true(r.value == [["Hello", "World"], ["Hello", "World"]].toString());
});


test("Hello.flatMap(v => World)", t => {
  const p = c.s("Hello").flatMap((v) => {
    return c.s("World");
  });
  let r = p.parse("Hello");
  t.true(!r.isSuccess());
  r = p.parse("HelloWorld");
  t.true(r.isSuccess());
  t.true(r.value == "World");
});

test("[0-9]+", t => {
  const p1 = c.r("[0-9]+");
  let r = p1.parse("100");
  t.true(r.isSuccess());
  t.true(r.value === "100");
  const p2 = c.r("[0-9]+").map(x => parseInt(x));
  r = p2.parse("100");
  t.true(r.isSuccess());
  t.true(!(r.value === "100"));
  t.true(r.value === 100);
});
test("J.rep", t => {
  const p = c.s("J").rep();
  let r = p.parse("JJJJ");
  t.true(r.isSuccess());
  t.true(r.value.toString() == ["J", "J", "J", "J"].toString())
});
test("chainl", t => {
  const Q = c.s("+").map((op) => (lhs, rhs) => lhs + rhs);
  const R = c.s("-").map((op) => (lhs, rhs) => lhs - rhs);
  const E = rule(() => P.chainl(Q.or(R)));
  const P = rule(() => c.r("[0-9]+").map((x) => parseInt(x)));
  let r1 = E.parse("100");
  t.true(r1.isSuccess());
  t.true(r1.value === 100);
  r1 = E.parse("100+200-50");
  t.true(r1.isSuccess());
  t.true(r1.next === "");
  t.true(r1.value === 250);
});
test("calculator", t => {

  const E = rule(() => A);
  const A = rule(() => 
    M.chainl(
      (
       c.s("+").map((op) => (lhs, rhs) => lhs + rhs)
      ).or(
       c.s("-").map((op) => (lhs, rhs) => lhs - rhs)
      )
    )
  );
  const M = rule(() => 
    P.chainl(
      (
       c.s("*").map((op) => (lhs, rhs) => lhs * rhs)).or(
       c.s("/").map((op) => (lhs, rhs) => lhs / rhs)
      )
    )
  );
  const P = rule(() =>
    (c.s("(").cat(E)).cat(c.s(")")).map((values) => {
      return values[0][1];
    }).or(N)
  );
  const N = rule(() =>
    c.r("[0-9]+").map((n) => parseInt(n))
  );
  const r1 = E.parse("111");
  const r2 = E.parse("222");
  t.true(r1.isSuccess());
  t.true(r2.isSuccess());
  t.true(r1.value === 111);
  t.true(r2.value === 222);
  const r3 = E.parse("A");
  t.true(!r3.isSuccess());
  const r4 = E.parse("(1+2*3)*4");
  t.true(r4.isSuccess());
  t.true(r4.value === 28);
  const r5 = E.parse("(3+2*4)/3");
  t.true(Math.floor(r5.value)=== 3);

});
