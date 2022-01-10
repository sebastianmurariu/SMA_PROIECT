package model;

import com.google.firebase.Timestamp;

public class Memorie {
    private String title;
    private String thought;
    private String imageUrl;
    private String userId;
    private com.google.firebase.Timestamp timeAdded;
    private String userName;

    //Empty constructor -> must fot firestore to work
    public Memorie(){

    }

    public Memorie(String title, String thought, String imageUrl, String userId,
                   com.google.firebase.Timestamp timeAdded, String userName) {
        this.title = title;
        this.thought = thought;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.timeAdded = timeAdded;
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public com.google.firebase.Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
