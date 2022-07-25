package com.example.instagramclone.Model;

import android.graphics.Bitmap;

public class User {

    private String fullname;
    private String email;
    private String username;
    private String bio;
    private String profileImage;
    private String id;

    public User() {
    }

    public User(String id, String username, String fullname, String email,  String bio, String profileImage ) {
        this.fullname = fullname;
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.profileImage = profileImage;
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
