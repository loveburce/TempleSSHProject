package com.elight.teaching.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;

/**
 * Created by dawn on 2014/9/4.
 */
@Table(name = "musicInfo")
public class MusicInfo extends BaseInfo{
    @Column(column = "title")
    private String title;
    @Column(column = "sound")
    private String sound;
    @Column(column = "intro")
    private String intro;
    @Column(column = "text")
    private String text;
    @Column(column = "isRead")
    private boolean isRead;
    @Column(column = "time")
    private Date time;
    @Foreign(column = "lessonInfoId",foreign = "id")
    public LessonInfo lessonInfo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "title='" + title + '\'' +
                ", sound='" + sound + '\'' +
                ", intro='" + intro + '\'' +
                ", text='" + text + '\'' +
                ", isRead=" + isRead +
                ", time=" + time +
                ", lessonInfo=" + lessonInfo +
                '}';
    }
}
