package com.compart.tec.orientdb;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.compart.tec.orientdb.unit.AbstractOrientDBDocumentITest;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * OrientDB sql queries try out.
 */
public class OrientDBODocumentIT extends AbstractOrientDBDocumentITest {

    public OrientDBODocumentIT() {
        super(OrientDBODocumentIT.class.getSimpleName());
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void testODocumentHashCode() {

        // setup
        ODocument oDocument = new ODocument("foo");
        Integer hashCodeBefore = oDocument.hashCode();

        // exercise
        oDocument.save();
        Integer hashCodeAfter = oDocument.hashCode();

        // verify
        Assert.assertEquals(hashCodeBefore, hashCodeAfter);
    }

    @Test
    public void testORidHashCode() {

        // setup
        ODocument oDocument = new ODocument("foo");
        Integer hashCodeBefore = oDocument.getIdentity().hashCode();

        // exercise
        oDocument.save();
        Integer hashCodeAfter = oDocument.getIdentity().hashCode();

        // verify
        Assert.assertEquals(hashCodeBefore, hashCodeAfter);
    }
}
