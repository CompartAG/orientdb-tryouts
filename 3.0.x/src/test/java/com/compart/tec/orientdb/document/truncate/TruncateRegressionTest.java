/*****************************************************************************
 * Copyright (C) Compart AG, 2020 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.document.truncate;

import org.junit.jupiter.api.Test;

import com.compart.tec.orientdb.unit.AbstractOrientDBDocumentITest;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 *
 * @author dta
 *
 */
public class TruncateRegressionTest extends AbstractOrientDBDocumentITest {

    public TruncateRegressionTest() {
        super(TruncateRegressionTest.class.getSimpleName());
    }

    @Test
    public void testTruncate() {

        // setup: schema
        new ClassHierarchySchemaCreator(this.getDatabase()).proceed();

        // setup: data
        ODocument object = this.getDatabase().save(new ODocument(ClassHierarchySchemaCreator.SUBCLASS));
        this.getDatabase().save(object);

        // exercise
        TruncateUtils.truncate(this.getDatabase());
    }

}
