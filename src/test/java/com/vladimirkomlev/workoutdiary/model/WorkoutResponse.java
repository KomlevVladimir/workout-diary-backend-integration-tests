package com.vladimirkomlev.workoutdiary.model;

import java.time.LocalDate;
import java.util.Objects;

public class WorkoutResponse {
    private long id;
    private String title;
    private LocalDate date;
    private String description;

    @Override
    public String toString() {
        return "WorkoutResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkoutResponse)) return false;
        WorkoutResponse that = (WorkoutResponse) o;
        return getId() == that.getId() &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDate(), getDescription());
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
