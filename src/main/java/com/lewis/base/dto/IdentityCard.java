package com.lewis.base.dto;

public class IdentityCard {

    private String account;
    private String cardName;
    private String identityCard;
    private String frontImg;
    private String reverseImg;
    private String handleImg;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getFrontImg() {
        return frontImg;
    }

    public void setFrontImg(String frontImg) {
        this.frontImg = frontImg;
    }

    public String getReverseImg() {
        return reverseImg;
    }

    public void setReverseImg(String reverseImg) {
        this.reverseImg = reverseImg;
    }

    public String getHandleImg() {
        return handleImg;
    }

    public void setHandleImg(String handleImg) {
        this.handleImg = handleImg;
    }
}
