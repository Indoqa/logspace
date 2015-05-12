/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp.resource;

import io.logspace.hq.core.api.InvalidSpaceTokenException;
import io.logspace.hq.core.api.MissingSpaceTokenException;
import io.logspace.hq.core.api.Spaces;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Spark;

import com.indoqa.boot.AbstractJsonResourcesBase;

public abstract class AbstractSpaceResource extends AbstractJsonResourcesBase {

    private static final String SPACE_TOKEN_HEADER = "logspace.space-token";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    protected Spaces spaces;

    @PostConstruct
    public void mapExceptions() {
        Spark.exception(MissingSpaceTokenException.class, (e, req, res) -> this.mapMissingSpaceTokenException(res));
        Spark.exception(InvalidSpaceTokenException.class,
                (e, req, res) -> this.mapInvalidSpaceTokenException((InvalidSpaceTokenException) e, res));
    }

    protected String getSpace(Request req) {
        String spaceToken = req.headers(SPACE_TOKEN_HEADER);
        if (StringUtils.isBlank(spaceToken)) {
            throw new MissingSpaceTokenException();
        }

        String space = this.spaces.getSpaceForAuthenticationToken(spaceToken);
        if (space == null) {
            throw new InvalidSpaceTokenException(spaceToken);
        }

        return space;
    }

    protected void validateSpace(Request req) {
        this.getSpace(req);
    }

    private void mapInvalidSpaceTokenException(InvalidSpaceTokenException exception, Response response) {
        response.status(403);
        response.body("Unrecognized space-token '" + exception.getSpaceToken() + "'.");
    }

    private void mapMissingSpaceTokenException(Response response) {
        response.status(403);
        response.body("Missing header '" + SPACE_TOKEN_HEADER + "'.");
    }
}
