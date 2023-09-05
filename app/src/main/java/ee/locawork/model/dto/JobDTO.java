package ee.locawork.model.dto;

import java.util.Date;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class JobDTO implements ClusterItem, net.sharewire.googlemapsclustering.ClusterItem {

    private int id;

    private String title;

    private Integer userId;

    private String description;

    private Integer categoryId;

    private Double salary;

    private double latitude;

    private double longitude;

    private Date createdAt;

    private boolean isDone;

    private String firebase_token;

    private int fkJobApplyer;

    @Override
    public LatLng getPosition() {
        return new LatLng(getLatitude(), getLongitude());
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }


    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }


    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirebase_token() {
        return firebase_token;
    }

    public void setFirebase_token(String firebase_token) {
        this.firebase_token = firebase_token;
    }

    public int getFkJobApplyer() {
        return fkJobApplyer;
    }

    public void setFkJobApplyer(int fkJobApplyer) {
        this.fkJobApplyer = fkJobApplyer;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }
}