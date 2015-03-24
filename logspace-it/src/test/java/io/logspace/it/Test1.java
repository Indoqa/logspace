/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.it;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import io.logspace.agent.impl.AbstractAgentController;
import io.logspace.agent.impl.HqAgentController;

import org.junit.Test;

public class Test1 extends AbstractLogspaceTest {

    @Test
    public void testMissingAgent() {
        AbstractAgentController hqAgentController = HqAgentController.install("1", "http://localhost:4567");

        this.waitFor(5, SECONDS);

        hqAgentController.shutdown();

        this.commitSolrDocument();
        assertEquals(0, this.getSolrDocumentCount("*:*"));
    }
}