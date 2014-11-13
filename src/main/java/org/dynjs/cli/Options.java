package org.dynjs.cli;

import com.headius.options.Option;
import org.dynjs.Config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Options {
    public static final String PREFIX = "dynjs";
    public static final Option<Config.CompileMode> CLI_COMPILE_MODE = Option.enumeration(PREFIX, "compile.mode", Category.COMPILER, Config.CompileMode.OFF, "Set compile mode: OFF = no compilation (interpreted); JIT = at runtime; FORCE = before execution; IR = IR runtime");
    public static final Option<Config.KernelMode> CLI_KERNEL_MODE = Option.enumeration(PREFIX, "kernel.mode", Category.KERNEL, Config.KernelMode.INTERNAL, "Set kernel mode: INTERNAL = use bundled JS kernel; EXTERNAL = loads JS kernel files straight from the source tree");
    public static final Option<Integer> JIT_TRESHOLD = Option.integer(PREFIX, "jit.threshold", Category.JIT, 50, "Set JIT threshold");
    public static final Option<Boolean> JIT = Option.bool(PREFIX, "jit.enabled", Category.JIT, false, "Enable JIT compilation");
    public static final Option<Boolean> JIT_ASYNC = Option.bool(PREFIX, "jit.async", Category.JIT, false, "Make JIT run in background");
    public static final Option<Boolean> INVOKEDYNAMIC = Option.bool(PREFIX, "invokedynamic.enabled", Category.INVOKEDYNAMIC, true, "Enable invokedynamic support");
    public static final Option<Boolean> COMPATIBILITY_RHINO = Option.bool(PREFIX, "compat.rhino.enabled", Category.COMPATIBILITY, true, "Enable Mozilla Rhino compatibility extensions");
    public static final Option<Boolean> COMPATIBILITY_COMMONJS = Option.bool(PREFIX, "compat.commonjs.enabled", Category.COMPATIBILITY, true, "Enable commonjs compatibility extensions");
    public static final Option<Boolean> COMPATIBILITY_V8 = Option.bool(PREFIX, "compat.v8.enabled", Category.COMPATIBILITY, true, "Enable v8 compatibility extensions");


    public static enum Category {
        COMPILER("compiler"),
        KERNEL("kernel"),
        COMPATIBILITY("compatibility"),
        INVOKEDYNAMIC("invokedynamic"),
        JIT("jit");

        private final String desc;

        Category(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return desc;
        }

        public String desc() {
            return desc;
        }
    }

    public static final Collection<Option> PROPERTIES = Collections.unmodifiableCollection(Arrays.<Option>asList(CLI_COMPILE_MODE, CLI_KERNEL_MODE, JIT_TRESHOLD, JIT, JIT_ASYNC, INVOKEDYNAMIC, COMPATIBILITY_RHINO, COMPATIBILITY_COMMONJS, COMPATIBILITY_V8));
}
