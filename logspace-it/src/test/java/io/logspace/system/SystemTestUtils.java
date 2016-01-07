/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.system;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class SystemTestUtils {

    public static void prepareDemoDirectory() {
        try {
            File directory = new File(System.getProperty("java.io.tmpdir"), "logspace-demo");
            directory.mkdirs();
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            // do nothing (cleanDirectory may fail on Windows systems because the directory could be in use by an old process)
        }
    }

    public static void sleep(long seconds) {
        try {
            SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
