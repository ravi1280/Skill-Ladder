package com.example.skill_ladder.model;

import java.util.List;

public class Lesson {
    private String jobField;
    private String jobTitle;
    private String lessonName;
    private String lessonStatus;
    private List<SubTopic> subTopics;

    public Lesson(String jobField, String jobTitle, String lessonName,String lessonstatus, List<SubTopic> subTopics) {
        this.jobField = jobField;
        this.jobTitle = jobTitle;
        this.lessonName = lessonName;
        this.lessonStatus = lessonstatus;
        this.subTopics = subTopics;
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

    public List<SubTopic> getSubTopics() {
        return subTopics;
    }

    public String getLessonStatus() {
        return lessonStatus;
    }


}