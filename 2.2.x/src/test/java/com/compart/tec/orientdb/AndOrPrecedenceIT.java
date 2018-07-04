package com.compart.tec.orientdb;

import org.junit.Assert;
import org.junit.Test;

import com.compart.tec.orientdb.unit.AbstractOrientDBDocumentITest;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OResultSet;

/**
 * @author diegomtassis <a href="mailto:dta@compart.com">Diego Martin Tassis</a>
 */
public class AndOrPrecedenceIT extends AbstractOrientDBDocumentITest {

    private static final String EQUALS = " = ";
    private static final String OR = " OR ";
    private static final String AND = " AND ";

    private static final String DRIVER = "Person";
    private static final String NAME = "name";
    private static final String AGE = "age";

    private static final int PROST_AGE = 54;

    public AndOrPrecedenceIT() {
        super(AndOrPrecedenceIT.class.getSimpleName());
    }

    /**
     * <code>false AND false AND false OR true</code>, should evaluate to true but evaluates to false instead
     */
    @Test
    public void testAndOrPrecedence_NoParentheses_3AndClauses() {

        // setup
        this.createProst();

        StringBuilder queryBuilder = new StringBuilder("select from ").append(DRIVER) //
                .append(" where ") //
                .append(AGE).append(EQUALS).append(10) //
                .append(AND).append(AGE).append(EQUALS).append(15) //
                .append(AND).append(AGE).append(EQUALS).append(20) //
                .append(OR).append(AGE).append(EQUALS).append(PROST_AGE);

        // exercise
        OResultSet<ODocument> drivers = executeQuery(queryBuilder.toString());

        // verify
        Assert.assertEquals(1, drivers.size());
    }

    /**
     * <code>true AND false AND false OR true</code>, evaluates to true, which is correct, what it is odd is that it is
     * almost identical to the failing scenario but replacing one of the AND subconditions to be true, which should not
     * matter.
     */
    @Test
    public void testAndOrPrecedence_NoParentheses_3AndClausesOneOfThemIsTrue() {

        // setup
        this.createProst();

        StringBuilder queryBuilder = new StringBuilder("select from ").append(DRIVER) //
                .append(" where ") //
                .append(AGE).append(EQUALS).append(PROST_AGE) //
                .append(AND).append(AGE).append(EQUALS).append(15) //
                .append(AND).append(AGE).append(EQUALS).append(20) //
                .append(OR).append(AGE).append(EQUALS).append(PROST_AGE);

        // exercise
        OResultSet<ODocument> drivers = executeQuery(queryBuilder.toString());

        // verify
        Assert.assertEquals(1, drivers.size());
    }

    /**
     * <code>(false AND false AND false) OR true</code>, evaluates to true (correct)
     */
    @Test
    public void testAndOrPrecedence_Parentheses_3AndClauses() {

        // setup
        this.createProst();

        StringBuilder queryBuilder = new StringBuilder("select from ").append(DRIVER) //
                .append(" where ") //
                .append(" ( ") //
                .append(AGE).append(EQUALS).append(10) //
                .append(AND).append(AGE).append(EQUALS).append(15) //
                .append(AND).append(AGE).append(EQUALS).append(20) //
                .append(" ) ") //
                .append(OR).append(AGE).append(EQUALS).append(PROST_AGE);

        // exercise
        OResultSet<ODocument> drivers = executeQuery(queryBuilder.toString());

        // verify
        Assert.assertEquals(1, drivers.size());
    }

    /**
     * <code>false AND false OR true</code>, evaluates to true (correct)
     */
    @Test
    public void testAndOrPrecedence_NoParentheses_2AndClauses() {

        // setup
        this.createProst();

        StringBuilder queryBuilder = new StringBuilder("select from ").append(DRIVER) //
                .append(" where ") //
                .append(AGE).append(EQUALS).append(10) //
                .append(AND).append(AGE).append(EQUALS).append(20) //
                .append(OR).append(AGE).append(EQUALS).append(PROST_AGE);

        // exercise
        OResultSet<ODocument> drivers = executeQuery(queryBuilder.toString());

        // verify
        Assert.assertEquals(1, drivers.size());
    }

    private void createProst() {

        new ODocument(DRIVER) //
                .field(NAME, "Alain") //
                .field(AGE, PROST_AGE) //
                .save();
    }
}
