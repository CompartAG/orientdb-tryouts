package com.compart.tec.orientdb.f1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * F1 simple fixture.
 */
public class F1SimpleFixture {

    private ODatabaseDocument oDocDatabase;

    /**
     * @param oDocDatabase
     */
    public F1SimpleFixture(ODatabaseDocument oDocDatabase) {
        this.oDocDatabase = oDocDatabase;
    }

    public void load() {

        // Some drivers
        createDriver("Fernando Alonso", "spanish", 1981);
        createDriver("Jenson Button", "british", 1980);
        createDriver("Lewis Hamilton", "british", 1985);
        createDriver("Nigel Mansell", "british", 1953);
        createDriver("Ralf Schumacher", "german", 1975);
        createDriver("Nelson Piquet", "brazilian", 1952);
        createDriver("Michele alboreto", "italian", 1956);
        createDriver("Riccardo Patrese", "italian", 1954);
        createDriver("Stefan Bellof", "german", 1957);
        createDriver("Stefan Johansson", "swedish", 1956);

        // The teams
        createMcLarenF1Team();
        createWilliamsF1Team();
        createTeam("Scuderia Ferrari");
        createTeam("Tyrrell");
        createTeam("Toleman");
    }

    private ODocument createMcLarenF1Team() {

        ODocument alonso = findDriver("Fernando Alonso").get();
        ODocument button = findDriver("Jenson Button").get();
        ODocument hamilton = findDriver("Lewis Hamilton").get();

        ODocument mp4_22 = createCar("Mp4-22", alonso, hamilton);
        ODocument mp4_28 = createCar("Mp4-28", hamilton, button);
        ODocument mp4_30 = createCar("Mp4-30", alonso, button);
        ODocument mclarenF1Team = createTeam("McLaren F1 Team").field(F1SchemaCreator.CARS_HERITAGE,
                new HashSet<>(Arrays.asList(mp4_22, mp4_28, mp4_30)));

        Arrays.asList(mp4_22, mp4_28, mp4_30).stream().forEach(car -> {
            car.field(F1SchemaCreator.BUILT_BY, mclarenF1Team).save();
        });

        return mclarenF1Team.save();
    }

    private ODocument createWilliamsF1Team() {

        ODocument button = findDriver("Jenson Button").get();
        ODocument ralf = findDriver("Ralf Schumacher").get();
        ODocument mansell = findDriver("Nigel Mansell").get();
        ODocument piquet = findDriver("Nelson Piquet").get();

        ODocument fw22 = createCar("FW22", ralf, button).save();
        ODocument fw11 = createCar("FW11", mansell, piquet).save();

        ODocument williamsF1Team = createTeam("Williams F1 Team").field(F1SchemaCreator.CARS_HERITAGE,
                new HashSet<>(Arrays.asList(fw22, fw11)));

        Arrays.asList(fw11, fw22).stream().forEach(car -> {
            car.field(F1SchemaCreator.BUILT_BY, williamsF1Team).save();
        });

        return williamsF1Team.save();
    }

    private ODocument createTeam(String teamName) {
        return new ODocument(F1SchemaCreator.TEAM).field(F1SchemaCreator.NAME, teamName).save();
    }

    private ODocument createDriver(String name, String nationality, int yearOfBirth) {

        return new ODocument(F1SchemaCreator.DRIVER).field(F1SchemaCreator.NAME, name)
                .field(F1SchemaCreator.NATIONALITY, nationality).field(F1SchemaCreator.YEAR_OF_BIRTH, yearOfBirth)
                .save();
    }

    private ODocument createCar(String name, ODocument... drivers) {
        ODocument car = new ODocument(F1SchemaCreator.CAR).field(F1SchemaCreator.NAME, name)
                .field(F1SchemaCreator.DRIVEN_BY, Arrays.asList(drivers)).save();
        Arrays.asList(drivers).stream().forEach(driver -> {
            driver.field(F1SchemaCreator.DRIVER_OF, car);
            driver.save();
        });
        return car;
    }

    private Optional<ODocument> findDriver(String name) {

        for (ODocument driver : oDocDatabase.browseClass(F1SchemaCreator.DRIVER)) {
            if (name.equals(driver.field(F1SchemaCreator.NAME))) {
                return Optional.of(driver);
            }
        }

        return Optional.empty();
    }
}
