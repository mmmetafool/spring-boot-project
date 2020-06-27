package com.diploma.diplomaproject.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "students")
public class students {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer StudentID;
    private Integer Grade_ID;
    private String userName;
    private String password;
    private String fullName;
    private Boolean enabled;

    public Integer getGrade_ID() {
        return Grade_ID;
    }

    public void setGrade_ID(Integer grade_ID) {
        Grade_ID = grade_ID;
    }

    public Integer getStudentID() {
        return StudentID;
    }

    public void setStudentID(Integer studentID) {
        StudentID = studentID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
