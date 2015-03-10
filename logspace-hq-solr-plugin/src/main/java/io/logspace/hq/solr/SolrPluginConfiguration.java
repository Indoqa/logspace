package io.logspace.hq.solr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.indoqa.commons.solr.server.factory.SolrServerFactory;

@Configuration
@PropertySource("classpath:/io/logspace/solr/logspace-hq-solr.properties")
public class SolrPluginConfiguration {

    @Value("${logspace.solr.base-url}")
    private String solrBaseUrl;

    @Bean
    public SolrServerFactory getSolrServerFactory() {
        SolrServerFactory solrServerFactory = new SolrServerFactory();
        solrServerFactory.setUrl(this.solrBaseUrl);
        return solrServerFactory;
    }
}
