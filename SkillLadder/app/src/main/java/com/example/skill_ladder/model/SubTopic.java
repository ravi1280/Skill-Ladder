package com.example.skill_ladder.model;

public class SubTopic {
    private String subTopicName;
    private String contentText;
    private String webUrl;
    private String ytVideoUrl;

    public SubTopic(String subTopicName, String contentText, String webUrl, String ytVideoUrl) {
        this.subTopicName = subTopicName;
        this.contentText = contentText;
        this.webUrl = webUrl;
        this.ytVideoUrl = ytVideoUrl;
    }

    public String getSubTopicName() {
        return subTopicName;
    }

    public String getContentText() {
        return contentText;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getYtVideoUrl() {
        return ytVideoUrl;
    }
}
