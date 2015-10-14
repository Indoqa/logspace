/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.it;

import static com.indoqa.commons.lang.util.FileUtils.getCanonicalFile;

import java.io.File;
import java.lang.reflect.Field;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.junit.rules.ExternalResource;
import org.springframework.context.ApplicationContext;

import io.logspace.hq.webapp.LogspaceHq;

public class InfrastructureRule extends ExternalResource {

    public static final int TEST_PORT = 4568;

    private boolean initialized;

    private LogspaceHq logspaceHq;

    private SolrClient solrClient;

    private static Field getField(Class<?> type, String fieldName) {
        Class<?> currentType = type;

        while (currentType != null) {
            try {
                return currentType.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // nothing to do
            } catch (SecurityException e) {
                e.printStackTrace();
            }

            currentType = currentType.getSuperclass();
        }

        return null;
    }

    public SolrClient getSolrClient() {
        return this.solrClient;
    }

    @Override
    protected void before() throws Throwable {
        if (this.initialized) {
            return;
        }

        this.initializeHQ();
    }

    private <T> T getSpringBean(Class<T> beanType) throws IllegalAccessException {
        Field field = getField(this.logspaceHq.getClass(), "context");
        field.setAccessible(true);
        ApplicationContext applicationContext = (ApplicationContext) field.get(this.logspaceHq);

        return applicationContext.getBean(beanType);
    }

    private void initializeHQ() throws Exception {
        File solrDataDirectory = getCanonicalFile(new File("./target/solr"));
        FileUtils.deleteDirectory(solrDataDirectory);

        File capabilitiesDirectory = new File("./target/capabilities");
        capabilitiesDirectory.mkdirs();

        System.setProperty("port", String.valueOf(TEST_PORT));
        System.setProperty("log-path", "./target");
        System.setProperty("spring.profiles.active", "prod,test");

        this.logspaceHq = new LogspaceHq();
        this.logspaceHq.invoke();

        this.solrClient = this.getSpringBean(SolrClient.class);

        this.initialized = true;
    }
}
