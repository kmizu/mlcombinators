function fib(n) 
  if n < 1
    0
  elseif n < 2
    1
  else
    fib(n - 1) + fib(n - 2)
  end
end

@time fib(big"33")
