/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr.orders;

import static io.logspace.hq.core.solr.ConfigFieldConstants.*;
import static io.logspace.hq.core.solr.utils.SolrDocumentHelper.*;

import java.io.IOException;

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

import io.logspace.hq.core.api.orders.Order;
import io.logspace.hq.core.api.orders.OrderService;
import io.logspace.hq.core.solr.ConfigQualifier;
import io.logspace.hq.rest.api.DataDeletionException;
import io.logspace.hq.rest.api.DataRetrievalException;
import io.logspace.hq.rest.api.DataStorageException;

@Named
public class SolrOrderService implements OrderService {

    private static final String CONFIG_TYPE = "order";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    @ConfigQualifier
    private SolrClient solrClient;

    @Override
    public void deleteOrder(String orderId) {
        this.logger.debug("Deleting order with ID {}.", orderId);

        try {
            this.solrClient.deleteById(orderId);
        } catch (SolrServerException | IOException e) {
            throw new DataDeletionException("Could not delete order.", e);
        }
    }

    @Override
    public Order getOrder(String controllerId) {
        this.logger.debug("Retrieving order for controller with ID {}.", controllerId);

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setRequestHandler("/get");
        solrQuery.set("id", this.getOrderId(controllerId));

        try {
            QueryResponse queryResponse = this.solrClient.query(solrQuery);
            SolrDocument solrDocument = (SolrDocument) queryResponse.getResponse().get("doc");
            if (solrDocument == null) {
                return null;
            }

            Order result = new Order();
            result.setId(getString(solrDocument, FIELD_ID));
            result.setContent(getString(solrDocument, FIELD_CONTENT));
            result.setLastModified(getDate(solrDocument, FIELD_TIMESTAMP));
            return result;
        } catch (SolrServerException | IOException e) {
            throw new DataRetrievalException("Could not retrieve order.", e);
        }
    }

    @Override
    public void storeOrder(Order order) {
        this.logger.debug("Storing order with ID {}.", order.getId());

        try {
            SolrInputDocument solrInputDocument = new SolrInputDocument();
            solrInputDocument.setField(FIELD_ID, order.getId());
            solrInputDocument.setField(FIELD_TYPE, CONFIG_TYPE);
            solrInputDocument.setField(FIELD_NAME, order.getId());
            solrInputDocument.setField(FIELD_TIMESTAMP, order.getLastModified());
            solrInputDocument.setField(FIELD_CONTENT, order.getContent());
            this.solrClient.add(solrInputDocument);
        } catch (SolrServerException | IOException e) {
            throw new DataStorageException("Could not store order.", e);
        }
    }

    private String getOrderId(String controllerId) {
        return "order_" + controllerId;
    }
}
