package org.locawork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("fullname")
    private String fullname;
    @SerializedName("contact")
    private String contact;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    private String name;

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String accountGoogleId2) {
        this.fullname = accountGoogleId2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact2) {
        this.contact = contact2;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
