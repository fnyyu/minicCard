package com.mini.paddling.minicard.protocol.bean;

import java.util.List;

public class BusinessBean extends ResultBean {
    /**
     * ret_code : 200
     * res_message : 查询成功
     * data : [{"card_id":"60002","user_id":"2835","card_business_name":"八合里2","card_business_trade":"餐饮2","card_business_service":"粤菜2","card_user_name":"李四","card_user_tel":"152731XXX2","card_user_address":"深圳市南山区2","card_user_slogan":"最好吃的XXX2","card_user_picture":"","card_click_time":""},{"card_id":"60000","user_id":"2835","card_business_name":"八合里","card_business_trade":"餐饮","card_business_service":"粤菜","card_user_name":"张三","card_user_tel":"152731XXX","card_user_address":"深圳市南山区","card_user_slogan":"最好吃的XXX","card_user_picture":"","card_click_time":""}]
     */

    private List<CardBean> data;

    public List<CardBean> getData() {
        return data;
    }

    public void setData(List<CardBean> data) {
        this.data = data;
    }

}
