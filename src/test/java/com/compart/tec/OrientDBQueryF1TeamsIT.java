package com.compart.tec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Test;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OResultSet;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

/**
 * OrientDB sql queries try out.
 */
public class OrientDBQueryF1TeamsIT extends AbstractOrientDBDocumentITest {

    public OrientDBQueryF1TeamsIT() {
        super(OrientDBQueryF1TeamsIT.class.getSimpleName());
        new F1SchemaCreator(getDatabase()).proceed();
        new F1SimpleFixture(getDatabase()).load();
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void testQueryEmulatingJoin_withoutExpanding() {

        // setup
        String querySql = "select distinct(" + F1SchemaCreator.BUILT_BY + "." + F1SchemaCreator.NAME + ")"
                + " as team from " + F1SchemaCreator.CAR;

        exerciseAndVerifyTeams(querySql);
    }

    @Test
    public void testQueryEmulatingJoin_nestedDistinctAndExpanding() {

        // setup
        String querySql = "select expand(distinct(" + F1SchemaCreator.BUILT_BY + "))" + " from " + F1SchemaCreator.CAR;

        exerciseAndVerifyTeams(querySql);
    }

    @Test
    public void testQueryEmulatingJoin_nestedDistinctAndExpanding_ignoresProperty() {

        // setup
        String querySql = "select expand(distinct(" + F1SchemaCreator.BUILT_BY + "))." + F1SchemaCreator.NAME + " from "
                + F1SchemaCreator.CAR;

        exerciseAndVerifyTeams(querySql);
    }

    @Test
    public void testQueryEmulatingJoin_nestedDistinctWithoutExpanding() {

        // setup
        String querySql = "select distinct(" + F1SchemaCreator.BUILT_BY + ")." + F1SchemaCreator.NAME + " from "
                + F1SchemaCreator.CAR;

        // This query fails!
        exerciseAndVerifyTeams(querySql);
    }

    private void exerciseAndVerifyTeams(String query) {

        // exercise
        OResultSet<ODocument> teams = executeQuery(query);

        // verify
        int numTeams = 0;
        for (ODocument team : teams) {
            assertThat(team.field(F1SchemaCreator.NAME), CoreMatchers.anyOf(CoreMatchers.containsString("Williams"),
                    CoreMatchers.containsString("McLaren")));
            numTeams++;
        }
        assertEquals(2, numTeams);
    }

    private OResultSet<ODocument> executeQuery(String sqlQuery) {
        OSQLSynchQuery<ODocument> oQuery = new OSQLSynchQuery<ODocument>(sqlQuery);
        return getDatabase().command(oQuery).execute();
    }
}
