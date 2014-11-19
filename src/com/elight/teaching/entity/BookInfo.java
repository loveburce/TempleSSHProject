package com.elight.teaching.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.sqlite.FinderLazyLoader;

/**
 * Created by dawn on 2014/9/4.
 */
@Table(name = "BookInfo")
public class BookInfo extends BaseInfo{
    @Column(column = "order")
    private int order;
    @Column(column = "title")
    private String title;
    @Column(column = "image")
    private String image;
    @Finder(valueColumn = "id",targetColumn = "bookId")
    public FinderLazyLoader<LessonInfo> lessonInfos;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
