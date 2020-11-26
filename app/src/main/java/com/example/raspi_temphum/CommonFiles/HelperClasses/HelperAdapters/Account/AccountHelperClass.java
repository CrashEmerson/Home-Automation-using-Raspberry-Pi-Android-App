package com.example.raspi_temphum.CommonFiles.HelperClasses.HelperAdapters.Account;

public class AccountHelperClass {

    int image;
    String content;

    public AccountHelperClass(int image, String content) {
        this.image = image;
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }
}
