package com.github.kmizu.haxanator;                                  
                                                                     
class OrParser<A> implements Parser<A> {              
  private var lhs: Parser<A>;                                        
  private var rhs: Parser<A>;                                        
                                                                     
  public function new(lhs: Parser<A>, rhs: Parser<A>) {              
    this.lhs = lhs;                                                  
    this.rhs = rhs;                                                  
  }                                                                  
                                                                     
  public function parse(input: String): ParseResult<A> {  
    var resultLhs = lhs.parse(input);                                
    return switch(resultLhs) {                                              
      case s=Success(v1, n1):
        s;
      case Failure(_):                                             
        rhs.parse(input);
    }                                                                
  }                                                                  
}                                                                    
