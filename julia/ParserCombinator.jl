module ParserCombinator
  export Success,
         Failure,
         Parser,
         @rule,
         @define_rule,
         or,
         cat,
         repeat,
         chainl,
         map,
         p_string,
         regex

  type Success
    value
    next
    success
    Success(value, next) = new(value, next, true)
  end

  type Failure
    next
    success
    Failure(next) = new(next, false)
  end

  type Parser
    parse
    Parser(parse) = new(parse)
  end

  macro rule(body, name)
    esc(quote
      function $(name)()
        Parser((input) -> ($body)().parse(input))
      end
    end)
  end

  function or(lhs :: Parser, rhs :: Parser)
    Parser() do input
      r = lhs.parse(input)
      if !r.success
        rhs.parse(input)
      else
        r 
      end
    end
  end

  function cat(lhs :: Parser, rhs :: Parser)
    Parser() do input
      r1 = lhs.parse(input)
      if r1.success
        r2 = rhs.parse(r1.next)
        if r2.success
          Success((r1.value, r2.value), r2.next)
        else
          Failure(r2.next)
        end
      else
        Failure(r1.next)
      end
    end
  end


  function map(parser :: Parser, f :: Function)
    Parser() do input
      r = parser.parse(input)
      if r.success
        Success(f(r.value), r.next)
      else
        r 
      end
    end
  end

  function repeat(parser :: Parser)
    Parser() do input
      rest = input
      values = [] 
      while true
        r = parser.parse(rest)
        if !r.success
          return Success(values, rest)
        end
        values = vcat(values, r.value)
        rest = r.next
      end
    end
  end

  function chainl(p :: Parser, q :: Parser)
    value = map(cat(p, repeat(cat(q, p))),
                  function(value)
                    x = value[1]
                    xs = value[2]
                    a = x
                    while length(xs) > 0 
                      f = xs[1][1]
                      b = xs[1][2]
                      a = f(a, b)
                      xs = xs[2:end]
                    end
                    return a
                  end
                 )
    return value
  end

  function p_string(literal)
    Parser() do input
      range = search(input, literal)
      if range.start != 1
        Failure(input)
      else
        Success(literal, input[length(literal)+1:end])
      end
    end
  end

  function regex(regex :: Regex)
    Parser() do input
      r = match(regex, input)
      if r != nothing
        Success(r.match, input[length(r.match)+1:end])
      else
        Failure(input)
      end
    end
  end

  Base.:+(p1::Parser, p2::Parser) = cat(p1, p2)
  Base.:/(p1::Parser, p2::Parser) = or(p1, p2)
  Base.:>(p::Parser, f::Function) = map(p, f)
end
