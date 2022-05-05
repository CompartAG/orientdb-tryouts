/*****************************************************************************
 * Copyright (C) Compart AG, 2015 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.unit;

/**
 * Wraps the creation of a schema.
 */
@FunctionalInterface
public interface SchemaCreator {

    /**
     * Creates the schema for a database.
     */
    void proceed();
}
