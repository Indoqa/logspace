/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import java.util.ArrayList;
import java.util.List;

public class FacetBuilder {

    private List<Facet> facets = new ArrayList<>();

    public void addFacet(Facet facet) {
        this.facets.add(facet);
    }

    public int getFacetCount() {
        return this.facets.size();
    }

    public String toJson() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append('{');

        for (Facet eachFacet : this.facets) {
            eachFacet.append(stringBuilder);
            stringBuilder.append(',');
        }

        if (!this.facets.isEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        stringBuilder.append('}');

        return stringBuilder.toString();
    }
}