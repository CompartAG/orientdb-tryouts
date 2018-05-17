package com.compart.tec.orientdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import com.compart.tec.orientdb.f1.F1SchemaCreator;
import com.compart.tec.orientdb.f1.F1SimpleFixture;
import com.compart.tec.orientdb.unit.AbstractOrientDBDocumentITest;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OResultSet;

/**
 * OrientDB sql queries try out.
 * 
 * @author diegomtassis <a href="mailto:dta@compart.com">Diego Martin Tassis</a>
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
        String querySql = "select distinct(" + F1SchemaCreator.BUILT_BY + "." + F1SchemaCreator.NAME + ") as "
                + F1SchemaCreator.NAME + " from " + F1SchemaCreator.CAR;

        exerciseAndVerifyTeamsHavingCars(querySql);
    }

    @Test
    public void testQueryEmulatingJoin_nestedDistinctAndExpanding() {

        // setup
        String querySql = "select expand(distinct(" + F1SchemaCreator.BUILT_BY + "))" + " from " + F1SchemaCreator.CAR;

        exerciseAndVerifyTeamsHavingCars(querySql);
    }

    @Test
    public void testQueryEmulatingJoin_nestedDistinctAndExpanding_ignoresProperty() {

        // setup
        String querySql = "select expand(distinct(" + F1SchemaCreator.BUILT_BY + "))." + F1SchemaCreator.NAME + " from "
                + F1SchemaCreator.CAR;

        exerciseAndVerifyTeamsHavingCars(querySql);
    }

    @Test
    @Ignore
    public void testQueryEmulatingJoin_nestedDistinctWithoutExpanding() {

        // setup
        String querySql = "select distinct(" + F1SchemaCreator.BUILT_BY + ")." + F1SchemaCreator.NAME + " from "
                + F1SchemaCreator.CAR;

        // This query fails!
        exerciseAndVerifyTeamsHavingCars(querySql);
    }

    private void exerciseAndVerifyTeamsHavingCars(String query) {

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
}
