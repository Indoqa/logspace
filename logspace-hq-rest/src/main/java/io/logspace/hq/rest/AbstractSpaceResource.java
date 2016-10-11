/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.logspace.hq.core.api.spaces.SpacesService;
import io.logspace.hq.rest.api.AbstractLogspaceResourceException;
import io.logspace.hq.rest.api.InvalidSpaceTokenException;
import io.logspace.hq.rest.api.MissingSpaceTokenException;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Base class for resources, which need protection via spaces. See {@link SpacesService}.<br>
 * Consumers must supply the space token header in their request.
 */
public abstract class AbstractSpaceResource extends AbstractLogspaceResourcesBase {

    private static final String SPACE_TOKEN_HEADER = "logspace.space-token";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    protected SpacesService spacesService;

    @PostConstruct
    public void mapExceptions() {
        Spark.exception(AbstractLogspaceResourceException.class,
            (e, req, res) -> this.mapLogspaceException(res, (AbstractLogspaceResourceException) e));
    }

    /**
     * Extract spaceToken from request headers.
     *
     * @param req - Request to extract spaceToken from.
     * @return Extracted spaceToken.
     * @throws MissingSpaceTokenException If the spaceToken Header is missing in the request.
     * @throws InvalidSpaceTokenException If no space is configured for the supplied spaceToken.
     */
    protected String getSpace(Request req) {
        String spaceToken = req.headers(SPACE_TOKEN_HEADER);
        if (StringUtils.isBlank(spaceToken)) {
            throw new MissingSpaceTokenException("Missing header '" + SPACE_TOKEN_HEADER + "'.");
        }

        String space = this.spacesService.getSpaceForAuthenticationToken(spaceToken);
        if (space == null) {
            throw new InvalidSpaceTokenException(spaceToken);
        }

        return space;
    }

    /**
     * Validates the request against its supplied spaceToken Header.<br>
     * Delegates to {@link #getSpace(Request)}
     *
     * @param req - Request to validate.
     */
    protected void validateSpace(Request req) {
        this.getSpace(req);
    }

    private void mapLogspaceException(Response res, AbstractLogspaceResourceException e) {
        res.status(e.getStatusCode().getCode());

        if (e.getStatusCode().hasBody()) {
            res.type("application/json");
            res.body(this.getTransformer().render(e.getErrorData()));
        } else {
            res.body("");
        }
    }
}
