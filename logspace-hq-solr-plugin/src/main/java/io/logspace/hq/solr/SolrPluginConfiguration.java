/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.indoqa.commons.solr.server.factory.SolrServerFactory;

@Configuration
@PropertySource("classpath:/io/logspace/hq/solr/logspace-hq-solr.properties")
public class SolrPluginConfiguration {

    @Value("${logspace.solr.base-url}")
    private String solrBaseUrl;

    @Value("${logspace.solr.embedded-configuration-dir}")
    private String solrEmbeddedConfigurationDir;

    @Bean
    public SolrServerFactory getSolrServerFactory() {
        SolrServerFactory solrServerFactory = new SolrServerFactory();
        solrServerFactory.setUrl(this.solrBaseUrl);
        solrServerFactory.setEmbeddedSolrConfigurationDir(this.getSolrInstanceDir());
        return solrServerFactory;
    }

    private String getSolrInstanceDir() {
        File file = new File(this.solrEmbeddedConfigurationDir);

        try {
            file = file.getCanonicalFile();
        } catch (IOException e) {
            // do nothing
        }

        return file.getAbsolutePath();
    }
}
