package com.compart.tec.orientdb;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.compart.tec.orientdb.f1.F1SchemaCreator;
import com.compart.tec.orientdb.f1.F1SimpleFixture;
import com.compart.tec.orientdb.unit.AbstractOrientDBDocumentITest;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OResultSet;

/**
 * @author diegomtassis <a href="mailto:dta@compart.com">Diego Martin Tassis</a>
 */
public class OrientDBPaginationIT extends AbstractOrientDBDocumentITest {

    public OrientDBPaginationIT() {
        super(OrientDBPaginationIT.class.getSimpleName());
        new F1SchemaCreator(getDatabase()).proceed();
        new F1SimpleFixture(getDatabase()).load();
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
