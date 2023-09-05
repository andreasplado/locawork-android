package ee.locawork.model;

import com.google.gson.annotations.SerializedName;

public class PayingToken {

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
