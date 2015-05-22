/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os;

import io.logspace.agent.api.order.AgentOrder;

import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DiskAgent extends AbstractOsAgent {

    public static final String TYPE = "os/disk";

    private DiskAgent() {
        super(TYPE);
    }

    public static DiskAgent create() {
        return new DiskAgent();
    }

    @Override
    public void execute(AgentOrder agentOrder) {
        for (Path eachRoot : FileSystems.getDefault().getRootDirectories()) {
            this.sendDiskEvent(eachRoot);
        }
    }

    private void sendDiskEvent(Path root) {
        try {
            FileStore store = Files.getFileStore(root);

            long totalSpace = store.getTotalSpace();
            long usableSpace = store.getUsableSpace();
            long unallocatedSpace = store.getUnallocatedSpace();

            OsEventBuilder eventBuilder = OsEventBuilder.createDiskBuilder(this.getId(), this.getSystem());

            eventBuilder.setDiskPath(root.toString());
            eventBuilder.setTotalDiskSpace(totalSpace);
            eventBuilder.setUsableDiskSpace(usableSpace);
            eventBuilder.setUnallocatedDiskSpace(unallocatedSpace);
            eventBuilder.setUsedDiskSpace(totalSpace - unallocatedSpace);

            this.sendEvent(eventBuilder.toEvent());
        } catch (Exception e) {
            // ignore
        }
    }
}
