/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import io.logspace.agent.api.event.Optional;

import java.util.Random;
import java.util.UUID;

public class RandomHelper {

    private static final Random RANDOM = new Random();

    public static int getRandomCount(int max) {
        return RANDOM.nextInt(max);
    }

    public static Optional<String> getRandomOptional() {
        if (RANDOM.nextBoolean()) {
            return Optional.empty();
        }

        return Optional.of(getRandomString());
    }

    public static String getRandomString() {
        return UUID.randomUUID().toString();
    }
}
