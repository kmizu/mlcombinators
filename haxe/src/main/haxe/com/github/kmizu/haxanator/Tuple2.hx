package com.github.kmizu.haxanator;

enum Tuple2<T1, T2> {
  tuple2(t1 : T1, t2 : T2);
}
@:forward @:callable abstract Tup2<A,B>(Tuple2<A,B>) from Tuple2<A,B> to Tuple2<A,B>{
  public function new(l,r){
    this = tuple2(l,r);
  }
  /**
    Returns the value on the left hand side.
   **/
  public function fst() : A {
    return Tuples2.fst(this);
  }
  /**
    Returns the value on the right hand side.
   **/
  public function snd() : B {
    return Tuples2.snd(this);
  }
  /**
    Returns the value on the right hand side.
   **/
  public function swap() : Tup2<B, A> {
    return Tuples2.swap(this);
  }
  /**
    Returns a tuple with the left hand side on the right and vice-versa.
   **/
  public function equals(b : Tup2<A,B>): Bool {
    return Tuples2.equals(this,b);
  }
  /**
    Produces an array of untyped values.
   **/
  public function toArray() : Array<Dynamic> {
    return Tuples2.toArray(this);
  }
  /**
    Unpacks the tuple and applies the function with the internal values.
   **/
  public function into<C>(fn:A->B->C):C {
    return Tuples2.into(this,fn);
  }
  /**
    Transforms the function to receive a tuple rather than two arguments.
   **/
  public inline static function tupled<A,B,C>(f : A -> B -> C){
    return Tuples2.tupled(f);
  }
  /**
    Transforms a function taking a tuple to one taking two arguments.
   **/
  public inline static function untupled<A,B,C>(f:Tup2<A,B>->C):A->B->C{
    return Tuples2.untupled(f);
  }
}

class Tuples2 {
  static public function apply<T1,T2,R>(tuple: Tup2<T1->R,T1>){
    return fst(tuple)(snd(tuple));
  }
  static public function fst<T1, T2>(tuple : Tup2<T1, T2>) : T1 {
    return switch (tuple){
      case tuple2(a,_)    : a;
    }
  }
  static public function snd<T1, T2>(tuple : Tup2<T1, T2>) : T2 {
    return switch (tuple){
      case tuple2(_,b)    : b;
    }
  }
  static public function swap<T1, T2>(tuple : Tup2<T1, T2>) : Tup2<T2, T1> {
    return switch (tuple) {
      case tuple2(a, b): tuple2(b, a);
    }
  }
  static public function equals<T1, T2>(a : Tup2<T1, T2>, b : Tup2<T1, T2>) : Bool {
    return switch (a) {
      case tuple2(t0l, t0r):
        switch (b) {
          case tuple2(t1l, t1r) :  (t0l == t1l) && (t0r == t1r);
          default               : false;
        }
      default : false;
    }
  }
  static public function toArray<T1, T2>(tuple : Tup2<T1, T2>) : Array<Dynamic> {
    return switch (tuple){
      case tuple2(a,b)    : [a,b];
    }
  }
  static public function into<A,B,C>(t:Tup2<A,B>,f:A->B->C):C {
    return switch(t){
      case tuple2(a,b)    : f(a,b);
    }
  }
  public inline static function tupled<A,B,C>(f : A -> B -> C){
    return into.bind(_,f);
  }
  public inline static function untupled<A,B,C>(f:Tup2<A,B>->C):A->B->C{
    return function(a:A,b:B):C{
      return f(tuple2(a,b));
    }
  }
}
