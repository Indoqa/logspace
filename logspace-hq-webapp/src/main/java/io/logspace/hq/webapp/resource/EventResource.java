package io.logspace.hq.webapp.resource;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Spark;

import com.indoqa.spark.AbstractResourcesBase;

@Named
public class EventResource extends AbstractResourcesBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void mount() {
        Spark.post("/events", (req, res) -> this.logEvents(req, res));
    }

    private String logEvents(Request req, Response res) {
        this.logger.info(req.body());
        res.status(202);
        return "";
    }
}
