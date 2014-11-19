package com.elight.teaching.entity;

/**
 * Created by dawn on 2014/11/11.
 */
public class TeachReferInfo {
    private Integer id;     //教参编号
    private String title;   //教参标题
    private String content; //教参内容

    public TeachReferInfo() {
    }

    public TeachReferInfo(Integer id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TeachReferInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
