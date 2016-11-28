/*****************************************************************************
 * Copyright (C) Compart AG, 2016 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.object;

public class Husband extends BaseEntity {

    private Wife wife;

    public Wife getWife() {
        return this.wife;
    }

    public void setWife(Wife wife) {
        this.wife = wife;
//        if (wife.getHusband() != this) {
//            wife.setHusband(this);
//        }
    }
}
