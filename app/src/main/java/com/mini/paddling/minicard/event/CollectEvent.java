package com.mini.paddling.minicard.event;


public class CollectEvent {

    private int type;

    private String cardId;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public int getType() {
        return type;
    }

    public String getCardId() {
        return cardId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setType(int type) {
        this.type = type;
    }
}
