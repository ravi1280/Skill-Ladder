package com.example.skill_ladder.model;

public class JobTitle {
    private String id;
    private String name;
    private String fieldId;
    private String fieldName;
    private boolean isActive;

    // Empty constructor (Required for Firestore)
    public JobTitle() {}

    // Constructor with parameters
    public JobTitle(String id, String name, String fieldId, String fieldName, boolean isActive) {
        this.id = id;
        this.name = name;
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.isActive = isActive;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFieldId() {
        return fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isActive() {
        return isActive;
    }

    // Setters (if needed)
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
