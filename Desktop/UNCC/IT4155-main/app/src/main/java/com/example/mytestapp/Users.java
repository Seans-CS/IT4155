package com.example.mytestapp;

public class Users {

    String firstName, lastName, major, age, gender, bio;

    public Users(String firstName, String lastName, String age, String major, String gender, String bio) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.major = major;
        this.gender = gender;
        this.bio = bio;

    }

    public Users() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGender(){return gender; }

    public void setGender(String gender){this.gender = gender; }

    public String getBio(){ return bio; }

    public void setBio(String bio){this.bio = bio; }

}
