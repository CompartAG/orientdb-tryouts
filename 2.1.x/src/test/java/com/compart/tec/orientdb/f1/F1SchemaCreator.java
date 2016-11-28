package com.compart.tec.orientdb.f1;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;

/**
 * Schema creator for tests scenarios.
 */
public class F1SchemaCreator {

    public static final String TEAM = "Team";
    public static final String CARS_HERITAGE = "cars";

    public static final String CAR = "Car";
    public static final String BUILT_BY = "team";
    public static final String DRIVEN_BY = "drivers";

    public static final String DRIVER = "Driver";
    public static final String DRIVER_OF = "cars";

    public static final String NAME = "name";
    public static final String NATIONALITY = "nationality";
    public static final String YEAR_OF_BIRTH = "yearOfBirth";

    private ODatabaseDocument oDocDatabase;

    /**
     * @param oDocDatabase
     */
    public F1SchemaCreator(ODatabaseDocument oDocDatabase) {
        this.oDocDatabase = oDocDatabase;
    }

    public void proceed() {

        OClass team = getoDocDatabase().getMetadata().getSchema().createClass(TEAM);
        team.createProperty(NAME, OType.STRING);

        OClass driver = getoDocDatabase().getMetadata().getSchema().createClass(DRIVER);
        driver.createProperty(NAME, OType.STRING);

        OClass car = getoDocDatabase().getMetadata().getSchema().createClass(CAR);
        car.createProperty(NAME, OType.STRING);

        // Relationships
        team.createProperty(CARS_HERITAGE, OType.LINKSET, car);
        car.createProperty(BUILT_BY, OType.LINK, team);
        car.createProperty(DRIVEN_BY, OType.LINKSET, driver);
        driver.createProperty(DRIVER_OF, OType.LINKSET, car);
    }

    protected ODatabaseDocument getoDocDatabase() {

        if (oDocDatabase.isClosed()) {
            throw new IllegalStateException("The database has been closed");
        }

        return this.oDocDatabase;
    }
}
