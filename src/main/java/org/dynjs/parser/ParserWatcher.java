package org.dynjs.parser;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Object create(Token payload) {
        if (payload != null) {
            if (payload.getType() == JavascriptParser.StringLiteral) {
                payload.setText(escapeHandler.unescape(this.context, payload.getText(), isStrict()));
            }
        }
        return new JavascriptTree(payload);
    }

    public void sourceElement(sourceElement_return ret) {
        if (isInDirectiveProlog()) {
            CommonTree tree = (CommonTree) ret.getTree();
            if (tree.getType() == JavascriptParser.StringLiteral) {
                String text = tree.getText();
                if (text.equals("use strict")) {
                    setStrict(true);
                }
            } else {
                setInDirectiveProlog(false);
            }
        }
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
