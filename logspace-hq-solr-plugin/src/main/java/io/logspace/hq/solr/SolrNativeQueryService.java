/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import java.io.*;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.AutoCloseInputStream;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.InputStreamResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.response.JSONResponseWriter;
import org.apache.solr.response.SolrQueryResponse;

import io.logspace.hq.core.api.event.NativeQueryResult;
import io.logspace.hq.core.api.nativequery.NativeQueryService;
import io.logspace.hq.rest.api.DataRetrievalException;

@Named
public class SolrNativeQueryService extends AbstractSolrService implements NativeQueryService {

    private final JSONResponseWriter jsonResponseWriter = new JSONResponseWriter();

    private static SolrParams createSolrParams(Map<String, String[]> parameters) {
        ModifiableSolrParams result = new ModifiableSolrParams();

        for (Entry<String, String[]> eachEntry : parameters.entrySet()) {
            result.add(eachEntry.getKey(), eachEntry.getValue());
        }

        return result;
    }

    @Override
    public NativeQueryResult executeNativeQuery(Map<String, String[]> parameters) {
        SolrParams params = createSolrParams(parameters);

        try {
            QueryRequest request = new QueryRequest(params, METHOD.POST);
            request.setResponseParser(new InputStreamResponseParser("json"));
            QueryResponse response = request.process(this.solrClient);

            InputStream inputStream = (InputStream) response.getResponse().get("stream");
            if (inputStream != null) {
                return new SolrNativeQueryResult(inputStream);
            }

            return new SolrNativeQueryResult(this.serializeResponse(params, response));
        } catch (SolrException | SolrServerException | IOException e) {
            throw new DataRetrievalException("Could not execute direct query with parameters " + parameters.toString() + ".", e);
        }
    }

    private InputStream serializeResponse(SolrParams params, QueryResponse response) throws IOException {
        LocalSolrQueryRequest solrQueryRequest = new LocalSolrQueryRequest(null, params);
        SolrQueryResponse solrQueryResponse = new SolrQueryResponse();
        solrQueryResponse.setAllValues(response.getResponse());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(baos, "UTF-8");
        this.jsonResponseWriter.write(writer, solrQueryRequest, solrQueryResponse);
        writer.flush();

        return new ByteArrayInputStream(baos.toByteArray());
    }

    private static class SolrNativeQueryResult implements NativeQueryResult {

        private final InputStream inputStream;

        public SolrNativeQueryResult(InputStream inputStream) {
            super();
            this.inputStream = new AutoCloseInputStream(inputStream);
        }

        @Override
        public String getContentType() {
            return "application/json;charset=UTF-8";
        }

        @Override
        public void writeTo(OutputStream outputStream) throws IOException {
            IOUtils.copy(this.inputStream, outputStream);
        }
    }
}
