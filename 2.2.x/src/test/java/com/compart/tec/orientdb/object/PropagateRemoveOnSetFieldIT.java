
package com.compart.tec.orientdb.object;

import com.compart.tec.orientdb.unit.AbstractOrientDBObjectITest;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author diegomtassis <a href="mailto:dta@compart.com">Diego Martin Tassis</a>
 * @author Przemyslaw Fusik
 */
public class PropagateRemoveOnSetFieldIT
        extends AbstractOrientDBObjectITest
{

    public PropagateRemoveOnSetFieldIT()
    {
        super(PropagateRemoveOnSetFieldIT.class.getSimpleName());
    }

    @Test
    public void testPropagateRemoveOverSet_SameConnection()
    {

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
        Assert.assertEquals(1, retrievedAyrton.getFriends().size());
        retrievedAyrton = ((OObjectDatabaseTx) getDatabase()).detachAll(retrievedAyrton, true);
        retrievedAyrton.getFriends().remove(alain);
        this.getDatabase().save(retrievedAyrton);

        // verify
        retrievedAyrton = this.getDatabase().load(new ORecordId(ayrtonId));
        Assert.assertEquals(0, retrievedAyrton.getFriends().size());
    }

    @Test
    public void testPropagateRemoveOverSet_DifferentConnections()
    {

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
        Assert.assertEquals(1, retrievedAyrton.getFriends().size());
        retrievedAyrton = ((OObjectDatabaseTx) getDatabase()).detachAll(retrievedAyrton, true);
        retrievedAyrton.getFriends().remove(alain);
        Assert.assertEquals(0, retrievedAyrton.getFriends().size());
        this.getDatabase().save(retrievedAyrton);

        // verify
        this.releaseCurrentConnection();
        retrievedAyrton = this.getDatabase().load(new ORecordId(ayrtonId));
        Assert.assertEquals(0, retrievedAyrton.getFriends().size());
    }


    @Test
    public void testPropagateRemoveOverCollection_DifferentConnections()
    {

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
        Assert.assertEquals(1, retrievedAyrton.getEnemies().size());
        retrievedAyrton = ((OObjectDatabaseTx) getDatabase()).detachAll(retrievedAyrton, true);
        retrievedAyrton.getEnemies().remove(alain);
        Assert.assertEquals(0, retrievedAyrton.getEnemies().size());
        this.getDatabase().save(retrievedAyrton);

        // verify
        this.releaseCurrentConnection();
        retrievedAyrton = this.getDatabase().load(new ORecordId(ayrtonId));
        Assert.assertEquals(0, retrievedAyrton.getEnemies().size());
    }

    private void registerEntities()
    {
        getDatabase().getEntityManager().registerEntityClasses(Man.class.getPackage().getName());
    }
}
