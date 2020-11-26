package com.example.raspi_temphum.CommonFiles.HelperClasses.HelperAdapters.Chats;

public class ChatsHelperClass {

    String chatsText, chatsTime;

    public ChatsHelperClass(String logsText, String chatsTime) {
        this.chatsText = logsText;
        this.chatsTime = chatsTime;
    }

    public String getChatsText() {
        return chatsText;
    }

    public String getChatsTime() {
        return chatsTime;
    }
}
