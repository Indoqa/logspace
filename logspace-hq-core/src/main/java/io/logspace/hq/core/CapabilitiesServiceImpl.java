/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import io.logspace.agent.api.json.AgentControllerCapabilitiesJsonDeserializer;
import io.logspace.agent.api.json.AgentControllerCapabilitiesJsonSerializer;
import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.hq.core.api.AgentDescription;
import io.logspace.hq.core.api.CapabilitiesService;
import io.logspace.hq.core.api.IdHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@Named
public class CapabilitiesServiceImpl implements CapabilitiesService {

    private static final String CAPABILITIES_FILE_EXTENSION = ".json";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${logspace.hq-webapp.capabilities-directory}")
    private String capabilitiesDirectory;

    private final Map<String, AgentControllerCapabilities> agentControllerCapabilities = new ConcurrentHashMap<>();

    private final Map<String, AgentDescription> agentDescriptions = new ConcurrentHashMap<>();

    @Override
    public AgentDescription getAgentDescription(String globalAgentId) {
        return this.agentDescriptions.get(globalAgentId);
    }

    @Override
    public String getAgentId(String globalAgentId) {
        return IdHelper.getAgentId(globalAgentId);
    }

    public AgentControllerCapabilities getCapabilities(String agentControllerId) {
        return this.agentControllerCapabilities.get(agentControllerId);
    }

    @Override
    public String getGlobalAgentId(String space, String system, String agentId) {
        return IdHelper.getGlobalAgentId(space, system, agentId);
    }

    @PostConstruct
    public void initialize() throws IOException {
        this.logger.info("Loading capabilities files from '{}'.", this.capabilitiesDirectory);

        Files.walkFileTree(Paths.get(this.capabilitiesDirectory), new LoadCapabilitiesFileVisitor());
    }

    @Override
    public void save(AgentControllerCapabilities capabilities) throws IOException {
        File capabilitiesFile = new File(this.capabilitiesDirectory, capabilities.getId() + CAPABILITIES_FILE_EXTENSION);
        try (OutputStream outputStream = new FileOutputStream(capabilitiesFile)) {
            AgentControllerCapabilitiesJsonSerializer.toJson(capabilities, outputStream);
        }

        this.addCapabilities(capabilities);
    }

    protected void loadCapabilities(Path path) throws IOException {
        this.addCapabilities(AgentControllerCapabilitiesJsonDeserializer.fromJson(Files.newInputStream(path)));
    }

    private void addCapabilities(AgentControllerCapabilities capabilities) {
        AgentControllerCapabilities oldCapabilities = this.agentControllerCapabilities.put(capabilities.getId(), capabilities);

        Set<String> oldGlobalAgentIds = this.getGlobalAgentIds(oldCapabilities);

        if (capabilities.hasAgentCapabilities()) {
            for (AgentCapabilities eachAgentCapabilities : capabilities.getAgentCapabilities()) {
                String globalAgentId = this.getGlobalAgentId(capabilities.getSpace().get(), capabilities.getSystem(),
                        eachAgentCapabilities.getId());

                AgentDescription agentDescription = new AgentDescription();
                agentDescription.setGlobalId(globalAgentId);
                agentDescription.setName(eachAgentCapabilities.getId());
                agentDescription.setSystem(capabilities.getSystem());
                agentDescription.setSpace(capabilities.getSpace().get());
                agentDescription.setPropertyDescriptions(Arrays.asList(eachAgentCapabilities.getPropertyDescriptions()));

                this.agentDescriptions.put(globalAgentId, agentDescription);

                oldGlobalAgentIds.remove(globalAgentId);
            }
        }

        for (String eachOldGlobalAgentId : oldGlobalAgentIds) {
            this.agentDescriptions.remove(eachOldGlobalAgentId);
        }
    }

    private Set<String> getGlobalAgentIds(AgentControllerCapabilities capabilities) {
        if (capabilities == null || !capabilities.hasAgentCapabilities()) {
            return Collections.emptySet();
        }

        Set<String> result = new HashSet<>();

        for (AgentCapabilities eachOldAgentCapabilities : capabilities.getAgentCapabilities()) {
            result.add(this.getGlobalAgentId(capabilities.getSpace().get(), capabilities.getSystem(), eachOldAgentCapabilities.getId()));
        }

        return result;
    }

    private class LoadCapabilitiesFileVisitor extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (attrs.isDirectory()) {
                return SKIP_SUBTREE;
            }

            if (!file.getFileName().toString().equalsIgnoreCase(CAPABILITIES_FILE_EXTENSION)) {
                return CONTINUE;
            }

            CapabilitiesServiceImpl.this.loadCapabilities(file);

            return CONTINUE;
        }
    }
}
