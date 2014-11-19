package com.elight.teaching.utils;

import com.elight.teaching.entity.UserInfo;

import java.util.Comparator;

/**
 * Created by dawn on 2014/9/29.
 */
public class PinyinComparator implements Comparator<UserInfo>{
    @Override
    public int compare(UserInfo o1, UserInfo o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
