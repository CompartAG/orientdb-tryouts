package com.compart.tec.orientdb.unit;

import org.junit.Assert;

import com.orientechnologies.orient.core.db.object.ODatabaseObject;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

/**
 * Base class for integration tests which need to interact with an orientdb object database.
 * 
 * @author dta
 */
public abstract class AbstractOrientDBObjectITest {

    private ODatabaseObject oDatabase;

    /**
     * Instantiates a "plocal" object database.
     * 
     * @param databaseName
     */
    public AbstractOrientDBObjectITest(String databaseName) {

        String dbUrl = "memory:foo/" + databaseName;

        this.oDatabase = new OObjectDatabaseTx(dbUrl);
        if (this.oDatabase.exists()) {
            this.oDatabase.open(Authentication.DEFAULT_TESTDB_USER, Authentication.DEFAULT_TESTDB_PASSWORD);
            this.oDatabase.drop();
        }
        this.oDatabase.create();

        Assert.assertTrue(dbIsUpAndRunning());
    }

    /**
     * @return <code>true</code> if the database is up and running, <code>false</code> otherwise.
     */
    protected boolean dbIsUpAndRunning() {
        return this.oDatabase != null;
    }

    public void tearDown() {
        this.oDatabase.drop();
        if (!this.oDatabase.isClosed()) {
            this.oDatabase.close();
        }
    }

    protected ODatabaseObject getDatabase() {
        this.oDatabase.activateOnCurrentThread();
        return this.oDatabase;
    }
}
