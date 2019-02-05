package com.github.kmizu.jcombinator.showcase.regex;

import static com.github.kmizu.jcombinator.Parser.*;
import com.github.kmizu.jcombinator.*;
import com.github.kmizu.jcombinator.datatype.Function2;

import java.util.List;

public class RegexParser {
    public Rule<AstNode.RExpression> expression() {
        return rule(() -> alternative());
    }

    public Rule<AstNode.RExpression> alternative() {
        return rule(() -> {
            final Parser<Function2<AstNode.RExpression, AstNode.RExpression, AstNode.RExpression>> Q = string("|").map(__ ->
                AstNode.RChoice::new
            );
            return concatenative().chain(Q);
        });
    }

    public Rule<AstNode.RExpression> concatenative() {
        return rule(() -> {
            final Parser<Function2<AstNode.RExpression, AstNode.RExpression, AstNode.RExpression>> Q = string("").map(__ ->
                AstNode.RSequence::new
            );
            return repeatable().chain(Q);
        });
    }

    public Rule<AstNode.RExpression> repeatable() {
        return rule(() ->
            primary().cat((string("*").or(string("+"))).many()).map(t -> {
                AstNode.RExpression e = t.item1();
                List<String> ops = t.item2();
                for(String op:ops) {
                    e = op.equals("*") ? new AstNode.RRepeat0(e) : new AstNode.RRepeat1(e);
                }
                return e;
            })
        );
    }

    public Rule<AstNode.RExpression> primary() {
        return rule(() ->
            grouped().or(single()).or(escaped()).or(rany())
        );
    }

    public Rule<AstNode.RExpression> grouped() {
        return rule(() ->
            string("(").cat(expression()).cat(string(")")).map(t -> new AstNode.RGrouped(t.item1().item2()))
        );
    }

    public Rule<AstNode.RExpression> single() {
        return rule(() ->
            (meta().not().cat(any())).map(t -> new AstNode.RString(t.item2()))
        );
    }
    public Rule<AstNode.RExpression> escaped() {
        return rule(() ->
            string("\\").cat(meta()).map(t -> new AstNode.RString(t.item2()))
        );
    }
    public Rule<AstNode.RExpression> rany() {
        return rule(() ->
            string(".").map(__ -> new AstNode.RAny())
        );
    }
    public Rule<String> meta() {
        return rule(() ->
            set('|', '*', '+', '(', ')', '.', '\\')
        );
    }
}
