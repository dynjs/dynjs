package org.dynjs.runtime;

import com.sun.management.ThreadMXBean;

import java.lang.management.ManagementFactory;

public class ResourceQuota {
    private long cpuTime;
    private final boolean monitorCpuTime;
    private long memoryUsage;
    private final boolean monitorMemoryUsage;

    ResourceQuota(long cpuTime, long memoryUsage) {
        this.cpuTime = cpuTime;
        this.monitorCpuTime = cpuTime > 0;
        this.memoryUsage = memoryUsage;
        this.monitorMemoryUsage = memoryUsage > 0;
    }

    public WatchDog createWatchDog() {
        return new WatchDog();
    }

    public class WatchDog {
        private long lastCpuTime = readCpuTime();
        private long lastAllocatedBytes = readAllocatedBytes();

        public void check() {
            if (monitorCpuTime) {
                checkCpuTime();
            }

            if (monitorMemoryUsage) {
                checkAllocatedBytes();
            }
        }

        private void checkCpuTime() {
            long current = readCpuTime();
            long delta = current - lastCpuTime;
            cpuTime -= delta;
            lastCpuTime = current;

            if (cpuTime < 0) {
                throw new ExceededResourcesException("CPU usage has exceeded the allowed quota");
            }
        }

        private void checkAllocatedBytes() {
            long current = readAllocatedBytes();
            long delta = current - lastAllocatedBytes;
            memoryUsage -= delta;
            lastAllocatedBytes = current;

            if (memoryUsage < 0) {
                throw new ExceededResourcesException("Memory usage has exceeded the allowed quota");
            }
        }
    }

    private static long readCpuTime() {
        return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }

    private static long readAllocatedBytes() {
        return ((ThreadMXBean) ManagementFactory.getThreadMXBean()).getThreadAllocatedBytes(Thread.currentThread().getId());
    }
}
