/*****************************************************************************
 * Copyright (C) Compart AG, 2016 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.object;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

@Entity
public class Man extends Person {

    private Woman wife;

    private List<Woman> exWives = new ArrayList<Woman>();

    public Woman getWife() {
        return this.wife;
    }

    public void setWife(Woman wife) {
        this.wife = wife;
        if (wife.getHusband() != this) {
            wife.setHusband(this);
        }
    }

    public List<Woman> getExWives() {
        return this.exWives;
    }

    public void setExWives(List<Woman> exWives) {
        this.exWives = exWives;
    }
}
