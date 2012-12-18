package org.dynjs.compiler.bytecode.partial;

import java.util.List;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.compiler.bytecode.Chunker;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.runtime.DynamicClassLoader;

public class CompilationPlanner {
    
    private Config config;
    private DynamicClassLoader classLoader;
    private CodeGeneratingVisitorFactory factory;

    public CompilationPlanner(Config config, DynamicClassLoader classLoader, CodeGeneratingVisitorFactory factory) {
        this.config = config;
        this.classLoader = classLoader;
        this.factory = factory;
    }
    public PartialCompiler plan(BlockStatement body) {
        return plan( Chunker.chunk( body ) );
    }
    
    public PartialCompiler plan(BlockStatement body, int chunkSize) {
        return plan( Chunker.chunk( body, chunkSize ), chunkSize );
    }
    
    public PartialCompiler plan(List<BlockStatement> chunks) {
        return plan( chunks, Chunker.CHUNK_SIZE );
    }
    
    public PartialCompiler plan(List<BlockStatement> chunks, int chunksPerClass) {
        
        if ( chunks.size() == 1 ) {
            return new InlineCompiler( this.config, this.classLoader, this.factory, chunks.get(0));
        }
        
        if ( chunks.size() <= chunksPerClass ) {
            return new SingleClassCompiler( this.config, this.classLoader, this.factory, chunks );
        }
        
        return new MultipleClassCompiler( this.config, this.classLoader, this.factory, chunks, chunksPerClass );
    }

}
