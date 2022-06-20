package org.locawork.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MyApplications {
    @SerializedName("job_applications")
    private List<MyApplicationDTO> myApplications;

    public List<MyApplicationDTO> getMyApplications() {
        return this.myApplications;
    }

    public void setMyApplications(List<MyApplicationDTO> myApplications2) {
        this.myApplications = myApplications2;
    }
}
