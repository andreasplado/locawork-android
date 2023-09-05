package ee.locawork.model;

import com.google.gson.annotations.SerializedName;

public class Settings {
    private boolean askPermissionsBeforeDeletingAJob;
    private Double radius;
    private boolean showInformationOnStartup;
    private int userId;
    private String viewByDefault;
    private String email;
    private String fullname;
    private String contact;
    @SerializedName("role")
    private String role;
    @SerializedName("biometric")
    private boolean isBiometric;

    private String customerId;

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId2) {
        this.userId = userId2;
    }

    public Double getRadius() {
        return this.radius;
    }

    public void setRadius(Double radius2) {
        this.radius = radius2;
    }

    public String getViewByDefault() {
        return this.viewByDefault;
    }

    public void setViewByDefault(String viewByDefault2) {
        this.viewByDefault = viewByDefault2;
    }

    public boolean isAskPermissionsBeforeDeletingAJob() {
        return this.askPermissionsBeforeDeletingAJob;
    }

    public void setAskPermissionsBeforeDeletingAJob(boolean askPermissionsBeforeDeletingAJob2) {
        this.askPermissionsBeforeDeletingAJob = askPermissionsBeforeDeletingAJob2;
    }

    public boolean isShowInformationOnStartup() {
        return this.showInformationOnStartup;
    }

    public void setShowInformationOnStartup(boolean showInformationOnStartup2) {
        this.showInformationOnStartup = showInformationOnStartup2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isBiometric() {
        return isBiometric;
    }

    public void setBiometric(boolean biometric) {
        isBiometric = biometric;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
