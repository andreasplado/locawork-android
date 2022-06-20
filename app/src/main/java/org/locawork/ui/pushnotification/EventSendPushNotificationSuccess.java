package org.locawork.ui.pushnotification;

public class EventSendPushNotificationSuccess {

    private String body;

    public EventSendPushNotificationSuccess(String body){
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}
