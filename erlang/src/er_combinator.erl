-module(er_combinator).

%% Macros
-define(rule(Block), (fun(Input) -> (Block)(Input) end)). 



%% API exports
-export([literal/1, s/1, alt/2, seq/2, rep/1, map/2, chainl/2, regex/1]).
-ifdef(TEST).
-include_lib("eunit/include/eunit.hrl").
-endif.

%%====================================================================
%% API functions
%%====================================================================

%% string literal in PEG
literal(Prefix) -> 
        fun(Input) ->
                        case string:prefix(Input, Prefix) of
                                nomatch -> {failure, "", Input};
                                Suffix -> {success, Prefix, Suffix}
                        end
        end.

%% regex literal.  Although this is not included in PEG, this is convenient.
regex(Literal) ->
        fun(Input) ->
                        {M, [{BeginIndex, EndIndex}]} = re:run(Input, Literal),
                        case M of
                                match when BeginIndex =:= 0 ->
                                        {success, 
                                         string:slice(Input, 0, EndIndex),
                                         string:slice(Input, EndIndex, string:len(Input))};
                                _ ->
                                        {failure, "", Input}
                        end
        end.

%% Shorthand of literal/1
s(Prefix) -> literal(Prefix).

%% X / Y in PEG
alt(X, Y) -> 
        fun(Input) ->
                        case X(Input) of
                                {failure, _, _} -> Y(Input);
                                Success -> Success
                        end
        end.

%% X Y in PEG
seq(X, Y) ->
        fun(Input) ->
                        case X(Input) of
                                {success, V1, Next1} ->
                                        case Y(Next1) of
                                                {success, V2, Next2} -> 
                                                        {success, {V1, V2}, Next2};
                                                Failure -> Failure
                                        end;
                                Failure -> Failure
                        end
        end.

%% X* in PEG
rep(X) ->
        fun(Input) ->
                        {success, Values, Next} = loop(X, Input, []),
                        {success, Values, Next}
        end.

%% map combinator
map(Parser, F) ->
        fun(Input) ->
                        case Parser(Input) of
                                {success, V, Next} -> {success, F(V), Next};
                                Failure -> Failure
                        end
        end.

%% chainl1 combhinator
chainl(P, Q) ->
        map(
          seq(P, rep(seq(Q, P))),
          fun(Values) ->
                          {X, XS} = Values,
                          lists:foldl(
                            fun(R, A) ->
                                            {F, E} = R,
                                            F(A, E)
                            end,
                            X,
                            XS
                          )
          end
        ).

%%====================================================================
%% Internal functions
loop(Parser, Rest, Results) ->
        case Parser(Rest) of
                {success, V, Next} -> loop(Parser, Next, [V|Results]);
                {failure, _, Next} -> {success, lists:reverse(Results), Next}
        end.

e() -> ?rule(a()).
a() -> ?rule(
          chainl(
            m(),
            alt(
              map(s("+"), 
                  fun(_) ->
                                  fun (Lhs, Rhs) -> 
                                                  Lhs + Rhs 
                                  end
                  end
                 ),
              map(s("-"), 
                  fun(_) ->
                                  fun (Lhs, Rhs) -> 
                                                  Lhs - Rhs 
                                  end
                  end
                 )
             )
           )
        ).


m() -> ?rule(
          chainl(
            p(),
            alt(
              map(s("*"), 
                  fun(_) ->
                                  fun (Lhs, Rhs) -> 
                                                  Lhs * Rhs 
                                  end
                  end
                 ),
              map(s("/"), 
                  fun(_) ->
                                  fun (Lhs, Rhs) -> 
                                                  Lhs div Rhs 
                                  end
                  end
                 )
             )
           )
        ).

p() -> ?rule(
          alt(
            map(
              seq(seq(s("("), e()), s(")")),
              fun(Result) ->
                              {{"(", V}, ")"} = Result,
                              V
              end
             ),
            n()
           )
        ).

n() -> map(
         regex("[0-9]+"),
         fun(V1) ->
                         V2 = list_to_integer(V1),
                         V2
         end
        ).
%%====================================================================
%%

-ifdef(TEST).

seq_test() ->
        X = seq(s("a"), s("b")),
        {C, Value, Rest} = X("ab"),
        ?assert(C =:= success),
        ?assert(Value =:= {"a", "b"}),
        ?assert(Rest =:= "")
        .

alt_test() ->
        X = alt(s("a"), s("b")),
        {C, Value, Rest} = X("ab"),
        ?assert(C =:= success),
        ?assert(Value =:= "a"),
        ?assert(Rest =:= "b"),
        {C2, Value2, Rest2} = X("ba"),
        ?assert(C2=:= success),
        ?assert(Value2=:= "b"),
        ?assert(Rest2=:= "a")
        .

rep_test() ->
        X = rep(s("a")),
        {C, Value, Rest} = X("aaa"),
        ?assert(C =:= success),
        ?assert(Value =:= ["a", "a", "a"]),
        ?assert(Rest =:= ""),
        {C2, Value2, Rest2} = X(""),
        ?assert(C2 =:= success),
        ?assert(Value2 =:= []),
        ?assert(Rest2 =:= "")
        .

regex_test() ->
        X = regex("a*"),
        {C, Value, Rest} = X("aaab"),
        ?assert(C =:= success),
        ?assert(Value =:= "aaa"),
        ?assert(Rest =:= "b").

expression_test() ->
        ?assert({success, 1, ""} =:= (e())("1")),
        ?assert({success, 2, ""} =:= (e())("1+1")),
        ?assert({success, 1, ""} =:= (e())("1*1")),
        ?assert({success, 1, ""} =:= (e())("1/1")),
        ?assert({success, 3, ""} =:= (e())("2+1")),
        ?assert({success, 1, ""} =:= (e())("2-1")),
        ?assert({success, 2, ""} =:= (e())("2*1")),
        ?assert({success, 2, ""} =:= (e())("2/1")),
        ?assert({success, 21, ""} =:= (e())("(1+2)*(3+4)")).

simple_test() ->
        seq_test(),
        alt_test(),
        rep_test(),
        regex_test(),
        expression_test().

-endif.
