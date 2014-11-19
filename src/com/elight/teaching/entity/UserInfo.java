package com.elight.teaching.entity;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by dawn on 2014/9/19.
 */
public class UserInfo extends BmobChatUser {

    /*显示数据拼音的首字母*/
    private String sortLetters;
    /*性别*/
    private boolean sex;
    /*地理坐标*/
    private BmobGeoPoint location;

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public BmobGeoPoint getLocation() {
        return location;
    }

    public void setLocation(BmobGeoPoint location) {
        this.location = location;
    }
}
