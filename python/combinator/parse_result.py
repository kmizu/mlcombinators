# -*- coding: utf-8 -*-

class ParseResult:
    def __init__(self, next):
        self.next = next

    def success(self):
        pass

class ParseSuccess(ParseResult):
    def __init__(self, value, next):
        super(ParseSuccess, self).__init__(next)
        self.value = value

    @property
    def success(self):
        return True

class ParseFailure(ParseResult):
    def __init__(self, next):
        super(ParseFailure, self).__init__(next)

    @property
    def success(self):
        return False
