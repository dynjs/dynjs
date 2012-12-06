package org.dynjs.compiler.partial;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.util.ArrayList;
import java.util.List;

import me.qmx.jitescript.JiteClass;

import org.dynjs.Config;
import org.dynjs.codegen.CodeGeneratingVisitorFactory;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.ExecutionContext;
import org.objectweb.asm.Opcodes;

public class MultipleClassCompiler extends AbstractPartialCompiler {

    private List<PartialCompiler> plans;

    public MultipleClassCompiler(Config config, DynamicClassLoader classLoader, CodeGeneratingVisitorFactory factory, List<BlockStatement> chunks, int chunkSize) {
        super( config, classLoader, factory );
        
        List<PartialCompiler> plans = new ArrayList<PartialCompiler>();
        
        int classStart = 0;
        int i = 0;
        while ( classStart < chunks.size() ) {
            int classEnd = classStart + chunkSize;
            
            if ( classEnd > chunks.size() ) {
                classEnd = chunks.size();
            }
            
            List<BlockStatement> classChunks = chunks.subList(classStart, classEnd);
            
            SingleClassCompiler classPlan = new SingleClassCompiler(this, classChunks);
            plans.add( classPlan );
            classStart = classEnd;
            ++i;
        }
        
        this.plans = treeify( plans, chunkSize );
    }
    
    private MultipleClassCompiler(MultipleClassCompiler parent, List<PartialCompiler> plans) {
        super( parent );
        this.plans = plans;
    }
    
    private List<PartialCompiler> treeify(List<PartialCompiler> plans, int chunkSize) {
        return treeify( 0, plans, chunkSize );
    }
    
    private List<PartialCompiler> treeify(int iteration, List<PartialCompiler> plans, int chunkSize) {
        if (  plans.size() <= chunkSize ) {
            return plans;
        }
        
        List<PartialCompiler> newPlans = new ArrayList<PartialCompiler>();
        
        int treeStart = 0;
        
        while ( treeStart < plans.size() ) {
            int treeEnd = treeStart + chunkSize;
            
            if ( treeEnd > plans.size() ) {
                treeEnd = plans.size();
            }
            
            List<PartialCompiler> treePlans = plans.subList(treeStart, treeEnd);
            
            PartialCompiler treePlan = new MultipleClassCompiler( this, treePlans );
            newPlans.add( treePlan );
        }
        
        return treeify( iteration + 1, plans, chunkSize );
    }
    
    @Override
    public void define(JiteClass cls, ExecutionContext context, boolean strict) {
        int numChunks = this.plans.size();
        
        for ( int i = 0 ; i < numChunks ; ++i ) {
            JiteClass chunkClass = new JiteClass(cls.getClassName() + "$" + i , new String[] { p(BasicBlock.class) } );
            chunkClass.defineDefaultConstructor();
            this.plans.get(i).define(chunkClass, context, strict);
            defineClass(chunkClass);
            
            cls.defineField("chunk" + i, Opcodes.ACC_PRIVATE, ci(BasicBlock.class), null);
        }
        
        cls.defineMethod("call", Opcodes.ACC_PUBLIC, sig(Completion.class, ExecutionContext.class), new MultipleClassCaller( cls.getClassName(), numChunks ));
        
        cls.defineMethod("initializeCode", Opcodes.ACC_PRIVATE, sig(void.class), new MultipleClassInitializer( cls.getClassName(), numChunks ));
    }

}
