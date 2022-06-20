package org.locawork.model.pushnotification;

public class NotificationRequestDto {

    private String target;
    private String title;
    private String body;

    public String getTarget() {
        return target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
