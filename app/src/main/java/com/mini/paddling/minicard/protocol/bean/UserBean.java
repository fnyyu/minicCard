package com.mini.paddling.minicard.protocol.bean;

public class UserBean {

    public String nameToString() {
        return "aname";
    }

    public String pasToString() {
        return "apwd";
    }

    private String aname;
    private String apwd;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getApwd() {
        return apwd;
    }

    public void setApwd(String apwd) {
        this.apwd = apwd;
    }
}
