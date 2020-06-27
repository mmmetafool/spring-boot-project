package com.diploma.diplomaproject.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "lesson")
public class lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer LessonID;

    private Integer GradeID;
    private Integer TeachersID;
    private String subjectname;
    private Date date;

    public Integer getLessonID() {
        return LessonID;
    }

    public void setLessonID(Integer lessonID) {
        LessonID = lessonID;
    }

    public Integer getGradeID() {
        return GradeID;
    }

    public void setGradeID(Integer gradeID) {
        GradeID = gradeID;
    }

    public Integer getTeachersID() {
        return TeachersID;
    }

    public void setTeachersID(Integer teachersID) {
        TeachersID = teachersID;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
