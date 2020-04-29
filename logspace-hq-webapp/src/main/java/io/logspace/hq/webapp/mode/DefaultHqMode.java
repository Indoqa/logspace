/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp.mode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.indoqa.lang.exception.InitializationFailedException;

import io.logspace.hq.core.api.orders.Order;
import io.logspace.hq.core.api.orders.OrderService;
import io.logspace.hq.core.api.spaces.SpacesService;

public class DefaultHqMode implements HqMode {

    private static final String DIRECTORY_ORDERS = "orders";
    private static final String DIRECTORY_SPACES = "spaces";

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHqMode.class);

    private static Order loadOrder(Path path) {
        try {
            Order order = new Order();

            order.setId(StringUtils.substringBeforeLast(path.getFileName().toString(), ".json"));
            order.setLastModified(new Date());
            String content = Files.readString(path, StandardCharsets.UTF_8);
            order.setContent(content);

            return order;
        } catch (IOException e) {
            throw new InitializationFailedException("Could load order from '" + path + "'.", e);
        }
    }

    @Override
    public void didInitialize(ApplicationContext applicationContext) {
        String property = applicationContext.getEnvironment().getProperty("logspace.hq-webapp.load-config-from");
        if (property == null) {
            return;
        }

        Path directory = Paths.get(property).toAbsolutePath().normalize();
        if (Files.notExists(directory)) {
            throw new InitializationFailedException(
                "Cannot load configurations from '" + property + "'. The directory does not exist.");
        }

        this.loadOrders(applicationContext, directory);
        this.loadSpaces(applicationContext, directory);
    }

    @Override
    public void willInitialize() {
        // default does nothing
    }

    private void loadOrders(ApplicationContext applicationContext, Path directory) {
        Path ordersDirectory = directory.resolve(DIRECTORY_ORDERS);
        if (Files.notExists(ordersDirectory)) {
            throw new InitializationFailedException("Missing directory '" + DIRECTORY_ORDERS + "' in '" + directory + "'.");
        }

        LOGGER.info("Loading orders from '{}'", ordersDirectory);
        OrderService orderService = applicationContext.getBean(OrderService.class);

        try (Stream<Path> stream = Files.list(ordersDirectory)) {
            List<Path> paths = stream.filter(path -> path.toString().endsWith(".json")).collect(Collectors.toList());

            for (Path eachPath : paths) {
                LOGGER.info("Loading order from '{}'.", eachPath);
                Order order = loadOrder(eachPath);
                orderService.storeOrder(order);
            }
        } catch (IOException e) {
            throw new InitializationFailedException("Could not load order from '" + directory + "'.", e);
        }
    }

    private void loadSpaces(ApplicationContext applicationContext, Path directory) {
        Path spacesDirectory = directory.resolve(DIRECTORY_SPACES);
        if (Files.notExists(spacesDirectory)) {
            throw new InitializationFailedException("Missing directory '" + DIRECTORY_SPACES + "' in '" + directory + "'.");
        }

        LOGGER.info("Loading spaces from '{}'", spacesDirectory);
        SpacesService service = applicationContext.getBean(SpacesService.class);

        try (Stream<Path> stream = Files.list(spacesDirectory)) {
            List<Path> paths = stream.filter(path -> path.toString().endsWith(".space")).collect(Collectors.toList());

            for (Path eachPath : paths) {
                LOGGER.info("Loading space from '{}'.", eachPath);
                String space = StringUtils.substringBeforeLast(eachPath.getFileName().toString(), ".space");
                List<String> tokens = Files.readAllLines(eachPath);
                service.setAuthenticationTokens(space, tokens.toArray(String[]::new));
            }
        } catch (IOException e) {
            throw new InitializationFailedException("Could not load order from '" + directory + "'.", e);
        }
    }
}
