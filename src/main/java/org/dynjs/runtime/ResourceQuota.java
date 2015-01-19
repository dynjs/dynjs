package org.dynjs.runtime;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.atomic.AtomicLong;

public class ResourceQuota {
    // Sadly, the call needed to monitor memory allocations made by a thread is specific to
    // Oracle's implementation. Under other JVMs we won't be able to enforce memory quotas.
    private static final ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
    private static final ThreadMXBean sunThreadMxBean = threadMxBean instanceof com.sun.management.ThreadMXBean ? (com.sun.management.ThreadMXBean) threadMxBean : null;

    private AtomicLong cpuTime;
    private AtomicLong memoryUsage;
    private final boolean monitorCpuTime;
    private final boolean monitorMemoryUsage;

    public ResourceQuota(long cpuTime, long memoryUsage) {
        this.cpuTime = new AtomicLong(cpuTime);
        this.memoryUsage = new AtomicLong(memoryUsage);

        monitorCpuTime = cpuTime > 0;
        monitorMemoryUsage = memoryUsage > 0;
        if (monitorMemoryUsage && sunThreadMxBean == null) {
            throw new UnsupportedOperationException("Cannot enforce memory quotas when not using Oracle's JVM implementation");
        }
    }

    // WatchDogs decrement the quota values every time they are checked. Once the
    // values reach 0 an exception is thrown. By decoupling this task from the Quota
    // it is possible to use the same quota for several script invocations.
    public WatchDog createWatchDog() {
        return new WatchDog();
    }

    public class WatchDog {
        private static final int CHECK_EVERY = 25;

        private long threadId = Thread.currentThread().getId();
        private long lastCpuTime = readCpuTime(threadId);
        private long lastAllocatedBytes = readAllocatedBytes(threadId);
        private int cycle = 0;

        public void check() {
            assert(Thread.currentThread().getId() == threadId);

            if (++cycle % CHECK_EVERY != 0) {
                return;
            }

            if (monitorCpuTime) {
                checkCpuTime();
            }

            if (monitorMemoryUsage) {
                checkAllocatedBytes();
            }
        }

        private void checkCpuTime() {
            long current = readCpuTime(threadId);
            long delta = current - lastCpuTime;
            lastCpuTime = current;

            if (cpuTime.addAndGet(-delta) < 0) {
                throw new ExceededResourcesException("CPU usage has exceeded the allowed quota");
            }
        }

        private void checkAllocatedBytes() {
            long current = readAllocatedBytes(threadId);
            long delta = current - lastAllocatedBytes;
            lastAllocatedBytes = current;

            if (memoryUsage.addAndGet(-delta) < 0) {
                throw new ExceededResourcesException("Memory usage has exceeded the allowed quota");
            }
        }
    }

    private static long readCpuTime(long threadId) {
        return threadMxBean.getThreadCpuTime(threadId);
    }

    private static long readAllocatedBytes(long threadId) {
        return ((com.sun.management.ThreadMXBean) threadMxBean).getThreadAllocatedBytes(threadId);
    }
}
