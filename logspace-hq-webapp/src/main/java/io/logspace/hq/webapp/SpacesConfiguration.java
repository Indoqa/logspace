/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.indoqa.commons.lang.util.FileUtils;

@Configuration
@PropertySource("classpath:/io/logspace/hq/webapp/logspace-hq-webapp.properties")
public class SpacesConfiguration {

    @Value("${logspace.hq-webapp.space-tokens-directory}")
    private String spaceTokensDirectory;

    @Bean
    public Spaces createSpaces() throws IOException {
        File file = FileUtils.getCanonicalFile(new File(this.spaceTokensDirectory));

        SpaceTokensFileVisitor visitor = new SpaceTokensFileVisitor();
        Files.walkFileTree(file.toPath(), visitor);

        SpacesImpl spaces = new SpacesImpl();
        spaces.setSpaceTokens(visitor.getSpaceTokens());
        return spaces;
    }

    private static class SpaceTokensFileVisitor extends SimpleFileVisitor<Path> {

        private static final String SPACE_CONFIGURATION_FILE_EXTENSION = ".space-tokens";

        private final Map<String, String> spaceTokens = new ConcurrentHashMap<String, String>();

        public Map<String, String> getSpaceTokens() {
            return this.spaceTokens;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            String fileName = file.getFileName().toString();
            if (!fileName.endsWith(SPACE_CONFIGURATION_FILE_EXTENSION)) {
                return CONTINUE;
            }

            String spaceName = fileName.substring(0, fileName.length() - SPACE_CONFIGURATION_FILE_EXTENSION.length());
            Files.lines(file).forEach((line) -> this.processLine(line, spaceName));

            return CONTINUE;
        }

        private void processLine(String line, String space) {
            String trimmedLine = StringUtils.trimToEmpty(line);

            if (trimmedLine.length() == 0 || trimmedLine.charAt(0) == '#') {
                return;
            }

            this.spaceTokens.put(trimmedLine, space);
        }
    }
}