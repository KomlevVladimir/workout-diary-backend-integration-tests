package com.vladimirkomlev.workoutdiary.model;

public class UserCreateRequest {
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public UserCreateRequest withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserCreateRequest withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public int getAge() {
        return age;
    }

    public UserCreateRequest withAge(int age) {
        this.age = age;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserCreateRequest withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserCreateRequest withPassword(String password) {
        this.password = password;
        return this;
    }
}
