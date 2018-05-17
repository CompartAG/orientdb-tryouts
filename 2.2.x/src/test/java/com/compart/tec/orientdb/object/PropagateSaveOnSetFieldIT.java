/*****************************************************************************
 * Copyright (C) Compart AG, 2018 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.object;

import org.junit.Assert;
import org.junit.Test;

import com.compart.tec.orientdb.unit.AbstractOrientDBObjectITest;
import com.orientechnologies.orient.core.id.ORecordId;

/**
 * @author diegomtassis <a href="mailto:dta@compart.com">Diego Martin Tassis</a>
 */
public class PropagateSaveOnSetFieldIT extends AbstractOrientDBObjectITest {

    public PropagateSaveOnSetFieldIT() {
        super(PropagateSaveOnSetFieldIT.class.getSimpleName());
    }

    @Test
    public void testPropagateSave_SameConnection() {

        // setup: infrastructure
        registerEntities();

        // setup: data
        Man ayrton = new Man();
        Man alan = new Man();
        ayrton.addFriend(alan);
        alan.setFavoriteFood("raclette");
        this.getDatabase().begin();
        Man savedAyrton = this.getDatabase().save(ayrton);
        this.getDatabase().commit();
        String ayrtonId = savedAyrton.getId();

        // exercise
        this.getDatabase().begin();
        Person retrievedAyrton = this.getDatabase().load(new ORecordId(ayrtonId));
        Person retrievedAlan = retrievedAyrton.getFriends().iterator().next();
        retrievedAlan.setFavoriteFood("scargots");
        savedAyrton = this.getDatabase().save(retrievedAyrton);
        this.getDatabase().commit();

        // verify
        retrievedAyrton = this.getDatabase().load(new ORecordId(ayrtonId));
        Assert.assertEquals("scargots", retrievedAyrton.getFriends().iterator().next().getFavoriteFood());
    }

    @Test
    public void testPropagateSave_DifferentConnections() {

        // setup: infrastructure
        registerEntities();

        // setup: data
        Man ayrton = new Man();
        Man alan = new Man();
        ayrton.addFriend(alan);
        alan.setFavoriteFood("raclette");
        this.getDatabase().begin();
        Man savedAyrton = this.getDatabase().save(ayrton);
        this.getDatabase().commit();
        String ayrtonId = savedAyrton.getId();

        // exercise
        this.releaseCurrentConnection();
        this.getDatabase().begin();
        Person retrievedAyrton = this.getDatabase().load(new ORecordId(ayrtonId));
        Person retrievedAlan = retrievedAyrton.getFriends().iterator().next();
        retrievedAlan.setFavoriteFood("scargots");
        savedAyrton = this.getDatabase().save(retrievedAyrton);
        this.getDatabase().commit();

        // verify
        this.releaseCurrentConnection();
        retrievedAyrton = this.getDatabase().load(new ORecordId(ayrtonId));
        Assert.assertEquals("scargots", retrievedAyrton.getFriends().iterator().next().getFavoriteFood());
    }

    private void registerEntities() {
        getDatabase().getEntityManager().registerEntityClasses(Man.class.getPackage().getName());
    }
}
