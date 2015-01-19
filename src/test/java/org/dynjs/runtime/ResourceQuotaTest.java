package org.dynjs.runtime;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ResourceQuotaTest extends AbstractDynJSTestSupport {
    @Test
    public void testCpuTimeCannotExceedQuota() {
        shouldExceedCpuQuota("for (;;) {}");
    }

    @Test
    public void testReasonableCpuUsageDoesntExceedQuota() {
        shouldNotExceedCpuQuota("for (var i = 0; i < 1000; ++i) { var j = i + 10; }");
    }

    @Test
    public void settingPropertiesOnObjectsCannotExceedMemoryQuota() {
        shouldExceedMemoryQuota("var obj = {}; for (var i = 0;; ++i) { obj[i.toString()] = 'foo'; }");
    }

    @Test
    public void settingTheSameNamedPropertyOverAndOverAgainDoesntExceedQuota() {
        shouldNotExceedMemoryQuota("var obj = {}; for (var i = 0; i < 100000; ++i) { obj['foo'] = i; }");
    }

    @Test
    public void settingTheSameIndexedPropertyOverAndOverAgainDoesntExceedQuota() {
        shouldNotExceedMemoryQuota("var obj = {}; for (var i = 0; i < 100000; ++i) { obj[10] = i; }");
    }

    @Test
    public void settingValuesInArrayCannotExceedMemoryQuota() {
        shouldExceedMemoryQuota("var obj = []; for (var i = 0;; ++i) { obj[i] = 'foo'; }");
    }

    @Test
    public void growingArrayWithPushCannotExceedMemoryQuota() {
        shouldExceedMemoryQuota("var obj = []; for (var i = 0;; ++i) { obj.push.apply(obj, obj); }");
    }

    @Test
    public void growingArrayWithConcatCannotExceedMemoryQuota() {
        shouldExceedMemoryQuota("var obj = []; for (var i = 0;; ++i) { obj.concat(obj); }");
    }

    @Test
    public void buildingLargeStringWithPlusOperatorCannotExceedMemoryQuota() {
        shouldExceedMemoryQuota("var str = ''; for (;;) { str = str + 'foo'; }");
    }

    @Test
    public void buildingLargeStringWithPlusEqualOperatorCannotExceedMemoryQuota() {
        shouldExceedMemoryQuota("var str = ''; for (;;) { str += 'foo'; }");
    }

    @Test
    public void buildingLargeStringWithConcatCannotExceedMemoryQuota() {
        shouldExceedMemoryQuota("var str = ''; for (;;) { str = str.concat(str); }");
    }

    @Test
    public void giganticAllocationRightBeforeRunningScriptDoesntTriggerQuota() {
        // This is nowhere near perfect (technically the doc says it could happen) but hey that's a data point
        byte[] buffer = new byte[1024 * 1024 * 250];
        shouldNotExceedMemoryQuota("var str = ''; for (var i = 0; i < 10; ++i) { str = str.concat(str); }");
    }

    @Test(expected = ExceededResourcesException.class)
    public void theSameQuotaCanBeUsedSuccessively() {
        DynJS runtime = getRuntime();
        ResourceQuota quota = new ResourceQuota(0, 1000000000);

        for (int i = 0; i < 1000000; ++i) {
            ExecutionContext context = runtime.getDefaultExecutionContext().createResourceQuotaExecutionObject(quota);
            runtime.newRunner().withContext(context).withSource("var str = 'foo' + 'bar';").evaluate();
        }
    }

    @Test
    @Ignore
    public void performanceComparisonWhenRunningWithOrWithoutQuotaIsntTooBad() {
        DynJS runtime = getRuntime();

        // Warmup (it seems to have an effect)
        runtime.newRunner().withSource("var c = 0; for (var i = 0; i < 1000000; ++i) { c++ }").evaluate();

        long beforeNormal = System.currentTimeMillis();
        runtime.newRunner().withSource("var c = 0; for (var i = 0; i < 5000000; ++i) { c++ }").evaluate();
        long timeNormal = System.currentTimeMillis() - beforeNormal;
        System.out.println("Normal: " + timeNormal + "ms");

        ResourceQuota quota = new ResourceQuota(Long.MAX_VALUE, Long.MAX_VALUE);
        ExecutionContext context = runtime.getDefaultExecutionContext().createResourceQuotaExecutionObject(quota);
        long beforeQuota = System.currentTimeMillis();
        runtime.newRunner().withContext(context).withSource("var c = 0; for (var i = 0; i < 5000000; ++i) { c++ }").evaluate();
        long timeQuota = System.currentTimeMillis() - beforeQuota;

        System.out.println("Quota: " + timeQuota + "ms");
    }

    private void shouldExceedCpuQuota(String lines) {
        ResourceQuota quota = new ResourceQuota(1000000000L, 0);
        DynJS runtime = getRuntime();
        ExecutionContext context = runtime.getDefaultExecutionContext().createResourceQuotaExecutionObject(quota);

        try {
            runtime.newRunner().withContext(context).withSource(lines).evaluate();
            Assert.assertTrue("CPU quota should have been exceeded", false);
        } catch (ExceededResourcesException ex) {
            // OK
        }
    }

    private void shouldNotExceedCpuQuota(String lines) {
        ResourceQuota quota = new ResourceQuota(1000000000L, 0);
        DynJS runtime = getRuntime();
        ExecutionContext context = runtime.getDefaultExecutionContext().createResourceQuotaExecutionObject(quota);

        try {
            runtime.newRunner().withContext(context).withSource(lines).evaluate();
        } catch (ExceededResourcesException ex) {
            Assert.assertTrue("CPU quota shouldn't have been exceeded", false);
        }
    }

    private void shouldExceedMemoryQuota(String lines) {
        DynJS runtime = getRuntime();
        ResourceQuota quota = new ResourceQuota(0, 1000000000);
        ExecutionContext context = runtime.getDefaultExecutionContext().createResourceQuotaExecutionObject(quota);

        try {
            runtime.newRunner().withContext(context).withSource(lines).evaluate();
            Assert.assertTrue("Memory quota should have been exceeded", false);
        } catch (ExceededResourcesException ex) {
            // OK
        }
    }

    private void shouldNotExceedMemoryQuota(String lines) {
        DynJS runtime = getRuntime();
        ResourceQuota quota = new ResourceQuota(0, 1000000000);
        ExecutionContext context = runtime.getDefaultExecutionContext().createResourceQuotaExecutionObject(quota);

        try {
            runtime.newRunner().withContext(context).withSource(lines).evaluate();
        } catch (ExceededResourcesException ex) {
            Assert.assertTrue("Memory quota shouldn't have been exceeded", false);
        }
    }
}
