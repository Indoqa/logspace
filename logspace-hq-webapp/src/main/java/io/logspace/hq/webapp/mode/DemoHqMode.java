/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp.mode;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;

import com.indoqa.lang.exception.InitializationFailedException;

import io.logspace.agent.api.util.ConsoleWriter;
import io.logspace.hq.core.api.orders.Order;
import io.logspace.hq.core.api.orders.OrderService;
import io.logspace.hq.core.api.spaces.SpacesService;

public class DemoHqMode implements HqMode {

    private static final String SPARK_DEFAULT_PORT = "4567";

    public DemoHqMode() {
        super();

        this.initializeDemoLogging();
    }

    public static Order loadOrder(String path, String controllerId) {
        try {
            Order order = new Order();

            order.setId("order_" + controllerId);
            order.setLastModified(new Date());
            order.setContent(IOUtils.toString(DemoHqMode.class.getResource(path + controllerId + ".json"), "UTF-8"));

            return order;
        } catch (IOException e) {
            throw new InitializationFailedException("Could not store demo order.", e);
        }
    }

    @Override
    public void afterInitialization(ApplicationContext applicationContext) {
        OrderService orderService = applicationContext.getBean(OrderService.class);
        orderService.storeOrder(loadOrder("/demo/orders/", "logspace-demo"));

        SpacesService spacesService = applicationContext.getBean(SpacesService.class);
        spacesService.setAuthenticationTokens("demo", "demo");

        ConsoleWriter.write("Logspace HQ now running in demo mode");
        ConsoleWriter.write("Go to http://localhost:" + SPARK_DEFAULT_PORT);
        ConsoleWriter.write("");
    }

    @Override
    public void beforeInitialization() {
        this.initializeDemoPort();
        this.initializeDemoSolr();
    }

    private File getBaseDir() {
        return new File(System.getProperty("java.io.tmpdir"), "logspace-demo");
    }

    private void initializeDemoLogging() {
        File logDir = new File(this.getBaseDir(), "logs");
        logDir.mkdirs();

        System.setProperty("log-path", logDir.getAbsolutePath());
        System.setProperty("log4j.configurationFile", "log4j2-demo.xml");
    }

    private void initializeDemoPort() {
        System.setProperty("port", String.valueOf(SPARK_DEFAULT_PORT));
    }

    private void initializeDemoSolr() {
        System.setProperty("logspace.solr-events.base-url", new File(this.getBaseDir(), "solr-events").toURI().toString());
        System.setProperty("logspace.solr-config.base-url", new File(this.getBaseDir(), "solr-config").toURI().toString());
    }
}
