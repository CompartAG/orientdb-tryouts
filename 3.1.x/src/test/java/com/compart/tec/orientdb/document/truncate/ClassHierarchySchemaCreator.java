/*****************************************************************************
 * Copyright (C) Compart AG, 2016 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.document.truncate;

import com.compart.tec.orientdb.unit.SchemaCreator;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.metadata.schema.OClass;

/**
 * Schema creator for tests scenarios.
 */
public class ClassHierarchySchemaCreator implements SchemaCreator {

    public static final String PARENT_CLASS = "parent";
    public static final String SUBCLASS = "subclass";

    private ODatabaseDocument oDocDatabase;

    /**
     * @param oDocDatabase
     */
    public ClassHierarchySchemaCreator(ODatabaseDocument oDocDatabase) {
        this.oDocDatabase = oDocDatabase;
    }

    @Override
    public void proceed() {

        OClass masterOClass = getoDocDatabase().getMetadata().getSchema().createAbstractClass(PARENT_CLASS);

        getoDocDatabase().getMetadata().getSchema().createClass(SUBCLASS, masterOClass);

    }

    protected ODatabaseDocument getoDocDatabase() {

        if (oDocDatabase.isClosed()) {
            throw new IllegalStateException("The database has been closed");
        }

        return this.oDocDatabase;
    }
}
