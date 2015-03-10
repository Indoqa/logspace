package io.logspace.hq.webapp.resource;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;

import com.indoqa.spark.AbstractJsonResourcesBase;

@Named
public class EventResource extends AbstractJsonResourcesBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void mount() {
        this.post("/events", (req, res) -> this.logEvents(req, res));
    }

    private String logEvents(Request req, Response res) {
        this.logger.info(req.body());
        res.status(202);
        return "";
    }
}
