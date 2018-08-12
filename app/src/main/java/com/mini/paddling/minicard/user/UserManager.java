package com.mini.paddling.minicard.user;

import com.mini.paddling.minicard.protocol.bean.UserBean;

public class UserManager {



    private UserBean userBean;

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public UserBean getUserBean() {
        return userBean;
    }
}
