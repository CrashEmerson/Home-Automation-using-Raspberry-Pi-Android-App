package com.example.raspi_temphum.CommonFiles.HelperClasses;

public class UserHelperClass {

    String fullName, username, emailAddress, password, gender, phoneNo;

    public UserHelperClass() {
    }

    public UserHelperClass(String fullName, String username, String emailAddress, String password, String gender, String phoneNo) {
        this.fullName = fullName;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.gender = gender;
        this.phoneNo = phoneNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
