package com.mini.paddling.minicard.user;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.mini.paddling.minicard.main.MiniApplication;
import com.mini.paddling.minicard.protocol.bean.UserBean;

import static android.content.Context.MODE_PRIVATE;

public class LoginUserManager {

    private static final String TAG = "mini_card_UserManager";

    private static LoginUserManager instance = new LoginUserManager();

    private UserBean userBean;

    private boolean isLogin;

    public void doLogin(UserBean userBean) {

        this.userBean = userBean;
        isLogin = true;
        SharedPreferences sp = MiniApplication.getContext().getSharedPreferences(TAG, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("username", userBean.getAname());
        ed.putString("password", userBean.getApwd());
        ed.putString("uid", userBean.getUserId());
        ed.apply();

    }

    public UserBean getUserBean() {
        return userBean;
    }

    public String getUserName() {
        return userBean.getAname();
    }

    public String getUserPas() {
        return userBean.getApwd();
    }

    public String getUserUid() {
        return userBean.getUserId();
    }

    public boolean isLogin() {
        return isLogin;
    }

    private LoginUserManager(){
        userBean = new UserBean();
        loadDataFromSP();
    }

    private void loadDataFromSP() {
        SharedPreferences sp = MiniApplication.getContext().getSharedPreferences(TAG, MODE_PRIVATE);
        if(sp!=null){
            userBean.setAname(sp.getString("username", ""));
            userBean.setApwd(sp.getString("password", ""));
            userBean.setUserId(sp.getString("uid", ""));
            if (!TextUtils.isEmpty(getUserUid())){
                isLogin = true;
            }
        }
    }

    public void logOut(){
        SharedPreferences sp = MiniApplication.getContext().getSharedPreferences(TAG, MODE_PRIVATE);
        isLogin = false;
        if(sp!=null){
            SharedPreferences.Editor ed = sp.edit();
            userBean.setAname("");
            userBean.setApwd("");
            userBean.setUserId("");
            ed.putString("username", userBean.getAname());
            ed.putString("password", userBean.getApwd());
            ed.putString("uid", userBean.getUserId());
            ed.apply();
        }
    }

    public static LoginUserManager getInstance(){
        return instance;
    }


}
