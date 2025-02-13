package com.example.skill_ladder.model;

public class Company {

    private String id;
    private String name;
    private String mobile;
    private String email;

    private boolean isActive;

    public Company(String id, String name, String mobile, String email, boolean isActive) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


}
