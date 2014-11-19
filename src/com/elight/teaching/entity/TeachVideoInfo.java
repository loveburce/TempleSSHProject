package com.elight.teaching.entity;

/**
 * Created by dawn on 2014/9/4.
 */
public class TeachVideoInfo {
    private Integer id;             //编号id
    private String title;           //视频标题
    private String thumbPic;        //视频缩略图路径
    private String videoUrl;        //视频地址
    private String teachers;        //视频中讲师的名字
    private String intros;          //视频对应的课堂实录文本

    public TeachVideoInfo() {
    }

    public TeachVideoInfo(Integer id, String title, String thumbPic, String videoUrl, String teachers, String intros) {
        this.id = id;
        this.title = title;
        this.thumbPic = thumbPic;
        this.videoUrl = videoUrl;
        this.teachers = teachers;
        this.intros = intros;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTeachers() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

    public String getIntros() {
        return intros;
    }

    public void setIntros(String intros) {
        this.intros = intros;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", thumbPic='" + thumbPic + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", teachers='" + teachers + '\'' +
                ", intros='" + intros + '\'' +
                '}';
    }
}
