package com.example.skill_ladder.model;

public class Cart {
    private int id;
    private String userId;
    private String lessonId;
    private String lessonName;
    private int lessonPrice;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getLessonId() { return lessonId; }
    public void setLessonId(String lessonId) { this.lessonId = lessonId; }

    public String getLessonName() { return lessonName; }
    public void setLessonName(String lessonName) { this.lessonName = lessonName; }

    public int getLessonPrice() { return lessonPrice; }
    public void setLessonPrice(int lessonPrice) { this.lessonPrice = lessonPrice; }


}
