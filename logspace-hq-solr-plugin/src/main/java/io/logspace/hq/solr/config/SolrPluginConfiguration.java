/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.indoqa.solr.spring.client.SolrClientFactory;

@Configuration
public class SolrPluginConfiguration {

    @Value("${logspace.solr-events.base-url}")
    private String eventsSolrBaseUrl;

    @Value("${logspace.solr-config.base-url}")
    private String configSolrBaseUrl;

    @Bean
    @Qualifier("config-solr-client")
    public SolrClientFactory getConfigSolrClientFactory() {
        SolrClientFactory solrClientFactory = new SolrClientFactory();

        solrClientFactory.setUrl(this.configSolrBaseUrl);
        solrClientFactory.setEmbeddedSolrConfigurationPath("META-INF/solr/config");

        return solrClientFactory;
    }

    @Bean
    @Qualifier("event-solr-client")
    public SolrClientFactory getEventSolrClientFactory() {
        SolrClientFactory solrClientFactory = new SolrClientFactory();

        solrClientFactory.setUrl(this.eventsSolrBaseUrl);
        solrClientFactory.setEmbeddedSolrConfigurationPath("META-INF/solr/events");

        return solrClientFactory;
    }
}
