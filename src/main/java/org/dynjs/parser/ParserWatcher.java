package org.dynjs.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.dynjs.parser.ES3Parser.functionDeclaration_return;
import org.dynjs.parser.ES3Parser.functionExpression_return;
import org.dynjs.parser.ES3Parser.program_return;
import org.dynjs.parser.ES3Parser.sourceElement_return;
import org.dynjs.runtime.ExecutionContext;

public class ParserWatcher extends CommonTreeAdaptor {

    private EscapeHandler escapeHandler = new EscapeHandler();
    private List<WatcherState> state = new ArrayList<>();
    private ExecutionContext context;

    public ParserWatcher(ExecutionContext context) {
        this.state.add(new WatcherState());
        this.context = context;
    }

    public boolean isValidIdentifier(String ident) {
        if (!isStrict()) {
            return true;
        }

        return VerifierUtils.isStrictIdentifier(ident);
    }

    public boolean areValidParameterNames(List<String> names) {
        if (isStrict()) {
            Set<String> seen = new HashSet<>();
            for (String name : names) {
                if ( seen.contains(name) ) {
                    return false;
                }
                if (!isValidIdentifier(name)) {
                    return false;
                }
                seen.add( name );
            }
        }
        return true;
    }

    @Override
    public Object create(Token payload) {
        if (payload != null) {
            if (payload.getType() == JavascriptParser.StringLiteral || payload.getType() == JavascriptParser.ContinuedStringLiteral) {
                String text = null;
                boolean continued = false;
                boolean escaped = false;
                if (payload.getType() == JavascriptParser.ContinuedStringLiteral) {
                    continued = true;
                }
                text = escapeHandler.unescape(this.context, payload.getText(), isStrict());
                if (!text.equals(payload.getText())) {
                    escaped = true;
                }
                payload = new StringLiteralToken(payload, text, continued, escaped);
                return new JavascriptTree(payload);
            }
        }
        return new JavascriptTree(payload);
    }

    public void sourceElement(sourceElement_return ret) {
        if (isInDirectiveProlog()) {
            CommonTree tree = (CommonTree) ret.getTree();
            if (tree.getToken() instanceof StringLiteralToken) {
                StringLiteralToken token = (StringLiteralToken) tree.getToken();
                String text = token.getText();
                if (text.equals("use strict") && !token.isEscaped() && !token.isContinued()) {
                    setStrict(true);
                }
            } else {
                setInDirectiveProlog(false);
            }
        }
    }

    public void emptyStatement() {
        setInDirectiveProlog(false);
    }

    public void startProgram(program_return ret) {
        pushState();
    }

    public void endProgram(program_return ret) {
        if (isStrict()) {
            ((JavascriptTree) ret.getTree()).setStrict(true);
        }
        popState();
    }

    public void startFunctionExpression() {
        pushState();
    }

    public void endFunctionExpression(functionExpression_return ret) {
        if (isStrict()) {
            ((JavascriptTree) ret.getTree()).setStrict(true);
        }
        popState();
    }

    public void startFunctionDeclaration() {
        pushState();
    }

    public void endFunctionDeclaration(functionDeclaration_return ret) {
        if (isStrict()) {
            ((JavascriptTree) ret.getTree()).setStrict(true);
        }
        popState();
    }

    public void pushState() {
        this.state.add(new WatcherState(getTopState()));
    }

    public void popState() {
        this.state.remove(this.state.size() - 1);
    }

    public boolean isInDirectiveProlog() {
        return getTopState().inDirectiveProlog;
    }

    public void setInDirectiveProlog(boolean inDirectiveProlog) {
        getTopState().inDirectiveProlog = inDirectiveProlog;
    }

    public boolean isStrict() {
        return getTopState().strict;
    }

    public void setStrict(boolean strict) {
        getTopState().strict = strict;
    }

    WatcherState getTopState() {
        return this.state.get(this.state.size() - 1);
    }

    private static class WatcherState {

        public boolean inDirectiveProlog = true;
        public boolean strict = false;

        WatcherState() {

        }

        WatcherState(WatcherState parent) {
            this.strict = parent.strict;
        }

    }

}
