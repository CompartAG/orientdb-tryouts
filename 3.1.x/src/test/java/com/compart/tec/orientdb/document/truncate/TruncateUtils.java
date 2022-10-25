/*****************************************************************************
 * Copyright (C) Compart AG, 2020 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.document.truncate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compart.tec.orientdb.unit.Authentication;
import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.exception.OStorageException;

public class TruncateUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TruncateUtils.class);

    private static final String ORIENTDB_CLASS_PREFIX = "O";

    private static final String CLASS_NAME_PLACEHOLDER = "class_name_placeholder";
    private static final String TRUNCATE_CLASS_CLASS_NAME_POLYMORPHIC_UNSAFE = "TRUNCATE CLASS "
            + CLASS_NAME_PLACEHOLDER + " POLYMORPHIC UNSAFE";

    /** Utility class: Hidden constructor. */
    private TruncateUtils() {
        // Hidden
    }

    /**
     * Truncates all the classes in a database (except the OrientDB classes).
     * 
     * @param orientEnvironment
     * @param dbName
     */
    public static final void truncate(OrientDB orientEnvironment, String dbName) {

        truncate(orientEnvironment, dbName, Authentication.DEFAULT_TESTDB_USER, Authentication.DEFAULT_TESTDB_PASSWORD);
    }

    /**
     * Truncates all the classes in a database (except the OrientDB classes).
     * 
     * @param orientEnvironment
     * @param dbName
     * @param user
     * @param password
     */
    public static final void truncate(OrientDB orientEnvironment, String dbName, String user, String password) {

        if (!orientEnvironment.exists(dbName)) {
            LOGGER.info("Database {} does not exist, so it can not be truncated.", dbName);
            return;
        }

        try (ODatabaseSession dbSession = orientEnvironment.open(dbName, user, password)) {
            truncate(dbSession);
        }
    }

    /**
     * Truncates all the classes in a database (except the OrientDB classes).
     * 
     * @param database
     */
    public static final void truncate(ODatabase<?> database) {

        LOGGER.info("Truncating database: {}", database.getURL());

        database.getMetadata().getSchema().getClasses().stream()
                .filter(oClass -> !oClass.getName().startsWith(ORIENTDB_CLASS_PREFIX)) //
                .forEach(oClass -> {
                    // WARNING: Do not use oClass.truncate() since that is not implemented for remote connections!

                    if (oClass.count() > 0) {
                        try {
                            database.command(TRUNCATE_CLASS_CLASS_NAME_POLYMORPHIC_UNSAFE
                                    .replace(CLASS_NAME_PLACEHOLDER, oClass.getName())).close();

                        } catch (OStorageException e) {
                            LOGGER.info("Error truncating OrientDB database", e); // It happens sometimes when the table
                                                                                  // is already empty
                        }
                    }
                });
    }

}
