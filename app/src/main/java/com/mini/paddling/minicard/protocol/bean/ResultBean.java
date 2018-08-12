package com.mini.paddling.minicard.protocol.bean;

public class ResultBean {
    /**
     * ret_code : 300
     * ret_message : 登陆失败
     */

    private String ret_code;
    private String ret_message;

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_message() {
        return ret_message;
    }

    public void setRet_message(String ret_message) {
        this.ret_message = ret_message;
    }
}
