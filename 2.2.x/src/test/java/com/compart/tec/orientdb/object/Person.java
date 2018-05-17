/*****************************************************************************
 * Copyright (C) Compart AG, 2016 - Compart confidential
 *
 *****************************************************************************/

package com.compart.tec.orientdb.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.OneToMany;

public abstract class Person extends BaseEntity {

    @Basic
    private String favoriteFood;

    @OneToMany
    private Set<Person> friends = new HashSet<>();

    @OneToMany
    private Collection<Person> enemies = new ArrayList<>();

    public Set<Person> getFriends() {
        return this.friends;
    }

    public void addFriend(Person friend) {
        this.friends.add(friend);
    }

    public Collection<Person> getEnemies() {
        return this.enemies;
    }

    public void addEnemy(Person enemy) {
        this.enemies.add(enemy);
    }

    public String getFavoriteFood() {
        return this.favoriteFood;
    }

    public void setFavoriteFood(String favoriteFood) {
        this.favoriteFood = favoriteFood;
    }
}
