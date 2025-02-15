package com.example.skill_ladder.model;

import java.util.List;

public class Lesson {




    private String id;
    private String jobField;
    private String jobTitle;
    private String lessonName;

    private Integer price;

    private List<SubTopic> subTopics;

    private boolean isActive;

    public Lesson(String jobField, String jobTitle, String lessonName,Integer price, List<SubTopic> subTopics,boolean isActive) {
        this.jobField = jobField;
        this.jobTitle = jobTitle;
        this.lessonName = lessonName;
        this.price = price;
        this.subTopics = subTopics;
        this.isActive = isActive;
    }
    public Lesson(String id,String lessonName, boolean isActive) {
        this.id = id;
        this.lessonName = lessonName;
        this.isActive = isActive;
    }
    public Lesson(String id,String lessonName,Integer price, boolean isActive) {
        this.id = id;
        this.lessonName = lessonName;
        this.price = price;
        this.isActive = isActive;
    }

    public String getJobField() {
        return jobField;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getLessonName() {
        return lessonName;
    }

    public Integer getPrice() {
        return price;
    }

    public List<SubTopic> getSubTopics() {
        return subTopics;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setJobField(String jobField) {
        this.jobField = jobField;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setSubTopics(List<SubTopic> subTopics) {
        this.subTopics = subTopics;
    }


}