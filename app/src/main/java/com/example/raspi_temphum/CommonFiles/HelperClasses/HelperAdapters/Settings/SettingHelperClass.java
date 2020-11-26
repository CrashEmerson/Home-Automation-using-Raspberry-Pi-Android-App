package com.example.raspi_temphum.CommonFiles.HelperClasses.HelperAdapters.Settings;

public class SettingHelperClass {

    int image;
    String content, desc, value;

    public SettingHelperClass(int image, String content, String desc, String value) {
        this.image = image;
        this.content = content;
        this.desc = desc;
        this.value = value;
    }

    public int getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        return value;
    }
}