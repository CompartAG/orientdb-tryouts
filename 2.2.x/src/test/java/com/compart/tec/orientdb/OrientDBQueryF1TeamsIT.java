package com.compart.tec.orientdb;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.compart.tec.orientdb.f1.F1SchemaCreator;
import com.compart.tec.orientdb.f1.F1SimpleFixture;
import com.compart.tec.orientdb.unit.AbstractOrientDBDocumentITest;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OResultSet;

/**
 * @author diegomtassis <a href="mailto:dta@compart.com">Diego Martin Tassis</a>
 */
public class OrientDBQueryF1TeamsIT extends AbstractOrientDBDocumentITest {

    public OrientDBQueryF1TeamsIT() {
        super(OrientDBQueryF1TeamsIT.class.getSimpleName());
        new F1SchemaCreator(getDatabase()).proceed();
        new F1SimpleFixture(getDatabase()).load();
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
    @Disabled
    public void testQueryEmulatingJoin_nestedDistinctWithoutExpanding() {

        // setup
        String querySql = "select distinct(" + F1SchemaCreator.BUILT_BY + ")." + F1SchemaCreator.NAME + " from "
                + F1SchemaCreator.CAR;

        // This query fails!
        exerciseAndVerifyTeamsHavingCars(querySql);
    }

    private void exerciseAndVerifyTeamsHavingCars(String query) {

        // exercise
        OResultSet<ODocument> teams = query(query);

        // verify
        int numTeams = 0;
        for (ODocument team : teams) {
            MatcherAssert.assertThat(team.field(F1SchemaCreator.NAME), CoreMatchers
                    .anyOf(CoreMatchers.containsString("Williams"), CoreMatchers.containsString("McLaren")));
            numTeams++;
        }
        Assertions.assertEquals(2, numTeams);
    }
}
