# -*- coding: utf-8 -*-

import re

from combinator import parse_result
from parse_result import *

class Parser:
    def parse(self, input):
        return ParseFailure(input)

    def mor(self, rhs):
        return OrParser(self,  rhs)

    def cat(self, rhs):
        return CatParser(self, rhs)

    def map(self, fun):
        return MapParser(self, fun)

    def flat_map(self, fun):
        return FlatMapParser(self, fun)

    def rep(self):
        return RepeatParser(self)

    def chainlI(self, p, q):
        def fun(value):
            x = value[0]
            xs = value[1]
            a = x
            while len(xs)> 0:
                f = xs[0][0]
                b = xs[0][1]
                a = f(a, b)
                del xs[0]
            return a
        value = self.cat((q.cat(p).rep())).map(fun)
        return value

    def chainl(self, q):
        return self.chainlI(self, q)

class RepeatParser(Parser): 
    def __init__(self, parser):
        super(RepeatParser, self).__init__()
        self.parser = parser

    def parse(self, input):
        rest = input
        values = []
        while True:
            r = self.parser.parse(rest)
            if not r.success: return ParseSuccess(values, rest)
            values.append(r.value)
            rest = r.next
        pass


class RegexParser(Parser):
    def __init__(self, regex_string):
        super(RegexParser, self).__init__()
        self.pattern = re.compile(regex_string)

    def parse(self, input):
        m = self.pattern.match(input)
        if m != None:
            s, e = m.span()
            return ParseSuccess(input[s:e], input[e:])
        else:
            return ParseFailure(input)

class FlatMapParser(Parser):
    def __init__(self, parser, fun):
        super(FlatMapParser, self).__init__()
        self.parser = parser
        self.fun = fun

    def parse(self, input):
        r = self.parser.parse(input)
        if not r.success: return r
        return self.fun(r.value).parse(r.next)

class MapParser(Parser):
    def __init__(self, parser, fun):
        super(MapParser, self).__init__()
        self.parser = parser
        self.fun = fun

    def parse(self, input):
        r = self.parser.parse(input)
        if not r.success: return r
        return ParseSuccess(self.fun(r.value), r.next)

class OrParser(Parser):
    def __init__(self, lhs, rhs):
        super(OrParser, self).__init__()
        self.lhs = lhs
        self.rhs = rhs

    def parse(self, input):
        r = self.lhs.parse(input)
        if r.success:
            return r
        else:
            return self.rhs.parse(input)


class CatParser(Parser):
    def __init__(self, lhs, rhs):
        super(CatParser, self).__init__()
        self.lhs = lhs
        self.rhs = rhs

    def parse(self, input):
        r1 = self.lhs.parse(input)
        if r1.success:
            r2 = self.rhs.parse(r1.next)
            if r2.success:
                return ParseSuccess([r1.value, r2.value], r2.next)
            else:
                return ParseFailure(r1.next)
        else:
            return ParseFailure(input)

class StringParser(Parser):
    def __init__(self, literal):
        super(StringParser, self).__init__()
        self.literal = literal

    def parse(self, input):
        if input.startswith(self.literal):
            rest = input[len(self.literal):]
            return ParseSuccess(self.literal, rest)
        else:
            return ParseFailure(input)

class DelayedParser(Parser):
    def __init__(self, fun):
        super(DelayedParser, self).__init__()
        self.fun = fun

    def parse(self, input):
        return self.fun().parse(input)

def s(literal):
    return StringParser(literal)

def reg(regex_string):
    return RegexParser(regex_string)

def f(fun):
    return DelayedParser(fun)

def rule(fun):
    return DelayedParser(fun)

def chainl(p, q):
    return p.chainl(q)

class Parser:
    def parse(self, input):
        return ParseFailure(input)

    def mor(self, rhs):
        return OrParser(self,  rhs)

    def cat(self, rhs):
        return CatParser(self, rhs)

    def map(self, fun):
        return MapParser(self, fun)

    def flat_map(self, fun):
        return FlatMapParser(self, fun)

    def rep(self):
        return RepeatParser(self)

    def chainlI(self, p, q):
        def fun(value):
            x = value[0]
            xs = value[1]
            a = x
            while len(xs)> 0:
                f = xs[0][0]
                b = xs[0][1]
                a = f(a, b)
                del xs[0]
            return a
        value = self.cat((q.cat(p).rep())).map(fun)
        return value

    def chainl(self, q):
        return self.chainlI(self, q)

class RepeatParser(Parser): 
    def __init__(self, parser):
        super(RepeatParser, self).__init__()
        self.parser = parser

    def parse(self, input):
        rest = input
        values = []
        while True:
            r = self.parser.parse(rest)
            if not r.success: return ParseSuccess(values, rest)
            values.append(r.value)
            rest = r.next
        pass


class RegexParser(Parser):
    def __init__(self, regex_string):
        super(RegexParser, self).__init__()
        self.pattern = re.compile(regex_string)

    def parse(self, input):
        m = self.pattern.match(input)
        if m != None:
            s, e = m.span()
            return ParseSuccess(input[s:e], input[e:])
        else:
            return ParseFailure(input)

class FlatMapParser(Parser):
    def __init__(self, parser, fun):
        super(FlatMapParser, self).__init__()
        self.parser = parser
        self.fun = fun

    def parse(self, input):
        r = self.parser.parse(input)
        if not r.success: return r
        return self.fun(r.value).parse(r.next)

class MapParser(Parser):
    def __init__(self, parser, fun):
        super(MapParser, self).__init__()
        self.parser = parser
        self.fun = fun

    def parse(self, input):
        r = self.parser.parse(input)
        if not r.success: return r
        return ParseSuccess(self.fun(r.value), r.next)

class OrParser(Parser):
    def __init__(self, lhs, rhs):
        super(OrParser, self).__init__()
        self.lhs = lhs
        self.rhs = rhs

    def parse(self, input):
        r = self.lhs.parse(input)
        if r.success:
            return r
        else:
            return self.rhs.parse(input)


class CatParser(Parser):
    def __init__(self, lhs, rhs):
        super(CatParser, self).__init__()
        self.lhs = lhs
        self.rhs = rhs

    def parse(self, input):
        r1 = self.lhs.parse(input)
        if r1.success:
            r2 = self.rhs.parse(r1.next)
            if r2.success:
                return ParseSuccess([r1.value, r2.value], r2.next)
            else:
                return ParseFailure(r1.next)
        else:
            return ParseFailure(input)

class StringParser(Parser):
    def __init__(self, literal):
        super(StringParser, self).__init__()
        self.literal = literal

    def parse(self, input):
        if input.startswith(self.literal):
            rest = input[len(self.literal):]
            return ParseSuccess(self.literal, rest)
        else:
            return ParseFailure(input)

class DelayedParser(Parser):
    def __init__(self, fun):
        super(DelayedParser, self).__init__()
        self.fun = fun

    def parse(self, input):
        return self.fun().parse(input)
