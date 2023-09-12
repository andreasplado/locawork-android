package ee.locawork.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import java.sql.Timestamp;

public class Job implements ClusterItem, net.sharewire.googlemapsclustering.ClusterItem {
    @SerializedName("user_id")
    private String accountGoogleId;
    @SerializedName("categoryId")
    private int categoryId;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("description")
    private String description;
    @SerializedName("applyerId")
    private int applyerId;
    @SerializedName("id")
    private int id;
    @SerializedName("jobEndTime")
    private Timestamp jobEndTime;
    @SerializedName("jobStartTime")
    private Timestamp jobStartTime;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("salary")
    private double salary;

    @SerializedName("hoursToWork")
    private double hoursToWork;
    @SerializedName("title")
    private String title;
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("userId")
    private int userId;


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getPosition() {
        return new LatLng(getLatitude(), getLongitude());
    }

    public String getTitle() {
        return this.title;
    }

    public String getSnippet() {
        return null;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public double getSalary() {
        return this.salary;
    }

    public void setSalary(double salary2) {
        this.salary = salary2;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude2) {
        this.longitude = longitude2;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude2) {
        this.latitude = latitude2;
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

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId2) {
        this.categoryId = categoryId2;
    }

    public String getAccountGoogleId() {
        return this.accountGoogleId;
    }

    public void setAccountGoogleId(String accountGoogleId2) {
        this.accountGoogleId = accountGoogleId2;
    }

    public Timestamp getJobStartTime() {
        return this.jobStartTime;
    }

    public void setJobStartTime(Timestamp jobStartTime2) {
        this.jobStartTime = jobStartTime2;
    }

    public Timestamp getJobEndTime() {
        return this.jobEndTime;
    }

    public void setJobEndTime(Timestamp jobEndTime2) {
        this.jobEndTime = jobEndTime2;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId2) {
        this.userId = userId2;
    }

    public int getApplyerId() {
        return this.applyerId;
    }

    public void setApplyerId(int fkJobApplyer2) {
        this.applyerId = fkJobApplyer2;
    }

    public double getHoursToWork() {
        return hoursToWork;
    }

    public void setHoursToWork(double hoursToWork) {
        this.hoursToWork = hoursToWork;
    }
}
