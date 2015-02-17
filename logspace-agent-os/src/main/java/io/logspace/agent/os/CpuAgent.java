package io.logspace.agent.os;

import io.logspace.agent.os.api.CpuEventBuilder;
import io.logspace.passive.agent.api.Event;
import io.logspace.passive.controller.PassiveAgent;
import io.logspace.passive.controller.PassiveController;

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
