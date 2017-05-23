/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr;

import javax.inject.Inject;

import org.apache.solr.client.solrj.SolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSolrConfigService {

    @Inject
    @ConfigQualifier
    protected SolrClient solrClient;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FieldDefinitions fieldDefinitions = this.createFieldDefinitions();

    public FieldDefinitions getFieldDefinitions() {
        return this.fieldDefinitions;
    }

    protected FieldDefinitions createFieldDefinitions() {
        return FieldDefinitions.empty();
    }
}
