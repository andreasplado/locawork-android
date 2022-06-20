package org.locawork.model;

public class Post {
    private String about;
    private String name;
    private String password;
    private String token;

    public Post(String name2, String password2) {
        this.name = name2;
        this.password = password2;
    }

    public Post(String name2, String password2, String about2) {
        this.name = name2;
        this.password = password2;
        this.about = about2;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public String getAbout() {
        return this.about;
    }

    public String getToken() {
        return this.token;
    }
}
