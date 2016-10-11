/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.orders;

import java.util.Date;

public class Order {

    private String content;

    private Date lastModified;

    private String id;

    public String getContent() {
        return this.content;
    }

    public String getId() {
        return this.id;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
