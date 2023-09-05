package ee.locawork.model;

import com.google.gson.annotations.SerializedName;

public class JobApplicationDTO {
    @SerializedName("email")
    private String accountEmail;
    @SerializedName("contact")
    private String contact;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("description")
    private String description;
    @SerializedName("id")
    private Integer id;
    @SerializedName("job_id")
    private Integer jobId;
    @SerializedName("salary")
    private Double salary;
    @SerializedName("title")
    private String title;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("user_id")
    private Integer userId;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public Double getSalary() {
        return this.salary;
    }

    public void setSalary(Double salary2) {
        this.salary = salary2;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId2) {
        this.userId = userId2;
    }

    public Integer getJobId() {
        return this.jobId;
    }

    public void setJobId(Integer jobId2) {
        this.jobId = jobId2;
    }

    public String getAccountEmail() {
        return this.accountEmail;
    }

    public void setAccountEmail(String accountEmail2) {
        this.accountEmail = accountEmail2;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact2) {
        this.contact = contact2;
    }
}
