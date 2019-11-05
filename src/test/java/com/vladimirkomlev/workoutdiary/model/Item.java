package com.vladimirkomlev.workoutdiary.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Item {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("Content")
    private Map<String, Object> content;

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", content=" + content +
                '}';
    }

    public String getBody() {
        return String.valueOf(content.get("Body"));
    }
}

