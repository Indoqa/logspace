/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.cxf.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.TEXT_PLAIN)
public class TestCxfResource {

    private String value = "";

    public TestCxfResource() {
        super();
    }

    @Path("/crud")
    @DELETE
    public void deleteString() {
        this.value = null;
    }

    @Path("/crud")
    @GET
    public String getString() {
        return this.value;
    }

    @Path("/test")
    @GET
    public String getTest() {
        return "test-response";
    }

    @Path("/crud")
    @POST
    public String postString(String postValue) {
        this.value = postValue;
        return this.value;
    }

    @Path("/crud")
    @PUT
    public void putString(String putValue) {
        this.value = putValue;
    }

}
