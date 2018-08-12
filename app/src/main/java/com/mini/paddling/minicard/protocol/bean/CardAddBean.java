package com.mini.paddling.minicard.protocol.bean;

public class CardAddBean extends ResultBean {


    /**
     * res_message : 添加成功
     * data : {"card_id":60003}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * card_id : 60003
         */

        private String card_id;

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }
    }
}
