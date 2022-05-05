/*****************************************************************************
 * Copyright (C) Compart AG, 2016 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.object;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.compart.tec.orientdb.unit.AbstractOrientDBObjectITest;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.object.enhancement.OObjectProxyMethodHandler;

import javassist.util.proxy.ProxyObject;

/**
 * @author diegomtassis <a href="mailto:dta@compart.com">Diego Martin Tassis</a>
 */
@Disabled
public class SaveOneToOnePojoIT extends AbstractOrientDBObjectITest {

    public SaveOneToOnePojoIT() {
        super(SaveOneToOnePojoIT.class.getSimpleName());
    }

    @Test
    public void testUpdateOneToOneBidirectional() {

        // setup
        registerEntities();

        // exercise
        Man husband = new Man();
        husband.setName("John");
        husband.setWife(new Woman());
        husband.getWife().setName("Joanna");
        Man savedHusband = getDatabase().save(husband);

        // verify
        Assertions.assertEquals(1, getDocument(savedHusband).getVersion(), "Parent saved 2 times");
        Assertions.assertEquals(1, getDocument(savedHusband.getWife()).getVersion(), "Child saved 2 times");
    }

    private void registerEntities() {
        getDatabase().getEntityManager().registerEntityClasses(Man.class.getPackage().getName());
    }

    private ODocument getDocument(Object object) {
        return ((OObjectProxyMethodHandler) ((ProxyObject) object).getHandler()).getDoc();
    }
}
