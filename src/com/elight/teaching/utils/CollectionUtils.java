package com.elight.teaching.utils;

import android.content.Entity;
import cn.bmob.im.bean.BmobChatUser;

import java.util.*;

/**
 * Created by dawn on 2014/9/22.
 */
public class CollectionUtils {

    public static boolean isNotNull(Collection<?> collection){
        if(collection != null && collection.size() > 0){
            return true;
        }
        return false;
    }

    public static Map<String, BmobChatUser> list2map(List<BmobChatUser> users){
        Map<String, BmobChatUser> friends = new HashMap<String, BmobChatUser>();
        for(BmobChatUser user : users){
            friends.put(user.getUsername(), user);
        }
        return friends;
    }

    public static List<BmobChatUser> map2list(Map<String, BmobChatUser> maps){
        List<BmobChatUser> users = new ArrayList<BmobChatUser>();
        Iterator<Map.Entry<String, BmobChatUser>>  iterator = maps.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, BmobChatUser> entry = iterator.next();
            users.add(entry.getValue());
        }
        return users;
    }
}
