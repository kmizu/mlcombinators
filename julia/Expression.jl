include("./ParserCombinator.jl")
using ParserCombinator

@rule(E) do
  A()
end

@rule(A) do
  chainl(
    M(),
    (p_string("+") > (op) -> (lhs, rhs) -> lhs + rhs) /
    (p_string("-") > (op) -> (lhs, rhs) -> lhs - rhs)
  )
end

@rule(M) do
  chainl(
    P(),
    (p_string("*") > (op) -> (lhs, rhs) -> lhs * rhs) /
    (p_string("/") > (op) -> (lhs, rhs) -> lhs / rhs)
  )
end

@rule(P) do
  (
    (p_string("(") + E() + p_string(")")) >
    values -> values[1][2]
  ) / N()
end

@rule(N) do
  regex(r"[0-9]+") > (n) -> parse(Int64, n)
end

const parser = E()
println(parser.parse("(1+2)*(3+4)"))
