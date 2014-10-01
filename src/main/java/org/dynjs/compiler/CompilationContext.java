package org.dynjs.compiler;

import org.dynjs.Config;
import org.dynjs.runtime.*;

/**
 * @author Bob McWhirter
 */
public interface CompilationContext {
    JSCompiler getCompiler();
    BlockManager getBlockManager();
    DynamicClassLoader getClassLoader();
    Config getConfig();

    // used by parser

    JSObject createSyntaxError(String message);

    // only used by function compilation
    LexicalEnvironment getLexicalEnvironment();
    GlobalContext getGlobalContext();
}
