/*****************************************************************************
 * Copyright (C) Compart AG, 2018 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.object.ui;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.OneToMany;

import com.compart.tec.orientdb.object.BaseEntity;

@SuppressWarnings("nls")
public class UserSettings extends BaseEntity {

    @Basic
    private String userName;

    @OneToMany
    private Set<View> views = new HashSet<>();

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<View> getViews() {
        return this.views;
    }

    public void addView(View view) {
        this.views.add(view);
    }
}