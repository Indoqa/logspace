/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr.capabilities;

import static io.logspace.hq.core.solr.ConfigFieldConstants.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.logspace.agent.api.json.AgentControllerCapabilitiesJsonDeserializer;
import io.logspace.agent.api.json.AgentControllerCapabilitiesJsonSerializer;
import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.order.PropertyDescription;
import io.logspace.hq.core.api.agent.IdHelper;
import io.logspace.hq.core.api.capabilities.CapabilitiesService;
import io.logspace.hq.core.solr.ConfigQualifier;
import io.logspace.hq.core.solr.utils.SolrDocumentHelper;
import io.logspace.hq.rest.api.DataRetrievalException;
import io.logspace.hq.rest.api.DataStorageException;
import io.logspace.hq.rest.api.suggestion.AgentDescription;

@Named
public class SolrCapabilitiesService implements CapabilitiesService {

    private static final String FIELD_GLOBAL_AGENT_ID = "strings_property_globalAgentId";
    private static final String CONFIG_TYPE = "capabilitites";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    @ConfigQualifier
    private SolrClient solrClient;

    @Override
    public AgentDescription getAgentDescription(String globalAgentId) {
        try {
            SolrQuery solrQuery = new SolrQuery(FIELD_GLOBAL_AGENT_ID + ":\"" + globalAgentId + "\"");
            solrQuery.setFilterQueries(FIELD_TYPE + ":" + CONFIG_TYPE);

            QueryResponse queryResponse = this.solrClient.query(solrQuery);
            for (SolrDocument eachSolrDocument : queryResponse.getResults()) {
                String content = SolrDocumentHelper.getString(eachSolrDocument, FIELD_CONTENT);
                AgentControllerCapabilities controllerCapabilities = AgentControllerCapabilitiesJsonDeserializer.fromJson(content);

                AgentCapabilities agentCapabilities = this.getAgentCapabilities(controllerCapabilities, globalAgentId);
                if (agentCapabilities != null) {
                    return this.createAgentDescription(controllerCapabilities, agentCapabilities);
                }
            }

            return null;
        } catch (SolrServerException | IOException e) {
            throw new DataRetrievalException("Could not retrieve capabilities.", e);
        }
    }

    @Override
    public AgentControllerCapabilities getCapabilities(String controllerId) {
        this.logger.debug("Retrieving agent controller capabilities for controller with ID '{}'.", controllerId);

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setRequestHandler("/get");
        solrQuery.set("id", this.getCapabilitiesId(controllerId));

        try {
            QueryResponse queryResponse = this.solrClient.query(solrQuery);
            SolrDocument solrDocument = (SolrDocument) queryResponse.getResponse().get("doc");
            if (solrDocument == null) {
                return null;
            }

            String content = SolrDocumentHelper.getString(solrDocument, FIELD_CONTENT);
            return AgentControllerCapabilitiesJsonDeserializer.fromJson(content);
        } catch (SolrServerException | IOException e) {
            throw new DataRetrievalException("Could not retrieve agent controller capabilities.", e);
        }
    }

    @Override
    public void save(AgentControllerCapabilities capabilities) throws IOException {
        this.logger.debug("Storing controller capabilities with ID {}.", capabilities.getId());

        try {
            SolrInputDocument solrInputDocument = new SolrInputDocument();
            solrInputDocument.setField(FIELD_ID, this.getCapabilitiesId(capabilities.getId()));
            solrInputDocument.setField(FIELD_TYPE, CONFIG_TYPE);
            solrInputDocument.setField(FIELD_NAME, capabilities.getId());
            solrInputDocument.setField(FIELD_TIMESTAMP, new Date());
            solrInputDocument.setField(FIELD_CONTENT, AgentControllerCapabilitiesJsonSerializer.toJson(capabilities));

            for (AgentCapabilities eachAgentCapabilities : capabilities.getAgentCapabilities()) {
                String globalAgentId = IdHelper.getGlobalAgentId(
                    capabilities.getSpace(),
                    capabilities.getSystem(),
                    eachAgentCapabilities.getId());
                solrInputDocument.addField(FIELD_GLOBAL_AGENT_ID, globalAgentId);
            }

            this.solrClient.add(solrInputDocument);
        } catch (SolrServerException | IOException e) {
            throw new DataStorageException("Could not store capabilities.", e);
        }
    }

    private AgentDescription createAgentDescription(AgentControllerCapabilities capabilities, AgentCapabilities agentCapabilities) {
        AgentDescription result = new AgentDescription();

        result.setGlobalId(IdHelper.getGlobalAgentId(capabilities.getSpace(), capabilities.getSystem(), agentCapabilities.getId()));
        result.setName(agentCapabilities.getId());
        result.setSystem(capabilities.getSystem());
        result.setSpace(capabilities.getSpace());
        result.setPropertyDescriptions(this.getSortedPropertyDescriptions(agentCapabilities));

        return result;
    }

    private AgentCapabilities getAgentCapabilities(AgentControllerCapabilities capabilities, String globalAgentId) {
        for (AgentCapabilities eachAgentCapabilities : capabilities.getAgentCapabilities()) {
            String id = IdHelper.getGlobalAgentId(capabilities.getSpace(), capabilities.getSystem(), eachAgentCapabilities.getId());

            if (id.equals(globalAgentId)) {
                return eachAgentCapabilities;
            }
        }

        return null;
    }

    private String getCapabilitiesId(String controllerId) {
        return "capabilities_" + controllerId;
    }

    private List<PropertyDescription> getSortedPropertyDescriptions(AgentCapabilities eachAgentCapabilities) {
        List<PropertyDescription> propertyDescriptions = Arrays.asList(eachAgentCapabilities.getPropertyDescriptions());
        Collections.sort(propertyDescriptions);
        return propertyDescriptions;
    }
}
