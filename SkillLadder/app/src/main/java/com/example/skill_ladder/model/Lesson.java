package com.example.skill_ladder.model;

import java.util.List;

public class Lesson {
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


}