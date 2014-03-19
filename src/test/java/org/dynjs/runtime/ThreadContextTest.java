package org.dynjs.runtime;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ThreadContextTest extends AbstractDynJSTestSupport {

    @Test
    public void returnsNullWhenThereIsNoContext() {
        final ThreadContext context = new ThreadContext();
        assertThat(context.getCurrentContext()).isNull();
    }

    @Test
    public void hasStackBehavior() {
        final ThreadContext context = new ThreadContext();
        final ExecutionContext ec1 = createDummyExecutionContext();
        context.pushContext(ec1);
        final ExecutionContext ec2 = createDummyExecutionContext();
        context.pushContext(ec2);
        final ExecutionContext ec3 = createDummyExecutionContext();
        context.pushContext(ec3);
        assertThat(context.getCurrentContext()).isEqualTo(ec3);
        context.popContext();
        assertThat(context.getCurrentContext()).isEqualTo(ec2);
        context.popContext();
        assertThat(context.getCurrentContext()).isEqualTo(ec1);
        context.pushContext(ec3);
        assertThat(context.getCurrentContext()).isEqualTo(ec3);
    }

    @Test
    public void stackGrowsBeyondInitialSize() {
        final ThreadContext context = new ThreadContext();
        final ExecutionContext dummyExecutionContext = createDummyExecutionContext();
        for(int i = 0; i < ThreadContext.DEFAULT_EXECUTION_CTX_SIZE + 1; i++) {
            context.pushContext(dummyExecutionContext);
        }
    }

    @Test
    public void canRetrieveCurrentContext() {
        final ThreadContext context = new ThreadContext();
        final ExecutionContext executionContext = createDummyExecutionContext();
        context.pushContext(executionContext);

        assertThat(context.getCurrentContext()).isEqualTo(executionContext);
    }

    @Test
    public void canRetrieveParentContext() {
        final ThreadContext context = new ThreadContext();
        final ExecutionContext ec1 = createDummyExecutionContext();
        context.pushContext(ec1);
        final ExecutionContext ec2 = createDummyExecutionContext();
        context.pushContext(ec2);

        assertThat(context.getCurrentContext()).isEqualTo(ec2);
        assertThat(context.getParentContext()).isEqualTo(ec1);
    }

    @Test
    public void returnsNullWhenNoParentIsFound() {
        final ThreadContext context = new ThreadContext();
        assertThat(context.getParentContext()).isNull();
    }

    private ExecutionContext createDummyExecutionContext() {
        return ExecutionContext.createEvalExecutionContext(this.getRuntime());
    }
}
