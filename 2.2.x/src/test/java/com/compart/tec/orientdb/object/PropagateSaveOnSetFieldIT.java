
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

    /**
     * Propagating a save operation over a Set field using the same connection. It works.
     */
    @Test
    public void testPropagateSaveOverSet_SameConnection() {

        // setup: infrastructure
        registerEntities();

        // setup: data
        Man ayrton = new Man();
        Man alain = new Man();
        ayrton.addFriend(alain);
        alain.setFavoriteFood("raclette");
        Man savedAyrton = this.getDatabase().save(ayrton);
        String ayrtonId = savedAyrton.getId();

        // exercise
        Person retrievedAyrton = this.getDatabase().load(new ORecordId(ayrtonId));
        Person retrievedAlain = retrievedAyrton.getFriends().iterator().next();
        retrievedAlain.setFavoriteFood("scargots");
        savedAyrton = this.getDatabase().save(retrievedAyrton);

        // verify
        retrievedAyrton = this.getDatabase().load(new ORecordId(ayrtonId));
        Assert.assertEquals("scargots", retrievedAyrton.getFriends().iterator().next().getFavoriteFood());
    }

    /**
     * Propagating a save operation over a Set field (and then reading) using different connections. It fails.
     */
    @Test
    public void testPropagateSaveOverSet_DifferentConnections() {

        // setup: infrastructure
        registerEntities();

        // setup: data
        Man ayrton = new Man();
        Man alain = new Man();
        ayrton.addFriend(alain);
        alain.setFavoriteFood("raclette");
        Man savedAyrton = this.getDatabase().save(ayrton);
        String ayrtonId = savedAyrton.getId();

        // exercise
        this.releaseCurrentConnection();
        Person retrievedAyrton = this.getDatabase().load(new ORecordId(ayrtonId));
        Person retrievedAlain = retrievedAyrton.getFriends().iterator().next();
        retrievedAlain.setFavoriteFood("scargots");
        savedAyrton = this.getDatabase().save(retrievedAyrton);

        // verify
        this.releaseCurrentConnection();
        retrievedAyrton = this.getDatabase().load(new ORecordId(ayrtonId));
        Assert.assertEquals("scargots", retrievedAyrton.getFriends().iterator().next().getFavoriteFood());
    }
    
    /**
     * Propagating a save operation over a Collection field (and then reading) using different connections. It works
     */
    @Test
    public void testPropagateSaveOverCollection_DifferentConnections() {

        // setup: infrastructure
        registerEntities();

        // setup: data
        Man ayrton = new Man();
        Man alain = new Man();
        ayrton.addEnemy(alain);
        alain.setFavoriteFood("raclette");
        Man savedAyrton = this.getDatabase().save(ayrton);
        String ayrtonId = savedAyrton.getId();

        // exercise
        this.releaseCurrentConnection();
        Person retrievedAyrton = this.getDatabase().load(new ORecordId(ayrtonId));
        Person retrievedAlain = retrievedAyrton.getEnemies().iterator().next();
        retrievedAlain.setFavoriteFood("scargots");
        savedAyrton = this.getDatabase().save(retrievedAyrton);

        // verify
        this.releaseCurrentConnection();
        retrievedAyrton = this.getDatabase().load(new ORecordId(ayrtonId));
        Assert.assertEquals("scargots", retrievedAyrton.getEnemies().iterator().next().getFavoriteFood());
    }

    private void registerEntities() {
        getDatabase().getEntityManager().registerEntityClasses(Man.class.getPackage().getName());
    }
}
