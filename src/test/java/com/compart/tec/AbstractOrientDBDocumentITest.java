package com.compart.tec;

import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OResultSet;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

/**
 * Base class for integration tests which need to interact with an orientdb object database.
 * 
 * @author dta
 */
public abstract class AbstractOrientDBDocumentITest {

    private ODatabaseDocument oDocDatabase;

    private OPartitionedDatabasePool databasePool;

    /**
     * Instantiates an document database.
     * 
     * @param databaseName
     */
    public AbstractOrientDBDocumentITest(String databaseName) {

        String dbUrl = "memory:foo/" + databaseName;
        ODatabaseDocumentTx db = new ODatabaseDocumentTx(dbUrl);
        db.create();
        db.close();

        this.databasePool = new OPartitionedDatabasePool(dbUrl, Authentication.DEFAULT_TESTDB_USER,
                Authentication.DEFAULT_TESTDB_PASSWORD);
        this.oDocDatabase = this.databasePool.acquire();

        if (!dbIsUpAndRunning()) {
            throw new IllegalStateException("Database is closed.");
        }
    }

    public void tearDown() {

        this.oDocDatabase.drop();
        if (!this.oDocDatabase.isClosed()) {
            this.oDocDatabase.close();
        }

        databasePool.close();
    }

    /**
     * @return <code>true</code> if the database is up and running, <code>false</code> otherwise.
     */
    protected boolean dbIsUpAndRunning() {
        return this.oDocDatabase != null;
    }

    /**
     * Executes a query
     * 
     * @param sqlQuery
     * @return resultSet
     */
    protected OResultSet<ODocument> executeQuery(String sqlQuery) {
        OSQLSynchQuery<ODocument> oQuery = new OSQLSynchQuery<ODocument>(sqlQuery);
        return getDatabase().command(oQuery).execute();
    }

    protected ODatabaseDocument getDatabase() {

        if (this.oDocDatabase.isClosed()) {
            this.oDocDatabase = this.databasePool.acquire();
        }

        if (this.oDocDatabase.isClosed()) {
            throw new IllegalStateException("Database is closed.");
        }

        return this.oDocDatabase;
    }
}
