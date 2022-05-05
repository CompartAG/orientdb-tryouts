package com.compart.tec.orientdb.object;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
        Assertions.assertNotNull(savedJohn);
        // Normal loop
        for (Woman exWife : savedJohn.getExWives()) {
            Assertions.assertNotNull(exWife.getName());
        }
        // Streaming
        savedJohn.getExWives().stream().forEach((exWife) -> {
            Assertions.assertNotNull(exWife.getName());
        });
    }

    @Test
    @Disabled
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
        Assertions.assertNotNull(savedJohn);
        savedJohn.getExWives().stream().forEach((exWife) -> {
            Assertions.assertNotNull(exWife.getName());
        });
    }
}
