/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.scheduling;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import io.logspace.agent.api.AgentControllerException;
import io.logspace.agent.api.AgentControllerInitializationException;
import io.logspace.agent.api.order.AgentControllerOrder;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentScheduler {

    private static final long UPDATE_START_DELAY = 1000;

    private static final String KEY_AGENT_ORDER = "agent-order";
    private static final String KEY_AGENT_EXECUTOR = "agent-executor";

    private static final String LOGSPACE_SCHEDULER_GROUP = "logspace";
    private static final String AGENT_SCHEDULER_GROUP = "logspace-agents";

    private Scheduler scheduler;

    private final Map<String, AgentOrder> agentOrders = new HashMap<String, AgentOrder>();

    private AgentExecutor agentExecutor;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AgentScheduler(AgentExecutor agentExecutor, int updateInterval) {
        super();

        this.agentExecutor = agentExecutor;

        this.initializeQuartzScheduler();
        this.initializeUpdateJob(updateInterval);
    }

    private static boolean isShaded() {
        try {
            Class.forName("io.logspace.agent.shaded.quartz.simpl.SimpleThreadPool");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public void applyOrder(AgentControllerOrder agentControllerOrder, Collection<String> agentIds) {
        this.logger.info("Applying new AgentControllerOrder");

        this.clearAgentOrders();

        for (AgentOrder eachAgentOrder : agentControllerOrder.getAgentOrders()) {
            String agentId = eachAgentOrder.getId();

            this.agentOrders.put(agentId, eachAgentOrder);

            if (eachAgentOrder.getTriggerType() == null) {
                this.logger.error("Found order for agent with ID '{}' that has no trigger type. "
                        + "This agent will not be able to produce any events!", agentId);
                continue;
            }

            if (eachAgentOrder.getTriggerType() == TriggerType.Scheduler) {
                if (!agentIds.contains(agentId)) {
                    this.logger.warn("Cannot schedule agent with ID '{}' because no such agent exists!", agentId);
                    continue;
                }

                this.scheduleAgentOrder(eachAgentOrder);
            }
        }
    }

    public void clearAgentOrders() {
        this.logger.info("Cancelling schedules for all agents.");

        try {
            Set<TriggerKey> triggerKeys = this.scheduler.getTriggerKeys(GroupMatcher.<TriggerKey> groupEquals(AGENT_SCHEDULER_GROUP));
            for (TriggerKey eachTriggerKey : triggerKeys) {
                this.scheduler.unscheduleJob(eachTriggerKey);
            }

            this.agentOrders.clear();
        } catch (SchedulerException e) {
            throw new AgentControllerException("Failed to clear AgentOrders.", e);
        }
    }

    public AgentOrder getAgentOrder(String agentId) {
        return this.agentOrders.get(agentId);
    }

    public void stop() {
        this.logger.info("Stopping now.");

        try {
            this.scheduler.shutdown(true);
        } catch (SchedulerException e) {
            this.logger.error("Failed to shutdown scheduler.", e);
        }
    }

    private void initializeQuartzScheduler() {
        if (this.scheduler != null) {
            return;
        }

        InputStream resourceStream;
        if (isShaded()) {
            resourceStream = AgentScheduler.class.getResourceAsStream("/logspace-shaded-quartz.properties");
        } else {
            resourceStream = AgentScheduler.class.getResourceAsStream("/logspace-quartz.properties");
        }

        try {
            StdSchedulerFactory factory = new StdSchedulerFactory();
            factory.initialize(resourceStream);
            this.scheduler = factory.getScheduler();
            this.scheduler.start();
        } catch (SchedulerException e) {
            throw new AgentControllerInitializationException("Error while creating and starting a Quartz scheduler.", e);
        } finally {
            try {
                resourceStream.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    private void initializeUpdateJob(int updateInterval) {
        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(KEY_AGENT_EXECUTOR, this.agentExecutor);
            JobDetail job = newJob(UpdateJob.class).withIdentity("update", LOGSPACE_SCHEDULER_GROUP).usingJobData(jobDataMap).build();

            Trigger trigger = newTrigger().withIdentity("update-trigger", LOGSPACE_SCHEDULER_GROUP)
                    .startAt(new Date(System.currentTimeMillis() + UPDATE_START_DELAY))
                    .withSchedule(simpleSchedule().withIntervalInSeconds(updateInterval).repeatForever()).build();

            this.scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new AgentControllerInitializationException("Error while scheduling update job.", e);
        }
    }

    private void scheduleAgentOrder(AgentOrder agentOrder) {
        if (agentOrder.getTriggerParameter().isPresent()) {
            this.logger.info("Scheduling order for agent '{}' with trigger '{}' and parameter '{}'.", new Object[] {
                    agentOrder.getId(), agentOrder.getTriggerType(), agentOrder.getTriggerParameter().get()});
        } else {
            this.logger.info("Scheduling order for agent '{}' with trigger '{}'.", agentOrder.getId(), agentOrder.getTriggerType());
        }

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(KEY_AGENT_EXECUTOR, this.agentExecutor);
        jobDataMap.put(KEY_AGENT_ORDER, agentOrder);

        JobDetail job = newJob(ScheduledAgentExecutionJob.class).withIdentity(agentOrder.getId(), AGENT_SCHEDULER_GROUP)
                .usingJobData(jobDataMap).build();

        Trigger trigger = newTrigger().withIdentity(agentOrder.getId() + "-trigger", AGENT_SCHEDULER_GROUP).startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(agentOrder.getTriggerParameter().get())).build();

        try {
            this.scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new AgentControllerException("Failed to schedule AgentOrder.", e);
        }
    }

    @DisallowConcurrentExecution
    public static class ScheduledAgentExecutionJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

            AgentExecutor agentExecutor = (AgentExecutor) jobDataMap.get(KEY_AGENT_EXECUTOR);
            AgentOrder agentOrder = (AgentOrder) jobDataMap.get(KEY_AGENT_ORDER);

            agentExecutor.executeScheduledAgent(agentOrder);
        }
    }

    public static class UpdateJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

            AgentExecutor agentExecutor = (AgentExecutor) jobDataMap.get(KEY_AGENT_EXECUTOR);
            agentExecutor.update();
        }
    }
}
