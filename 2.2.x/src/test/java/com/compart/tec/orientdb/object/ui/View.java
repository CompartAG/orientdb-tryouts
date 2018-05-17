package com.compart.tec.orientdb.object.ui;

import javax.persistence.Basic;

import com.compart.tec.orientdb.object.BaseEntity;

public class View extends BaseEntity {

    @Basic
    private Boolean autoRefresh;

    public Boolean getAutoRefresh() {
        return this.autoRefresh;
    }

    public void setAutoRefresh(Boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }
}