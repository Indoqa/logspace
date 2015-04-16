/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os;

import io.logspace.agent.api.AbstractAgent;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.order.TriggerType;
import io.logspace.agent.os.api.CpuEventBuilder;

public class CpuAgent extends AbstractAgent {

    public CpuAgent() {
        super();

        this.updateCapabilities(TriggerType.Off, TriggerType.Cron);
    }

    public void someMethodSendingAnEvent() {
        Event event = new CpuEventBuilder(this.getId()).setLoadAverage(3.2).toEvent();
        this.sendEvent(event);
    }
}