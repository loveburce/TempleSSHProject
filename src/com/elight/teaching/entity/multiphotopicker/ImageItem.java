package com.elight.teaching.entity.multiphotopicker;

import java.io.Serializable;

/**
 * Created by dawn on 2014/10/28.
 */

public class ImageItem implements Serializable{

    public String imageId;
    public String thumbnailPath;
    public String sourcePath;
    public boolean isSelected = false;

    @Override
    public String toString() {
        return "ImageItem{" +
                "imageId='" + imageId + '\'' +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                ", sourcePath='" + sourcePath + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
