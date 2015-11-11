/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.impl;

import static java.nio.file.FileVisitResult.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import io.logspace.agent.api.json.AgentControllerCapabilitiesJsonDeserializer;
import io.logspace.agent.api.json.AgentControllerCapabilitiesJsonSerializer;
import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.order.PropertyDescription;
import io.logspace.hq.core.api.capabilities.CapabilitiesService;
import io.logspace.hq.core.api.model.IdHelper;
import io.logspace.hq.rest.api.suggestion.AgentDescription;

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
        Path capabilitiesDir = Paths.get(this.capabilitiesDirectory);
        this.logger.info("Loading capabilities files from '{}'.", capabilitiesDir.toAbsolutePath().toString());
        Files.walkFileTree(capabilitiesDir, new LoadCapabilitiesFileVisitor());
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
                String globalAgentId = this.getGlobalAgentId(capabilities.getSpace(), capabilities.getSystem(),
                    eachAgentCapabilities.getId());

                AgentDescription agentDescription = new AgentDescription();
                agentDescription.setGlobalId(globalAgentId);
                agentDescription.setName(eachAgentCapabilities.getId());
                agentDescription.setSystem(capabilities.getSystem());
                agentDescription.setSpace(capabilities.getSpace());
                agentDescription.setPropertyDescriptions(this.asSortedList(eachAgentCapabilities));

                this.agentDescriptions.put(globalAgentId, agentDescription);

                oldGlobalAgentIds.remove(globalAgentId);
            }
        }

        for (String eachOldGlobalAgentId : oldGlobalAgentIds) {
            this.agentDescriptions.remove(eachOldGlobalAgentId);
        }
    }

    private List<PropertyDescription> asSortedList(AgentCapabilities eachAgentCapabilities) {
        List<PropertyDescription> propertyDescriptions = Arrays.asList(eachAgentCapabilities.getPropertyDescriptions());
        Collections.sort(propertyDescriptions);
        return propertyDescriptions;
    }

    private Set<String> getGlobalAgentIds(AgentControllerCapabilities capabilities) {
        if (capabilities == null || !capabilities.hasAgentCapabilities()) {
            return Collections.emptySet();
        }

        Set<String> result = new HashSet<>();

        for (AgentCapabilities eachOldAgentCapabilities : capabilities.getAgentCapabilities()) {
            result.add(this.getGlobalAgentId(capabilities.getSpace(), capabilities.getSystem(), eachOldAgentCapabilities.getId()));
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
