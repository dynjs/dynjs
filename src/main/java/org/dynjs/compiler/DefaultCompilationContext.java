package org.dynjs.compiler;

import org.dynjs.Config;
import org.dynjs.runtime.*;

/**
 * @author Bob McWhirter
 */
public class DefaultCompilationContext implements CompilationContext {

    private final Config config;
    private final BlockManager blockManager;
    private final JSCompiler compiler;

    public DefaultCompilationContext(Config config) {
        this.config = config;
        this.blockManager = new BlockManager();
        this.compiler = new JSCompiler( this.config );
    }

    public DefaultCompilationContext(CompilationContext parent) {
        this( parent.getConfig() );
    }

    @Override
    public JSCompiler getCompiler() {
        return this.compiler;
    }

    @Override
    public BlockManager getBlockManager() {
        return this.blockManager;
    }

    @Override
    public DynamicClassLoader getClassLoader() {
        return this.config.getClassLoader();
    }

    @Override
    public Config getConfig() {
        return this.config;
    }

    @Override
    public JSObject createSyntaxError(String message) {
        return null;
    }

    @Override
    public LexicalEnvironment getLexicalEnvironment() {
        return null;
    }

    @Override
    public GlobalContext getGlobalContext() {
        return null;
    }
}
