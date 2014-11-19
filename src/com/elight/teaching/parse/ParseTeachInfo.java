package com.elight.teaching.parse;

import android.util.Xml;
import com.elight.teaching.entity.TeachClassInfo;
import com.elight.teaching.entity.TeachReferInfo;
import com.elight.teaching.entity.TeachVideoInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2014/9/4.
 */
public class ParseTeachInfo {

    public static List<TeachReferInfo> parseTeachReferInfo(String str){
        if(null != str || !str.equals("")) {
            try {
                List<TeachReferInfo> teachReferInfoList = new ArrayList<TeachReferInfo>();
                JSONArray jsonArray = new JSONArray(str);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String title = jsonObject.getString("title");
                    String content = jsonObject.getString("content");

                    TeachReferInfo teachReferInfo = new TeachReferInfo(id, title, content);
                    teachReferInfoList.add(teachReferInfo);
                }
                return teachReferInfoList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<TeachClassInfo> parseTeachClassList(String str){
        if(null != str && !str.equals("")) {
            try {
                List<TeachClassInfo> teachClassInfoList = new ArrayList<TeachClassInfo>();
                JSONArray jsonArray = new JSONArray(str);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String title = jsonObject.getString("title");
                    String thumbPic = jsonObject.getString("litpic");
                    String swfUrl = jsonObject.getString("appends");

                    TeachClassInfo teachClassInfo = new TeachClassInfo(id, title, thumbPic, swfUrl);
                    teachClassInfoList.add(teachClassInfo);
                }
                return teachClassInfoList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<TeachVideoInfo> parseTeachVideoList(String str){
        if(null != str || !str.equals("")) {
            try {
                List<TeachVideoInfo> teachVideoInfoList = new ArrayList<TeachVideoInfo>();
                JSONArray jsonArray = new JSONArray(str);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String title = jsonObject.getString("title");
                    String thumbPic = jsonObject.getString("litpic");
                    String videoUrl = jsonObject.getString("videourl");
                    String teachers = jsonObject.getString("teachers");
                    String intros = jsonObject.getString("intros");

                    TeachVideoInfo teachVideoInfo = new TeachVideoInfo(id, title, thumbPic, videoUrl, teachers, intros);
                    teachVideoInfoList.add(teachVideoInfo);
                }
                return teachVideoInfoList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
