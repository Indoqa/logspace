package io.logspace.system;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import io.logspace.it.LogspaceTestException;

public class SystemTestUtils {

    public static void prepareDemoDirectory() {
        try {
            File directory = new File(System.getProperty("java.io.tmpdir"), "logspace-demo");
            directory.mkdirs();
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            throw new LogspaceTestException("Error while cleaning Logspace demo directory.", e);
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
