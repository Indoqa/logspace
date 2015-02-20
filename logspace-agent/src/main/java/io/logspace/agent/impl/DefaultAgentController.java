package io.logspace.agent.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import io.logspace.agent.api.Agent;
import io.logspace.agent.api.AgentController;
import io.logspace.agent.api.event.Event;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
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

public class DefaultAgentController implements AgentController {

    private Collection<Agent> receivers = Collections.synchronizedSet(new HashSet<Agent>());
    private CloseableHttpClient httpclient;
    private String baseUrl;

    public DefaultAgentController(String baseUrl) {
        this.baseUrl = baseUrl;
        this.initialize();
    }

    private static StringEntity toJsonEntity(Collection<Event> event) throws IOException {
        return new StringEntity(EventJsonSerializer.toJson(event), ContentType.APPLICATION_JSON);
    }

    @Override
    public void register(Agent agent) {
        this.receivers.add(agent);
    }

    @Override
    public void send(Collection<Event> events) {
        try {
            this.sendEvents(events);
        } catch (IOException e) {

        }
    }

    @Override
    public void send(Event event) {
        this.send(Collections.singleton(event));
    }

    private void initialize() {
        this.initializeHttpClient();
        this.initializeQuartz();
    }

    private void initializeHttpClient() {
        this.httpclient = HttpClients.createDefault();
    }

    private void initializeQuartz() {
        if (this.isShaded()) {
            System.setProperty("org.quartz.properties", "logspace-shaded-quartz.properties");
        }
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = null;
        try {
            scheduler = schedulerFactory.getScheduler();
            scheduler.start();

            JobDetail job = newJob(HelloJob.class).withIdentity("job1", "group1").build();
            Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startNow()
                    .withSchedule(simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Error while creating and starting a Quartz scheduler.", e);
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

    public static class HelloJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("hello!!! - " + new Date());
        }
    }
}
