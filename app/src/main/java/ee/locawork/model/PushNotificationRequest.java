package ee.locawork.model;

public class PushNotificationRequest {
    private String message;
    private String title;
    private String token;
    private String topic;

    public PushNotificationRequest() {
    }

    public PushNotificationRequest(String title2, String messageBody, String topicName) {
        this.title = title2;
        this.message = messageBody;
        this.topic = topicName;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic2) {
        this.topic = topic2;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token2) {
        this.token = token2;
    }
}
