package com.mini.paddling.minicard.protocol.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CardBean implements Parcelable {
    /**
     * card_id : 60002
     * user_id : 2835
     * card_business_name : 八合里2
     * card_business_trade : 餐饮2
     * card_business_service : 粤菜2
     * card_user_name : 李四
     * card_user_tel : 152731XXX2
     * card_user_address : 深圳市南山区2
     * card_user_slogan : 最好吃的XXX2
     * card_user_picture :
     * card_click_time :
     */

    private String card_id = "";
    private String user_id = "";
    private String card_business_name = "";
    private String card_business_trade = "";
    private String card_business_service = "";
    private String card_user_name = "";
    private String card_user_tel = "";
    private String card_user_address = "";
    private String card_user_slogan = "";
    private String card_user_picture = "";
    private String card_click_time = "";
    /**
     * is_collect : 1
     */

    private String is_collect = "";

    public CardBean() {}

    protected CardBean(Parcel in) {
        card_id = in.readString();
        user_id = in.readString();
        card_business_name = in.readString();
        card_business_trade = in.readString();
        card_business_service = in.readString();
        card_user_name = in.readString();
        card_user_tel = in.readString();
        card_user_address = in.readString();
        card_user_slogan = in.readString();
        card_user_picture = in.readString();
        card_click_time = in.readString();
        is_collect = in.readString();
    }

    public static final Creator<CardBean> CREATOR = new Creator<CardBean>() {
        @Override
        public CardBean createFromParcel(Parcel in) {
            return new CardBean(in);
        }

        @Override
        public CardBean[] newArray(int size) {
            return new CardBean[size];
        }
    };

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCard_business_name() {
        return card_business_name;
    }

    public void setCard_business_name(String card_business_name) {
        this.card_business_name = card_business_name;
    }

    public String getCard_business_trade() {
        return card_business_trade;
    }

    public void setCard_business_trade(String card_business_trade) {
        this.card_business_trade = card_business_trade;
    }

    public String getCard_business_service() {
        return card_business_service;
    }

    public void setCard_business_service(String card_business_service) {
        this.card_business_service = card_business_service;
    }

    public String getCard_user_name() {
        return card_user_name;
    }

    public void setCard_user_name(String card_user_name) {
        this.card_user_name = card_user_name;
    }

    public String getCard_user_tel() {
        return card_user_tel;
    }

    public void setCard_user_tel(String card_user_tel) {
        this.card_user_tel = card_user_tel;
    }

    public String getCard_user_address() {
        return card_user_address;
    }

    public void setCard_user_address(String card_user_address) {
        this.card_user_address = card_user_address;
    }

    public String getCard_user_slogan() {
        return card_user_slogan;
    }

    public void setCard_user_slogan(String card_user_slogan) {
        this.card_user_slogan = card_user_slogan;
    }

    public String getCard_user_picture() {
        return card_user_picture;
    }

    public void setCard_user_picture(String card_user_picture) {
        this.card_user_picture = card_user_picture;
    }

    public String getCard_click_time() {
        return card_click_time;
    }

    public void setCard_click_time(String card_click_time) {
        this.card_click_time = card_click_time;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(card_id);
        dest.writeString(user_id);
        dest.writeString(card_business_name);
        dest.writeString(card_business_trade);
        dest.writeString(card_business_service);
        dest.writeString(card_user_name);
        dest.writeString(card_user_tel);
        dest.writeString(card_user_address);
        dest.writeString(card_user_slogan);
        dest.writeString(card_user_picture);
        dest.writeString(card_click_time);
        dest.writeString(is_collect);
    }
}
