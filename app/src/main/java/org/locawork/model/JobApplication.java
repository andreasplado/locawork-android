package org.locawork.model;

import com.google.gson.annotations.SerializedName;

public class JobApplication {
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("id")

    private Integer id;
    @SerializedName("isApproved")
    private boolean isApproved;
    @SerializedName("job")
    private Integer job;
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("userId")
    private Integer user;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isApproved() {
        return this.isApproved;
    }

    public void setApproved(boolean approved) {
        this.isApproved = approved;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt2) {
        this.createdAt = createdAt2;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String updatedAt2) {
        this.updatedAt = updatedAt2;
    }

    public Integer getJob() {
        return this.job;
    }

    public void setJob(Integer job2) {
        this.job = job2;
    }

    public Integer getUser() {
        return this.user;
    }

    public void setUser(Integer user2) {
        this.user = user2;
    }
}
