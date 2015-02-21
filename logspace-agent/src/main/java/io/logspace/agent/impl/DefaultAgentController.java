package io.logspace.agent.impl;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import io.logspace.agent.api.Agent;
import io.logspace.agent.api.AgentController;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.EventJsonSerializer;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultAgentController implements AgentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Collection<Agent> agents = Collections.synchronizedSet(new HashSet<Agent>());
    private CloseableHttpClient httpclient;
    private String baseUrl;
    private Scheduler scheduler;

    public DefaultAgentController(String baseUrl) {
        this(baseUrl, null);
    }

    public DefaultAgentController(String baseUrl, Scheduler scheduler) {
        this.baseUrl = baseUrl;
        this.scheduler = scheduler;

        this.initialize();
    }

    private static StringEntity toJsonEntity(Collection<Event> event) throws IOException {
        return new StringEntity(EventJsonSerializer.toJson(event), APPLICATION_JSON);
    }

    @Override
    public void register(Agent agent) {
        this.agents.add(agent);
    }

    @Override
    public void send(Collection<Event> events) {
        try {
            this.sendEvents(events);
        } catch (IOException e) {
            this.logger.error("Cannot send events to HQ.", e);
        }
    }

    @Override
    public void send(Event event) {
        this.send(Collections.singleton(event));
    }

    @Override
    public void unregister(Agent agent) {
        this.agents.remove(agent);
    }

    private void initialize() {
        this.initializeHttpClient();
        this.initializeQuartzScheduler();
        this.initializeHqCommunication();
    }

    private void initializeHqCommunication() {
        JobDetail job = newJob(HqCommunicationJob.class).withIdentity("hq-communication", "logspace").build();
        Trigger trigger = newTrigger().withIdentity("hq-communication-trigger", "logspace").startNow()
                .withSchedule(simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();
        try {
            this.scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new AgentControllerInitializationException("Error while scheduling a Quartz job.", e);
        }
    }

    private void initializeHttpClient() {
        this.httpclient = HttpClients.createDefault();
    }

    private void initializeQuartzScheduler() {
        if (this.scheduler != null) {
            return;
        }

        if (this.isShaded()) {
            System.setProperty("org.quartz.properties", "logspace-shaded-quartz.properties");
        } else {
            System.setProperty("org.quartz.properties", "logspace-quartz.properties");
        }

        try {
            this.scheduler = new StdSchedulerFactory().getScheduler();
            this.scheduler.start();
        } catch (SchedulerException e) {
            throw new AgentControllerInitializationException("Error while creating and starting a Quartz scheduler.", e);
        }
    }

    private boolean isShaded() {
        try {
            Class.forName("io.logspace.agent.impl.shaded.quartz.simpl.SimpleThreadPool");
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    private void sendEvents(Collection<Event> event) throws IOException, ClientProtocolException {
        HttpPut httpPut = new HttpPut(this.baseUrl + "/events/");
        httpPut.setEntity(toJsonEntity(event));

        ResponseHandler<Void> responseHandler = new ResponseHandler<Void>() {

            @Override
            public Void handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                return null;
            }

        };

        this.httpclient.execute(httpPut, responseHandler);
    }

    public static class HqCommunicationJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("hello!!! - " + new Date());
        }
    }
}
