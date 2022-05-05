/*****************************************************************************
 * Copyright (C) Compart AG, 2020 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.document;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.compart.tec.orientdb.unit.AbstractOrientDBDocumentITest;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

/**
 *
 * @author dta
 *
 */
public class Issue9199RegressionTest extends AbstractOrientDBDocumentITest {

    public Issue9199RegressionTest() {
        super(Issue9199RegressionTest.class.getSimpleName());
    }

    @Test
    public void testQuery_SetWIth1Element() {

        // setup
        ODocument master = this.getDatabase().save(new ODocument("Master"));
        Set<String> labels = new HashSet<>(Arrays.asList("bar"));
        master.field("labels", labels, OType.EMBEDDEDSET);

        ODocument detail = new ODocument("Detail");
        detail.field("master", master);
        detail.field("name", "foo");

        this.getDatabase().save(detail);

        // exercise
        OResultSet resultset = this.query("SELECT FROM Detail WHERE (name = 'foo' AND master.labels LIKE '%bar%')");

        // verify
        int count = 0;
        while (resultset.hasNext()) {
            resultset.next();
            count++;
        }
        Assertions.assertEquals(1, count);
    }

    @Test
    public void testQuery_SetWIth2Elements() {

        // setup
        ODocument master = this.getDatabase().save(new ODocument("Master"));
        Set<String> labels = new HashSet<>(Arrays.asList("bar", "foobar"));
        master.field("labels", labels, OType.EMBEDDEDSET);

        ODocument detail = new ODocument("Detail");
        detail.field("master", master);
        detail.field("name", "foo");

        this.getDatabase().save(detail);

        // exercise
        OResultSet resultset = this.query("SELECT FROM Detail WHERE (name = 'foo' AND master.labels LIKE '%bar%')");

        // verify
        int count = 0;
        while (resultset.hasNext()) {
            resultset.next();
            count++;
        }
        Assertions.assertEquals(1, count);
    }

    @Test
    public void testQuery_UsingContains() {

        // setup
        ODocument master = this.getDatabase().save(new ODocument("Master"));
        Set<String> labels = new HashSet<>(Arrays.asList("bar", "foobar"));
        master.field("labels", labels, OType.EMBEDDEDSET);

        ODocument detail = new ODocument("Detail");
        detail.field("master", master);
        detail.field("name", "foo");

        this.getDatabase().save(detail);

        // exercise
        OResultSet resultset = this
                .query("SELECT FROM Detail WHERE (name = 'foo' AND master CONTAINS (labels LIKE '%bar%'))");

        // verify
        int count = 0;
        while (resultset.hasNext()) {
            resultset.next();
            count++;
        }
        Assertions.assertEquals(1, count);
    }
}
