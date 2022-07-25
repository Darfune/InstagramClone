package com.example.instagramclone.Model;

public class Post {

    private String description;
    private String imageURL;
    private String postId;
    private String poster;

    public Post() {}

    public Post(String description, String imageURL, String postId, String poster) {
        this.description = description;
        this.imageURL = imageURL;
        this.postId = postId;
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
