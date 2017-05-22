/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.it;

import static com.indoqa.lang.util.FileUtils.getCanonicalFile;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.junit.rules.ExternalResource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.logspace.hq.core.api.orders.OrderService;
import io.logspace.hq.core.api.report.ReportService;
import io.logspace.hq.core.api.spaces.SpacesService;
import io.logspace.hq.core.solr.ConfigQualifier;
import io.logspace.hq.core.solr.EventQualifier;
import io.logspace.hq.webapp.LogspaceHq;
import io.logspace.hq.webapp.mode.DemoHqMode;

public class InfrastructureRule extends ExternalResource {

    public static final int TEST_PORT = 4568;

    private boolean initialized;

    private LogspaceHq logspaceHq;

    @Inject
    @EventQualifier
    private SolrClient eventSolrClient;

    @Inject
    @ConfigQualifier
    private SolrClient configSolrClient;

    @Inject
    private OrderService orderService;

    @Inject
    private SpacesService spacesService;

    @Inject
    private ReportService reportService;

    private static void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (Exception e) {
            throw new LogspaceTestException("Error while deleting file '" + path + "'.", e);
        }
    }

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

    public final SolrClient getConfigSolrClient() {
        return this.configSolrClient;
    }

    public final SolrClient getEventSolrClient() {
        return this.eventSolrClient;
    }

    public final OrderService getOrderService() {
        return this.orderService;
    }

    public final ReportService getReportService() {
        return this.reportService;
    }

    public final SpacesService getSpacesService() {
        return this.spacesService;
    }

    @Override
    protected void before() throws Throwable {
        if (this.initialized) {
            return;
        }

        this.initializeHQ();
    }

    private void initializeHQ() throws Exception {
        File solrDataDirectory = getCanonicalFile(new File("./target/solr"));
        FileUtils.deleteDirectory(solrDataDirectory);

        Path capabilitiesDirectory = Paths.get("./capabilities");
        Files.createDirectories(capabilitiesDirectory);
        Files.newDirectoryStream(capabilitiesDirectory, "*.json").forEach(InfrastructureRule::deleteFile);

        System.setProperty("port", String.valueOf(TEST_PORT));
        System.setProperty("admin.separate-service", "false");
        System.setProperty("log-path", "./target");
        System.setProperty("spring.profiles.active", "prod,test");

        this.logspaceHq = new LogspaceHq();
        this.logspaceHq.invoke();

        this.injectDependencies();

        this.prepareConfig();

        this.initialized = true;
    }

    private void injectDependencies() throws IllegalAccessException {
        Field field = getField(this.logspaceHq.getClass(), "context");
        field.setAccessible(true);
        AnnotationConfigApplicationContext applicationContext = (AnnotationConfigApplicationContext) field.get(this.logspaceHq);

        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    private void prepareConfig() {
        this.orderService.storeOrder(DemoHqMode.loadOrder("/orders/", "1"));
        this.spacesService.setAuthenticationTokens("test", "test");
    }
}
