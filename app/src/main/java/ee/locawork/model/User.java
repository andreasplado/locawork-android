package ee.locawork.model;

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

    @SerializedName("id_code")
    private String id_code;

    @SerializedName("company_name")
    private String company_name;

    @SerializedName("company_reg_number")
    private String company_reg_number;
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

    public String getId_code() {
        return id_code;
    }

    public void setIdCode(String id_code) {
        this.id_code = id_code;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompanyName(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_reg_number() {
        return company_reg_number;
    }

    public void setCompanyRegNumber(String company_reg_number) {
        this.company_reg_number = company_reg_number;
    }
}
