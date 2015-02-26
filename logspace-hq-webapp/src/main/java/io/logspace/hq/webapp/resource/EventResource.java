package io.logspace.hq.webapp.resource;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import com.indoqa.spark.AbstractResourcesBase;

@Named
public class EventResource extends AbstractResourcesBase {

    @PostConstruct
    public void mount() {
        this.postAsJson("/events", (req, res) -> "foo");
    }
}
