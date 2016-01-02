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
