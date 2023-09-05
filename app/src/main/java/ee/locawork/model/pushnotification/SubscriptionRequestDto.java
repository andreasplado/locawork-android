package ee.locawork.model.pushnotification;

import java.util.List;

public class SubscriptionRequestDto {

    private String topicName;
    private List<String> tokens;

    public SubscriptionRequestDto() {
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}
