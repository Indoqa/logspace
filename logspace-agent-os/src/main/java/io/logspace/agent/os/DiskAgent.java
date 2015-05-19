/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os;

import io.logspace.agent.api.AbstractAgent;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;
import io.logspace.agent.impl.AgentControllerProvider;
import io.logspace.agent.os.api.OsEventBuilder;

import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DiskAgent extends AbstractAgent {

    private DiskAgent(String agentId) {
        super(agentId, "os/disk", TriggerType.Off, TriggerType.Cron);

        this.setAgentController(AgentControllerProvider.getAgentController());
    }

    public static DiskAgent create(String agentId) {
        return new DiskAgent(agentId);
    }

    @Override
    public void execute(AgentOrder agentOrder) {
        for (Path root : FileSystems.getDefault().getRootDirectories()) {
            this.sendDiskEvent(root);
        }
    }

    private void sendDiskEvent(Path root) {
        try {
            FileStore store = Files.getFileStore(root);

            OsEventBuilder eventBuilder = OsEventBuilder.createDiskBuilder(this.getId(), this.getSystem());

            eventBuilder.setDiskPath(root.toString());
            eventBuilder.setTotalDiskSpace(store.getTotalSpace());
            eventBuilder.setUsableDiskSpace(store.getUsableSpace());
            eventBuilder.setUnallocatedDiskSpace(store.getUnallocatedSpace());
            eventBuilder.setUsedDiskSpace(store.getTotalSpace() - store.getUnallocatedSpace());

            this.sendEvent(eventBuilder.toEvent());
        } catch (Exception e) {
            // ignore
        }
    }
}
