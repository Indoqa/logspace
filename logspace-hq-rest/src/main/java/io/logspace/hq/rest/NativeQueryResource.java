/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import io.logspace.hq.core.api.event.NativeQueryResult;
import io.logspace.hq.core.api.nativequery.NativeQueryService;
import io.logspace.hq.rest.api.nativequery.NativeQueryParameters;
import spark.Request;
import spark.Response;
import spark.Spark;

@Named
public class NativeQueryResource extends AbstractLogspaceResourcesBase {

    @Inject
    private NativeQueryService nativeQueryService;

    @PostConstruct
    public void mount() {
        Spark.get(this.resolvePath("/native-query"), (req, res) -> this.getNativeQuery(req, res));
        Spark.post(this.resolvePath("/native-query"), (req, res) -> this.postNativeQuery(req, res));
    }

    private void executeNativeQuery(Response res, Map<String, String[]> parameters) throws IOException {
        NativeQueryResult result = this.nativeQueryService.executeNativeQuery(parameters);
        res.type(result.getContentType());
        result.writeTo(res.raw().getOutputStream());
    }

    private Object getNativeQuery(Request req, Response res) throws IOException {
        this.executeNativeQuery(res, req.raw().getParameterMap());
        return "";
    }

    private Object postNativeQuery(Request req, Response res) throws IOException {
        NativeQueryParameters parameters = this.getTransformer().toObject(req.body(), NativeQueryParameters.class);
        this.executeNativeQuery(res, parameters.getParameters());
        return "";
    }
}
