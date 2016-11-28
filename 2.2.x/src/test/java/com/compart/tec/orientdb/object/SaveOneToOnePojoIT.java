/*****************************************************************************
 * Copyright (C) Compart AG, 2016 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.object;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.compart.tec.orientdb.unit.AbstractOrientDBObjectITest;

public class SaveOneToOnePojoIT extends AbstractOrientDBObjectITest {

    public SaveOneToOnePojoIT() {
        super(SaveOneToOnePojoIT.class.getSimpleName());
    }

    @Test
    public void testUpdateOneToOneBidirectional() {

        // setup
        registerEntities();

        // exercise
        Husband husband = new Husband();
        husband.setName("John");
        husband.setWife(new Wife());
        husband.getWife().setName("Joanna");
        Husband savedHusband = getDatabase().save(husband);

        // verify
        assertEquals("Joanna", savedHusband.getWife().getName());
    }

    private void registerEntities() {
        getDatabase().getEntityManager().registerEntityClasses(Husband.class.getPackage().getName());
    }
}
