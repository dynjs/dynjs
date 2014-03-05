package org.dynjs.cli;

import com.headius.options.Option;
import org.dynjs.Config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Options {
    public static final String PREFIX = "dynjs";
    public static final Option<Config.CompileMode> CLI_COMPILE_MODE = Option.enumeration(PREFIX, "compile.mode", Category.COMPILER, Config.CompileMode.JIT, "Set compile mode: OFF = no compilation (interpreted); JIT = at runtime; FORCE = before execution");

    public static enum Category {
        COMPILER("compiler");

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

    public static final Collection<Option> PROPERTIES = Collections.unmodifiableCollection(Arrays.<Option>asList(CLI_COMPILE_MODE));
}
