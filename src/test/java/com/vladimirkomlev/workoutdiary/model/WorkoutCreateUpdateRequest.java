package com.vladimirkomlev.workoutdiary.model;

public class WorkoutCreateUpdateRequest {
    private String title;
    private String date;
    private String description;

    @Override
    public String toString() {
        return "WorkoutCreateUpdateRequest{" +
                "title='" + title + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public WorkoutCreateUpdateRequest withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDate() {
        return date;
    }

    public WorkoutCreateUpdateRequest withDate(String date) {
        this.date = date;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public WorkoutCreateUpdateRequest withDescription(String description) {
        this.description = description;
        return this;
    }
}
