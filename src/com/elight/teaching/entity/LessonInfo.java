package com.elight.teaching.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.sqlite.FinderLazyLoader;

/**
 * Created by dawn on 2014/9/4.
 */
public class LessonInfo extends BaseInfo{
    @Column(column = "order")
    private int order;
    @Column(column = "title")
    private String title;
    @Column(column = "image")
    private String image;
    @Column(column = "isRead")
    private String isRead;
    @Column(column = "intro")
    private String intro;
    @Foreign(column = "bookInfoId", foreign = "id")
    public BookInfo bookInfo;
    @Finder(valueColumn = "id",targetColumn = "bookId")
    public FinderLazyLoader<MusicInfo> musicInfos;

    public LessonInfo() {
    }

    public LessonInfo(String title, String image, String intro) {
        this.title = title;
        this.image = image;
        this.intro = intro;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
