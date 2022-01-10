package util;

import android.app.Application;

public class MemorieApi extends Application {
    private String username;
    private String userId;
    private static MemorieApi instance;

    public MemorieApi(){}

    public static MemorieApi getInstance(){
        if(instance == null)
            instance = new MemorieApi();
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
