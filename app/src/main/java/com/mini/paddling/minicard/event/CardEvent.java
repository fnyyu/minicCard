package com.mini.paddling.minicard.event;

import com.mini.paddling.minicard.protocol.bean.CardBean;

public class CardEvent {

    private CardBean cardBean;

    private int type; //0为添加 1为修改

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCardBean(CardBean cardBean) {
        this.cardBean = cardBean;
    }

    public CardBean getCardBean() {
        return cardBean;
    }
}
