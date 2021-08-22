package com.Akshith.KnowYourGovernment;

import java.io.Serializable;


public class Official implements Serializable{
    String office;
    String name;
    String party;
    String address;
    String phone;
    String email;
    String websiteURL;
    String youTubeURL;
    String facebookURL;
    String twitterURL;
    String photoURL;
    private String photoLink;
    private Official official;

    public Official getOfficial() {
        return official;
    }


    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public String getYouTubeURL() {
        return youTubeURL;
    }

    public void setYouTubeURL(String youTubeURL) {
        this.youTubeURL = youTubeURL;
    }

    public String getFacebookURL() {
        return facebookURL;
    }

    public void setFacebookURL(String facebookURL) {
        this.facebookURL = facebookURL;
    }

    public String getTwitterURL() {
        return twitterURL;
    }

    public void setTwitterURL(String twitterURL) {
        this.twitterURL = twitterURL;
    }

    public String getOfficialPhotoLink() {
        return photoLink;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }
}
