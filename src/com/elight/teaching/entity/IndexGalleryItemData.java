package com.elight.teaching.entity;

/**
 * 
 */
public class IndexGalleryItemData {

	private int id;
	private int imageUrl;
	private String text;

    public IndexGalleryItemData(int id, int imageUrl, String text) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
