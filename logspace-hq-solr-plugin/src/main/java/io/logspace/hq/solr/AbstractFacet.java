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

public abstract class AbstractFacet implements Facet {

    private String name;
    private List<Facet> children;

    public List<Facet> getChildren() {
        return this.children;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public void addChild(Facet facet) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }

        this.children.add(facet);
    }

    public void setChildren(List<Facet> children) {
        this.children = children;
    }

    @Override
    public final void setName(String name) {
        this.name = name;
    }

    @Override
    public void append(StringBuilder stringBuilder) {
        stringBuilder.append("\"");
        stringBuilder.append(this.name);
        stringBuilder.append("\":");

        this.appendBody(stringBuilder);
    }

    protected void appendChildren(StringBuilder stringBuilder) {
        if (this.children == null || this.children.isEmpty()) {
            return;
        }

        stringBuilder.append(',');
        stringBuilder.append("facet:{");
        for (Facet eachChild : this.children) {
            eachChild.append(stringBuilder);
        }
        stringBuilder.append("}");
    }

    protected abstract void appendBody(StringBuilder stringBuilder);

}