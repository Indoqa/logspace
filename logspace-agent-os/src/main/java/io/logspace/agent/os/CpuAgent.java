package io.logspace.agent.os;

import io.logspace.agent.os.api.CpuEventBuilder;
import io.logspace.passive.agent.api.PassiveAgent;
import io.logspace.passive.agent.api.PassiveController;
import io.logspace.passive.agent.api.event.Event;

public class CpuAgent implements PassiveAgent {

    private PassiveController passiveController;

    @Override
    public void setPassiveController(PassiveController passiveController) {
        this.passiveController = passiveController;
    }

    public void someMethodSendingAnEvent() {
        Event event = new CpuEventBuilder().setLoadAverage(3.2).toEvent();
        this.passiveController.send(event);
    }
}
