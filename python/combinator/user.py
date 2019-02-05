# -*- coding: utf-8 -*-

from combinator import parser
from combinator import parse_result
from parser import *
from parse_result import *

E = rule(lambda : A)
A = rule((lambda :
        M.chainl(
            (
                s('+').map(lambda op: (lambda lhs, rhs: lhs + rhs))
            ).mor(
                s('-').map(lambda op: (lambda lhs, rhs: lhs - rhs))
            )
        )))
M = rule((lambda :
        P.chainl(
            (
                s('*').map(lambda op: (lambda lhs, rhs: lhs * rhs))
            ).mor(
                s('/').map(lambda op: (lambda lhs, rhs: lhs / rhs))
            )
        )))
P = rule((lambda :
        (s('(').cat(E)).cat(s(')')).map(lambda values: values[0][1]).mor(N)
        ))

N = rule((lambda :
        reg('[0-9]+').map(lambda n: int(n))))

r = int(E.parse("111+222").value)
print(r)
r = int(E.parse("(1+2*3)*4").value)
print(r)
r = int(E.parse("(3+2*4)/3").value)
print(r)
