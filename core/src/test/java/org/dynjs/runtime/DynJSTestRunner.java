/**
 *  Copyright 2011 Douglas Campos
 *  Copyright 2011 dynjs contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.runtime;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.internal.matchers.Equals;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynJSTestRunner extends Runner {

    private DynJS dynJS = new DynJS();

    private final Class<?> testClass;
    private List<String> files = new ArrayList<>();

    public DynJSTestRunner(Class<?> testClass) {
        this.testClass = testClass;
        init();
    }

    @Override
    public Description getDescription() {
        final Description description = Description.createSuiteDescription(testClass);
        for (String file : files) {
            description.addChild(Description.createTestDescription(testClass, file));
        }
        return description;
    }

    private void init() {
        final URL resource = testClass.getResource("");
        if (resource.getProtocol().equals("file")) {
            try {
                final String[] files = new File(resource.toURI()).list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.matches(".*\\.js$");
                    }
                });
                this.files.addAll(Arrays.asList(files));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        for (String file : files) {
            final Description description = Description.createTestDescription(testClass, file);
            notifier.fireTestStarted(description);
            final Boolean result;
            try {
                DynThreadContext context = new DynThreadContext();
                final InputStream stream = testClass.getResourceAsStream(file);
                dynJS.eval(context, stream);
                result = (Boolean) context.getScope().resolve("result");
                if (result) {
                    notifier.fireTestFinished(description);
                } else {
                    notifier.fireTestFailure(new Failure(description, new AssumptionViolatedException(result, new Equals(true))));
                }
            } catch (Exception e) {
                notifier.fireTestFailure(new Failure(description, e));
            }
        }
    }
}
