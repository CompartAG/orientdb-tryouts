/*****************************************************************************
 * Copyright (C) Compart AG, 2016 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.object;

import javax.persistence.Basic;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * Base class for entities.
 */
public abstract class BaseEntity {

    @Id
    public String id;

    @Basic
    public String name;

    @Version
    public String version;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
