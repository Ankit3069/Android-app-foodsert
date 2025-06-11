package com.example.personalizedecommerceapp.model;

import java.io.Serializable;

public class UserMaster implements Serializable {
    private int userId;
    private String fullName, emailId, password, contactNumber, address;
    private String shopTypePreference, cuisineCategoryPreference;
    private String minPricePreference, maxPricePreference;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopTypePreference() {
        return shopTypePreference;
    }

    public void setShopTypePreference(String shopTypePreference) {
        this.shopTypePreference = shopTypePreference;
    }

    public String getCuisineCategoryPreference() {
        return cuisineCategoryPreference;
    }

    public void setCuisineCategoryPreference(String cuisineCategoryPreference) {
        this.cuisineCategoryPreference = cuisineCategoryPreference;
    }

    public String getMinPricePreference() {
        return minPricePreference;
    }

    public void setMinPricePreference(String minPricePreference) {
        this.minPricePreference = minPricePreference;
    }

    public String getMaxPricePreference() {
        return maxPricePreference;
    }

    public void setMaxPricePreference(String maxPricePreference) {
        this.maxPricePreference = maxPricePreference;
    }
}
