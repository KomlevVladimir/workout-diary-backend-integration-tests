package com.vladimirkomlev.workoutdiary.model;

import java.util.Objects;

public class UserResponse {
    private long id;
    private String firstName;
    private String lastName;
    private int age;
    private String email;

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserResponse)) return false;
        UserResponse response = (UserResponse) o;
        return getId() == response.getId() &&
                getAge() == response.getAge() &&
                Objects.equals(getFirstName(), response.getFirstName()) &&
                Objects.equals(getLastName(), response.getLastName()) &&
                Objects.equals(getEmail(), response.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getAge(), getEmail());
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }
}
