/*****************************************************************************
 * Copyright (C) Compart AG, 2020 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.document;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.compart.tec.orientdb.unit.AbstractOrientDBDocumentITest;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

/**
 *
 * @author dta
 *
 */
public class Issue9797RegressionTest extends AbstractOrientDBDocumentITest {

    public Issue9797RegressionTest() {
        super(Issue9797RegressionTest.class.getSimpleName());
    }

    @Test
    public void testQuery() {

        // setup: schema
        new F1DocumentSchemaCreator(this.getDatabase()).proceed();

        // setup: data
        ODocument alonso = this.getDatabase().save(new ODocument(F1DocumentSchemaCreator.DRIVER));
        alonso.field(F1DocumentSchemaCreator.NAME, "Fernando Alonso");

        ODocument ferrariF12t = this.getDatabase().save(new ODocument(F1DocumentSchemaCreator.CAR));
        ferrariF12t.field(F1DocumentSchemaCreator.NAME, "Ferrari F12T");

        ODocument scuderiaFerrari = this.getDatabase().save(new ODocument(F1DocumentSchemaCreator.TEAM));
        scuderiaFerrari.field(F1DocumentSchemaCreator.NAME, "Scuderia Ferrari");

        // relationships
        ferrariF12t.field(F1DocumentSchemaCreator.BUILT_BY, scuderiaFerrari);
        ferrariF12t.field(F1DocumentSchemaCreator.DRIVEN_BY, alonso);
        alonso.field(F1DocumentSchemaCreator.DRIVER_OF, ferrariF12t);

        this.getDatabase().save(alonso);
        this.getDatabase().save(ferrariF12t);
        this.getDatabase().save(scuderiaFerrari);

        // exercise
        OResultSet resultset = this
                .query("SELECT expand(distinct(cars.team)) FROM Driver WHERE name = 'Fernando Alonso'");

        // verify
        int count = 0;
        while (resultset.hasNext()) {
            resultset.next();
            count++;
        }
        Assertions.assertEquals(1, count);
    }

}
