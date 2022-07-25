package com.example.instagramclone.Model;

public class Comment {

    private String postId;
    private String comment;
    private String publisher;

    public Comment() {
    }

    public Comment(String postId, String comment, String publisher) {
        this.postId = postId;
        this.comment = comment;
        this.publisher = publisher;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
