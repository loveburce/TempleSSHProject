package com.elight.teaching.entity;

import com.lidroid.xutils.db.annotation.Id;

/**
 * Created by dawn on 2014/9/4.
 */
public abstract class BaseInfo  {
    @Id
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
