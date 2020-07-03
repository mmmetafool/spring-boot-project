package com.diploma.diplomaproject.models;

import javax.persistence.*;

@Entity
@Table(name = "students")
public class Student
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StudentID")
    private int studentID;
    private String userName;
    private String password;
    private String fullName;
    private boolean enabled;
    @Column(name = "Grade_ID")
    private int GradeID;

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        studentID = studentID;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getGradeID() {
        return GradeID;
    }

    public void setGradeID(int grade_ID) {
        GradeID = grade_ID;
    }
}
