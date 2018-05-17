package com.compart.tec.orientdb;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.compart.tec.orientdb.unit.AbstractOrientDBDocumentITest;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * @author diegomtassis <a href="mailto:dta@compart.com">Diego Martin Tassis</a>
 */
public class OrientDBODocumenHashCodeIT extends AbstractOrientDBDocumentITest {

    public OrientDBODocumenHashCodeIT() {
        super(OrientDBODocumenHashCodeIT.class.getSimpleName());
    }

    @Test
    @Ignore
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
    @Ignore
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
