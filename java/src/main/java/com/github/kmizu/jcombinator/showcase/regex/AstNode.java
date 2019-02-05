package com.github.kmizu.jcombinator.showcase.regex;

import com.sun.org.apache.regexp.internal.RE;

public class AstNode {
    public interface RVisitor<R, C> {
        public R visitRGrouped(RGrouped node, C context);
        public R visitRAny(RAny node, C context);
        public R visitRString(RString node, C context);
        public R visitRChoice(RChoice node, C context);
        public R visitRSequence(RSequence node, C context);
        public R visitRRepeat0(RRepeat0 node, C context);
        public R visitRRepeat1(RRepeat1 node, C context);
    }
    public static abstract class RExpression {
        public abstract <R, C> R accept(RVisitor<R, C> visitor, C context);
    }
    public static class RString extends RExpression {
        private final String literal;

        public RString(String literal) {
            this.literal = literal;
        }

        public String getLiteral() {
            return literal;
        }

        @Override
        public <R, C> R accept(RVisitor<R, C> visitor, C context) {
            return visitor.visitRString(this, context);
        }
    }
    public static class RAny extends RExpression {
        public RAny() {
        }

        @Override
        public <R, C> R accept(RVisitor<R, C> visitor, C context) {
            return visitor.visitRAny(this, context);
        }
    }
    public static class RChoice extends RExpression {
        private final RExpression lhs, rhs;

        public RChoice(RExpression lhs, RExpression rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public RExpression getLhs() {
            return lhs;
        }

        public RExpression getRhs() {
            return rhs;
        }

        @Override
        public <R, C> R accept(RVisitor<R, C> visitor, C context) {
            return visitor.visitRChoice(this, context);
        }
    }
    public static class RGrouped extends RExpression {
        private final RExpression target;

        public RGrouped(RExpression target) {
            this.target = target;
        }

        public RExpression getTarget() {
            return target;
        }

        @Override
        public <R, C> R accept(RVisitor<R, C> visitor, C context) { return visitor.visitRGrouped(this, context); }
    }
    public static class RSequence extends RExpression {
        private final RExpression lhs, rhs;

        public RSequence(RExpression lhs, RExpression rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public RExpression getLhs() {
            return lhs;
        }

        public RExpression getRhs() {
            return rhs;
        }
        @Override
        public <R, C> R accept(RVisitor<R, C> visitor, C context) {
            return visitor.visitRSequence(this, context);
        }
    }
    public static class RRepeat0 extends RExpression {
        private final RExpression target;

        public RRepeat0(RExpression target) {
            this.target = target;
        }

        public RExpression getTarget() { return target; }

        @Override
        public <R, C> R accept(RVisitor<R, C> visitor, C context) {
            return visitor.visitRRepeat0(this, context);
        }
    }
    public static class RRepeat1 extends RExpression {
        private final RExpression target;

        public RRepeat1(RExpression target) {
            this.target = target;
        }

        public RExpression getTarget() { return target; }

        @Override
        public <R, C> R accept(RVisitor<R, C> visitor, C context) {
            return visitor.visitRRepeat1(this, context);
        }
    }
}
