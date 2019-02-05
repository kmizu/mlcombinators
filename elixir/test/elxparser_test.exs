defmodule ElxParserTest do
  use ExUnit.Case
  import ElxParser

  def e do
    rule(fn ->
      a
    end)
  end

  def a do
    rule(fn ->
      chainl(
        m,
        map(string("+"), fn _ -> 
          fn lhs, rhs ->
            lhs + rhs
          end
        end) <|>
        map(string("-"), fn _ -> 
          fn lhs, rhs ->
            lhs - rhs
          end
        end)
      )
    end)
  end

  def m do
    rule(fn ->
      chainl(
        p,
        map(string("*"), fn _ -> 
          fn lhs, rhs ->
            lhs * rhs
          end
        end) <|>
        map(string("/"), fn _ -> 
          fn lhs, rhs ->
            lhs * rhs
          end
        end)
      )
    end)
  end

  def p do
    rule(fn ->
      map(string("(") ~>> e ~>> string(")"), fn result ->
        {{"(", v}, ")"} = result
        v
      end) <|>
      n
    end)
  end

  def n do
    map(regex(~r/[0-9]+/), fn v1 ->
      {v2, _} = Integer.parse(v1)
      v2
    end)
  end

  test "for input '1'" do
    assert {:success, 1, ""} == e.("1")
  end
  test "for input '1+1'" do
    assert {:success, 2, ""} == e.("1+1")
  end
  test "for input '1*1'" do
    assert {:success, 1, ""} == e.("1*1")
  end
  test "for input '1/1'" do
    assert {:success, 1, ""} == e.("1/1")
  end
  test "for input '2+1'" do
    assert {:success, 3, ""} == e.("2+1")
  end
  test "for input '2-1'" do
    assert {:success, 1, ""} == e.("2-1")
  end
  test "for input '2*1'" do
    assert {:success, 2, ""} == e.("2*1")
  end
  test "for input '2/1'" do
    assert {:success, 2, ""} == e.("2/1")
  end
  test "for input '(1+2)*(3+4)'" do
    assert {:success, 21, ""} == e.("(1+2)*(3+4)")
  end
end
