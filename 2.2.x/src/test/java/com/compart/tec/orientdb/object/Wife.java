/*****************************************************************************
 * Copyright (C) Compart AG, 2016 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.object;

public class Wife extends BaseEntity {

    private Husband husband;

    public Husband getHusband() {
        return this.husband;
    }

    public void setHusband(Husband husband) {
        this.husband = husband;
        if (husband.getWife() != this) {
            husband.setWife(this);
        }
    }
}
