package org.dynjs.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.dynjs.parser.ECMAScriptParser.functionDeclaration_return;
import org.dynjs.parser.ECMAScriptParser.functionExpression_return;
import org.dynjs.parser.ECMAScriptParser.program_return;
import org.dynjs.parser.ECMAScriptParser.propertyAssignment_return;
import org.dynjs.parser.ECMAScriptParser.sourceElement_return;
import org.dynjs.runtime.ExecutionContext;

public class ParserWatcher extends CommonTreeAdaptor {

    private EscapeHandler escapeHandler = new EscapeHandler();
    private List<WatcherState> state = new ArrayList<>();
    private ExecutionContext context;

    public ParserWatcher(ExecutionContext context) {
        this.state.add(new WatcherState());
        this.context = context;
    }
    
    public void enterIteration() {
        getTopState().enterIteration();
    }
    
    public void exitIteration() {
        getTopState().exitIteration();
    }
    
    public void enterLabel(String label) {
        getTopState().enterLabel( label );
    }
    
    public void exitLabel() {
        getTopState().exitLabel();
    }
    
    public boolean isValidIteration(String label) {
        return getTopState().isValidIteration(label);
    }
    
    public boolean isValidReturn() {
        return this.state.size() > 2;
    }

    public boolean isValidIdentifier(String ident) {
        if (!isStrict()) {
            return true;
        }

        return VerifierUtils.isStrictIdentifier(ident);
    }

    public boolean isValidIdentifierIfIdentifier(CommonTree tree) {
        if (tree.getType() == JavascriptParser.Identifier) {
            return isValidIdentifier(tree.getText());
        }

        return true;
    }

    public boolean areValidParameterNames(List<String> names) {
        if (isStrict()) {
            Set<String> seen = new HashSet<>();
            for (String name : names) {
                if (seen.contains(name)) {
                    return false;
                }
                if (!isValidIdentifier(name)) {
                    return false;
                }
                seen.add(name);
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
            } else if ( payload.getType() == JavascriptParser.Identifier ) {
                payload.setText( escapeHandler.unescape(this.context, payload.getText(), isStrict()) );
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
    
    public void startPropertyAssignment() {
        pushState();
    }
    
    public void endPropertyAssignment(propertyAssignment_return ret) {
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
        public List<String> labels = new ArrayList<>();
        public int iterations = 0;

        WatcherState() {

        }

        WatcherState(WatcherState parent) {
            this.strict = parent.strict;
        }
        
        void enterLabel(String label) {
            this.labels.add( label );
        }
        
        void exitLabel() {
            this.labels.remove( this.labels.size() - 1 );
        }
        
        void enterIteration() {
            ++iterations;
        }
        
        void exitIteration() {
            --iterations;
        }
        
        boolean isValidIteration(String label) {
            if ( label == null ) {
                return (iterations > 0);
            }
            return labels.contains(label);
        }

    }

}
