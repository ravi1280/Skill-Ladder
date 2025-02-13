package com.example.skill_ladder.model;

public class JobField {
    private String id;
    private String name;
    private boolean isActive;

    public JobField() {}

    public JobField(String id, String name, boolean isActive) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

}
