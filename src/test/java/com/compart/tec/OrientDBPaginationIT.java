package com.compart.tec;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OResultSet;

/**
 * OrientDB sql queries try out.
 */
public class OrientDBPaginationIT extends AbstractOrientDBDocumentITest {

    public OrientDBPaginationIT() {
        super(OrientDBPaginationIT.class.getSimpleName());
        new F1SchemaCreator(getDatabase()).proceed();
        new F1SimpleFixture(getDatabase()).load();
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void testQuery_UsingSkipLimit() {

        // setup
        String query = "select from " + F1SchemaCreator.TEAM + " SKIP 1 LIMIT 3";

        // exercise
        OResultSet<ODocument> teams = executeQuery(query);

        // verify
        int numTeams = 0;
        for (ODocument team : teams) {
            numTeams++;
        }
        assertEquals(3, numTeams);
    }
}
