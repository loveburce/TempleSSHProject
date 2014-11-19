package com.elight.teaching.entity;

/**
 * Created by dawn on 2014/11/11.
 */
public class TeachClassInfo {

    private Integer id;         //编号
    private String title;       //标题
    private String thumbPic;    //缩略图
    private String swfUrl;      //SWF路径

    public TeachClassInfo() {
    }

    public TeachClassInfo(Integer id, String title, String thumbPic, String swfUrl) {
        this.id = id;
        this.title = title;
        this.thumbPic = thumbPic;
        this.swfUrl = swfUrl;
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

    public String getThumbPic() {
        return thumbPic;
    }

    public void setThumbPic(String thumbPic) {
        this.thumbPic = thumbPic;
    }

    public String getSwfUrl() {
        return swfUrl;
    }

    public void setSwfUrl(String swfUrl) {
        this.swfUrl = swfUrl;
    }

    @Override
    public String toString() {
        return "TeachClassInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", thumbPic='" + thumbPic + '\'' +
                ", swfUrl='" + swfUrl + '\'' +
                '}';
    }
}
