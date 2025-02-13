package com.example.skill_ladder.model;

public class JobTitle {
    private String id;
    private String fieldId;
    private String name;
    private boolean isActive;

    public JobTitle() {}

    public JobTitle(String id, String fieldId, String name, boolean isActive) {
        this.id = id;
        this.fieldId = fieldId;
        this.name = name;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public String getFieldId() {
        return fieldId;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }
}
