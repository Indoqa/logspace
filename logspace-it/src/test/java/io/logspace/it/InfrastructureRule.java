/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.it;

import io.logspace.hq.webapp.LogspaceHq;

import java.lang.reflect.Field;

import org.apache.solr.client.solrj.SolrServer;
import org.junit.rules.ExternalResource;
import org.springframework.context.ApplicationContext;

public class InfrastructureRule extends ExternalResource {

    private boolean initialized;

    private LogspaceHq logspaceHq;

    private SolrServer solrServer;

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

    public SolrServer getSolrServer() {
        return this.solrServer;
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
        System.setProperty("log-path", "./target");
        System.setProperty("logspace.solr.base-url", "file://./target/solr");

        this.logspaceHq = new LogspaceHq();
        this.logspaceHq.invoke();

        this.solrServer = this.getSpringBean(SolrServer.class);

        this.initialized = true;
    }
}