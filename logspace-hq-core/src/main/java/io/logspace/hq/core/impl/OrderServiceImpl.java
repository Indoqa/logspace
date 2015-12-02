/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import io.logspace.hq.core.api.orders.OrderService;
import io.logspace.hq.core.api.orders.StoredOrder;

@Named
public class OrderServiceImpl implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${logspace.hq-webapp.data-directory}")
    private String dataDirectory;

    private Path ordersPath;

    @Override
    public StoredOrder getStoredOrder(String controllerId) {
        Path path = this.getOrderPath(controllerId);
        if (!Files.exists(path)) {
            return null;
        }

        return new PathStoredOrder(path);
    }

    @PostConstruct
    public void initialize() {
        this.ordersPath = Paths.get(this.dataDirectory, "orders");
        this.logger.info("Using '{}' as orders directory.", this.ordersPath.toAbsolutePath());
    }

    private Path getOrderPath(String controllerId) {
        return this.ordersPath.resolve(controllerId + ".json");
    }

    private class PathStoredOrder implements StoredOrder {

        private final Path path;

        private PathStoredOrder(Path path) {
            this.path = path;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return Files.newInputStream(this.path);
        }

        @Override
        public long getLastModified() throws IOException {
            return Files.getLastModifiedTime(this.path).toMillis();
        }

        @Override
        public boolean isNotModifiedSince(Date date) throws IOException {
            if (date == null) {
                return false;
            }

            return date.getTime() / 1000 == this.getLastModified() / 1000;
        }
    }
}
