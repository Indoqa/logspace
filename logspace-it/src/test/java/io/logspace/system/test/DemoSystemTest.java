/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.system.test;

import static com.indoqa.system.test.tools.JarRunnerUtils.*;
import static io.logspace.system.SystemTestUtils.sleep;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.ClassRule;
import org.junit.Test;

import com.indoqa.system.test.tools.JarRunner;
import com.indoqa.system.test.tools.JarRunnerBuilder;

import io.logspace.system.SolrClientResource;
import io.logspace.system.SystemTestUtils;

public class DemoSystemTest {

    private static final String PORT = "4567";
    private static final String BASE_URL = "http://localhost:" + PORT;

    private static final Path HQ_RUNNABLE = searchJavaRunnable(Paths.get("../logspace-hq-webapp/target/"), endsWithRunnableJar());
    private static final Path MONITOR_RUNNABLE = searchJavaRunnable(Paths.get("../logspace-monitor/target/"), endsWithRunnableJar());

    @ClassRule
    public static final JarRunner HQ_RUNNER = new JarRunnerBuilder(HQ_RUNNABLE)
        .preInitialization(SystemTestUtils::prepareDemoDirectory)
        .setCheckAdress(BASE_URL + "/system-info")
        .addArguments("--demo")
        .build();

    @ClassRule
    public static final JarRunner MONITOR_RUNNER = new JarRunnerBuilder(MONITOR_RUNNABLE)
        .setAlwaysWait(1000)
        .addArgument("--demo")
        .build();

    @ClassRule
    public static final SolrClientResource SOLR_CLIENT = new SolrClientResource();

    @Test
    public void test() {
        sleep(30);
        SOLR_CLIENT.assertSolrNumFound("Expected at least 4 events.", BASE_URL, numFound -> numFound >= 4);
    }
}
