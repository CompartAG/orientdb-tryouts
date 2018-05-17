/*****************************************************************************
 * Copyright (C) Compart AG, 2018 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.object.ui;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.compart.tec.orientdb.unit.AbstractOrientDBObjectITest;

public class PropagateSaveOnSetIT extends AbstractOrientDBObjectITest {

    private static final String DEFAULT_USER = "xyz";
    private static final String DEFAULT_VIEW = "Job";

    public PropagateSaveOnSetIT() {
        super(PropagateSaveOnSetIT.class.getSimpleName());
    }

    @Before
    public void setUp() {
        this.registerEntities();
    }

    @After
    public void tearDown() {
        this.deregisterEntities();
    }

    @Test
    public void testSave_ChangeAutoRefreshValueOnRetrievedEntity() {

        // setup
        UserSettings userSettings = this.createUserSettings(DEFAULT_USER);
        this.createView(userSettings, DEFAULT_VIEW, Boolean.FALSE);

        this.getDatabase().save(userSettings);

        // exercise
        this.setAutoRefresh(DEFAULT_USER, DEFAULT_VIEW, Boolean.TRUE);

        // verify
        Boolean isAutoRefresh = this.isAutoRefresh(DEFAULT_USER, DEFAULT_VIEW);
        assertTrue(isAutoRefresh);
    }

    @Test
    public void testSave_ChangeAutoRefreshValueOnSavedEntity() {

        // setup
        UserSettings userSettings = this.createUserSettings(DEFAULT_USER);
        this.createView(userSettings, DEFAULT_VIEW, Boolean.FALSE);

        UserSettings savedUserSettings = this.getDatabase().save(userSettings);

        // exercise
        this.setAutoRefresh(savedUserSettings, DEFAULT_VIEW, Boolean.TRUE);

        // verify
        assertTrue(this.isAutoRefresh(DEFAULT_USER, DEFAULT_VIEW));
    }

    private UserSettings createUserSettings(String userName) {

        assertState(this.findUserSettings(userName) == null, "Settings for user " + userName + " already exist");

        UserSettings userSettings = new UserSettings();
        userSettings.setUserName(userName);

        return userSettings;
    }

    private UserSettings findUserSettings(String userName) {

        Iterator<UserSettings> userSettingsIterator = this.getDatabase().browseClass(UserSettings.class);

        while (userSettingsIterator.hasNext()) {
            UserSettings userSettings = (UserSettings) userSettingsIterator.next();
            if (userSettings.getUserName().equals(userName)) {
                return userSettings;
            }
        }

        return null;
    }

    private View createView(UserSettings userSettings, String viewName, Boolean autoRefresh) {

        assertState(this.findView(userSettings, viewName) == null,
                "View " + viewName + " already present in settings for user " + userSettings.getUserName());

        View view = new View();
        view.setName(viewName);
        view.setAutoRefresh(autoRefresh);

        userSettings.addView(view);

        return view;
    }

    private View findView(UserSettings userSettings, String viewName) {

        Iterator<View> iterator = userSettings.getViews().iterator();
        while (iterator.hasNext()) {
            View view = iterator.next();
            if (view.getName().equals(viewName)) {
                return view;
            }
        }

        return null;
    }

    public void setAutoRefresh(String userName, String viewName, Boolean autoRefresh) {

        UserSettings userSettings = this.findUserSettings(userName);
        if (userSettings == null) {
            userSettings = this.createUserSettings(userName);
        }

        this.setAutoRefresh(userSettings, viewName, autoRefresh);
    }

    public void setAutoRefresh(UserSettings userSettings, String viewName, Boolean autoRefresh) {

        View view = this.findView(userSettings, viewName);
        if (view == null) {
            view = this.createView(userSettings, viewName, autoRefresh);
        } else {
            view.setAutoRefresh(autoRefresh);
        }

        this.getDatabase().save(userSettings);
    }

    public Boolean isAutoRefresh(String userName, String viewName) {

        UserSettings userSettings = this.findUserSettings(userName);
        assertState(userSettings != null, "User " + userName + " doesn't exist");

        View view = this.findView(userSettings, viewName);
        assertState(view != null, "View " + viewName + " doesn't exist in user: " + userName);

        return view.getAutoRefresh();
    }

    private void registerEntities() {
        getDatabase().getEntityManager().registerEntityClasses(View.class.getPackage().getName());
    }

    private void deregisterEntities() {
        getDatabase().getEntityManager().deregisterEntityClasses(View.class.getPackage().getName());
    }

    private void assertState(Boolean state, String message) {
        if (!state) {
            throw new IllegalStateException(message);
        }
    }
}
