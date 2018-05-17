package com.compart.tec.orientdb.object;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.compart.tec.orientdb.unit.AbstractOrientDBObjectITest;

/**
 * @author diegomtassis <a href="mailto:dta@compart.com">Diego Martin Tassis</a>
 */
public class OObjectLazyListRegressionIT extends AbstractOrientDBObjectITest {

    public OObjectLazyListRegressionIT() {
        super(OObjectLazyListRegressionIT.class.getSimpleName());
        this.registerEntities();
    }

    private void registerEntities() {
        this.getDatabase().getEntityManager().registerEntityClass(Man.class);
        this.getDatabase().getEntityManager().registerEntityClass(Woman.class);
    }

    @Test
    public void testLoopSavedListField() {

        // setup
        Man john = new Man();
        john.setName("John");

        Woman katherine = new Woman();
        katherine.setName("Katherine");
        john.getExWives().add(katherine);

        Woman rachel = new Woman();
        rachel.setName("Rachel");
        john.getExWives().add(rachel);

        // exercise
        Man savedJohn = getDatabase().save(john);

        // verify
        Assert.assertNotNull(savedJohn);
        // Normal loop
        for (Woman exWife : savedJohn.getExWives()) {
            Assert.assertNotNull(exWife.getName());
        }
        // Streaming
        savedJohn.getExWives().stream().forEach((exWife) -> {
            Assert.assertNotNull(exWife.getName());
        });
    }

    @Test
    @Ignore
    public void testStreamSavedListField() {

        // setup
        Man john = new Man();
        john.setName("John");

        Woman katherine = new Woman();
        katherine.setName("Katherine");
        john.getExWives().add(katherine);

        Woman rachel = new Woman();
        rachel.setName("Rachel");
        john.getExWives().add(rachel);

        // exercise
        Man savedJohn = getDatabase().save(john);

        // verify
        Assert.assertNotNull(savedJohn);
        savedJohn.getExWives().stream().forEach((exWife) -> {
            Assert.assertNotNull(exWife.getName());
        });
    }
}
