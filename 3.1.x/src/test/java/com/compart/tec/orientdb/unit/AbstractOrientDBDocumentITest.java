package com.compart.tec.orientdb.unit;

import java.util.Iterator;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
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
        Assertions.assertTrue(dbIsUpAndRunning());
    }

    @AfterEach
    public void tearDownAbstractOrientDBDocumentITest() {

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
     * @return resultset
     */
    protected OResultSet queryLegacy(String sqlQuery) {
        return getDatabase().query(new OSQLSynchQuery<ODocument>(sqlQuery));
    }

    /**
     * Executes a query
     * 
     * @param sqlQuery
     * @return resultset
     */
    protected OResultSet query(String sqlQuery) {

        return getDatabase().query(sqlQuery);
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

    protected static Iterator<ODocument> toIterator(OResultSet resultSet) {

        return resultSet.elementStream() //
                .map(obj -> (ODocument) obj) //
                .collect(Collectors.toList()) //
                .iterator();
    }
}
