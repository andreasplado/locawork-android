package ee.locawork.model;

import com.google.gson.annotations.SerializedName;

public class JobGategory {
    @SerializedName("name")
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }
}
