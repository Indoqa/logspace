/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static java.util.concurrent.TimeUnit.DAYS;
import io.logspace.agent.api.event.Optional;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class RandomHelper {

    private static final Random RANDOM = new Random();

    public static boolean getRandomBoolean() {
        return RANDOM.nextBoolean();
    }

    public static int getRandomCount(int max) {
        return RANDOM.nextInt(max);
    }

    public static Date getRandomDate() {
        long time = (long) (System.currentTimeMillis() - getRandomDouble() * DAYS.toMillis(3650));

        // set the milliseconds to 0
        time = time / 1000 * 1000;

        return new Date(time);
    }

    public static double getRandomDouble() {
        return RANDOM.nextDouble();
    }

    public static <T extends Enum<T>> T getRandomEnumValue(Class<T> enumType) {
        T[] values = enumType.getEnumConstants();

        int index = getRandomCount(values.length);
        return values[index];
    }

    public static float getRandomFloat() {
        return RANDOM.nextFloat();
    }

    public static int getRandomInt() {
        return RANDOM.nextInt();
    }

    public static long getRandomLong() {
        return RANDOM.nextLong();
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
