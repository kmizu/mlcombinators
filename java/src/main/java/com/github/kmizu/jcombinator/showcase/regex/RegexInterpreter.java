package com.github.kmizu.jcombinator.showcase.regex;

import com.github.kmizu.jcombinator.ParseResult;
import com.github.kmizu.jcombinator.datatype.Function0;
import com.github.kmizu.jcombinator.datatype.Function1;

public class RegexInterpreter {
    interface SuccessfullContinuation extends Function0<Boolean> {}
    private static class ExpressionRewriter implements AstNode.RVisitor<AstNode.RExpression, Void> {
        @Override
        public AstNode.RExpression visitRGrouped(AstNode.RGrouped node, Void context) {
            return new AstNode.RGrouped(node.getTarget().accept(this, context));
        }

        @Override
        public AstNode.RExpression visitRString(AstNode.RString node, Void context) {
            return node;
        }

        @Override
        public AstNode.RExpression visitRAny(AstNode.RAny node, Void context) {
            return node;
        }

        @Override
        public AstNode.RExpression visitRChoice(AstNode.RChoice node, Void context) {
            return new AstNode.RChoice(
                node.getLhs().accept(this, context),
                node.getRhs().accept(this, context)
            );
        }

        @Override
        public AstNode.RExpression visitRSequence(AstNode.RSequence node, Void context) {
            return new AstNode.RSequence(
                node.getLhs().accept(this, context),
                node.getRhs().accept(this, context)
            );
        }

        @Override
        public AstNode.RExpression visitRRepeat0(AstNode.RRepeat0 node, Void context) {
            return new AstNode.RRepeat0(
                node.getTarget().accept(this, context)
            );
        }

        @Override
        public AstNode.RExpression visitRRepeat1(AstNode.RRepeat1 node, Void context) {
            AstNode.RExpression target = node.getTarget().accept(this, context);
            return new AstNode.RSequence(
                target,
                new AstNode.RRepeat0(target)
            );
        }
    }
    private static class ExpressionInterpreter implements  AstNode.RVisitor<Boolean, SuccessfullContinuation> {
        private String input;
        private int cursor;

        public ExpressionInterpreter(String input) {
            this.input = input;
            this.cursor = cursor;
        }

        @Override
        public Boolean visitRGrouped(AstNode.RGrouped node, SuccessfullContinuation context) {
            return node.getTarget().accept(this, context);
        }

        @Override
        public Boolean visitRString(AstNode.RString node, SuccessfullContinuation context) {
            String slice = input.substring(cursor);
            if(slice.startsWith(node.getLiteral())) {
                cursor += node.getLiteral().length();
                return context.invoke();
            } else {
                return false;
            }
        }

        @Override
        public Boolean visitRAny(AstNode.RAny node, SuccessfullContinuation context) {
            String slice = input.substring(cursor);
            if(slice.length() > 0) {
                cursor++;
                return context.invoke();
            } else {
                return false;
            }
        }

        @Override
        public Boolean visitRChoice(AstNode.RChoice node, SuccessfullContinuation context) {
            final int start = cursor;
            boolean lhsMatched = node.getLhs().accept(this, context);
            if(lhsMatched) {
                return true;
            } else {
                cursor = start;
                return node.getRhs().accept(this, context);
            }
        }

        @Override
        public Boolean visitRSequence(AstNode.RSequence node, SuccessfullContinuation context) {
            return node.getLhs().accept(this, () ->
                node.getRhs().accept(this, context)
            );
        }

        @Override
        public Boolean visitRRepeat0(AstNode.RRepeat0 node, SuccessfullContinuation context) {
            Function1<Function0<Boolean>, Boolean>[] onSuccessRep = new Function1[1];
            onSuccessRep[0] = (f) -> {
                final int start = cursor;
                final SuccessfullContinuation nf = () -> {
                    cursor = start;
                    return context.invoke() ? true : f.invoke();
                };
                return (node.getTarget().accept(
                    this,
                    () -> onSuccessRep[0].invoke(nf)
                ) ? true : nf.invoke());
            };
            return onSuccessRep[0].invoke(context);
        }

        @Override
        public Boolean visitRRepeat1(AstNode.RRepeat1 node, SuccessfullContinuation context) {
            throw new UnsupportedOperationException("ExpressionInterpreter#visit(AstNode.RRepeat1, SuccessfullContinuation)");
        }
    }

    public boolean matches(String regex, String input) {
        ParseResult<AstNode.RExpression> result = new RegexParser().expression().invoke(regex);
        boolean[] matchResult = new boolean[1];
        matchResult[0] = false;
        result.onSuccess(e -> {
            ExpressionRewriter rewriter = new ExpressionRewriter();
            AstNode.RExpression newE = e.value().accept(rewriter, null);
            ExpressionInterpreter interpreter = new ExpressionInterpreter(input);
            matchResult[0] = newE.accept(interpreter, () -> true);
        });
        return matchResult[0];
    }

    public static void main(String[] args) {
        RegexInterpreter i = new RegexInterpreter();
        System.out.println(i.matches("a*aaa", "aaa"));
        System.out.println(i.matches("(a*|xxx)y(bca)*(x|y)d", "xxxybcabcaxd"));
    }
}
