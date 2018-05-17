/*****************************************************************************
 * Copyright (C) Compart AG, 2016 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.object;

import javax.persistence.Entity;

@Entity
public class Woman extends Person {

    private Man husband;

    public Man getHusband() {
        return this.husband;
    }

    public void setHusband(Man husband) {
        this.husband = husband;
        if (husband.getWife() != this) {
            husband.setWife(this);
        }
    }
}
