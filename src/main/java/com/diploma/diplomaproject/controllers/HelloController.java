package com.diploma.diplomaproject.controllers;

import com.diploma.diplomaproject.models.Lesson;
import com.diploma.diplomaproject.models.Mark;
import com.diploma.diplomaproject.models.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Controller
public class HelloController {

    Iterable<String> academicPerformanceInfo;
    Iterable<String> infoToday;
    Iterable<String> infoTomorrow;
    Iterable<String> infoDayThree;
    Iterable<String> infoDayFour;
    Iterable<String> infoDayFive;
    Iterable<String> infoDaySix;
    Iterable<String> infoDaySeven;
    Iterable<String> daysOfWeek;

    private String getDayOfWeek(LocalDate date){
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        Locale localeRu = new Locale("ru", "RU");
        return dayOfWeek.getDisplayName(TextStyle.FULL,localeRu);
    }

    private Session getSession(){
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();
        return session;
    }

    private String getCurrentlyLoggedUsername(){
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return username;
    }

    @GetMapping("/performance")
    public String performance(Model model){

        {
            String username = getCurrentlyLoggedUsername();
            String studID ="";
            String subjectInfo;
            float avg=0;
            float counter=0;
            List<String> subjectNameInfo = new ArrayList<>();
            List<String> academicPerf = new ArrayList<>();


            Session session = getSession();
            List<Student> currentlyLogged = session.createQuery("From " + Student.class.getSimpleName() +
                    " Where userName = '" + username + "'").getResultList();
            for (Student name : currentlyLogged){
                studID = Integer.toString(name.getStudentID());
            }

            List marks = session.createQuery(
                    "SELECT DISTINCT M.subjectname FROM Mark M").getResultList();
            for (Object subj : marks){
                subjectNameInfo.add(subj.toString());
            }

            for (int i = 0; i!=subjectNameInfo.size(); i++){
                subjectInfo="";
                String subjName = subjectNameInfo.get(i);
                System.out.println(subjName);
                subjectInfo = subjectInfo.concat(subjName+":");
                List<Mark> markList = session.createQuery("From " + Mark.class.getSimpleName() +
                        " a Where StudentID = '" + studID +
                        "' and subjectname = '" + subjName + "'").getResultList();
                for (Mark mark : markList){
                    avg+=mark.getMark();
                    counter++;
                    subjectInfo = subjectInfo.concat(" " + mark.getMark());
                }
                avg=avg/counter;
                subjectInfo = subjectInfo.concat(" // Средний балл по предмету: " + String.format("%.2f", avg));
                academicPerf.add(subjectInfo);
                avg=0; counter = 0;
            }
            academicPerformanceInfo = academicPerf;

            session.close();

        }
        model.addAttribute("academicPerformanceInfo",academicPerformanceInfo);
        return "performance";
    }

    @GetMapping("/schedule")
    public String schedule(Model model){
        {
            String username;
            int gradeNumber = 0;
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            Locale localeRu = new Locale("ru", "RU");
            Integer num = 1;
            Session session = getSession();
            List<String> InfoToday = new ArrayList<>();
            List<String> InfoTomorrow = new ArrayList<>();
            List<String> InfoDayThree = new ArrayList<>();
            List<String> InfoDayFour = new ArrayList<>();
            List<String> InfoDayFive = new ArrayList<>();
            List<String> InfoDaySix = new ArrayList<>();
            List<String> InfoDaySeven = new ArrayList<>();

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }

            List<Student> currentlyLogged = session.createQuery("From " + Student.class.getSimpleName() +
                    " Where userName = '" + username + "'").getResultList();
            for (Student logged : currentlyLogged){
                gradeNumber = logged.getGradeID();
            }
            List<Lesson> lessons = session.createQuery("From " + Lesson.class.getSimpleName() +
                    " Where GradeID = '" + gradeNumber + "'").getResultList();
            InfoToday.add(getDayOfWeek(LocalDate.parse(date.toString())).toUpperCase());
            for (Lesson lesson : lessons){
                InfoToday.add(num + ": " + lesson.getSubjectname());
                num++;
            }
            num=1;
            List<Lesson> lessonsDayTwo = session.createQuery("From " + Lesson.class.getSimpleName() +
                    " Where GradeID = '" + gradeNumber + "'" +
                    "AND date = '" + LocalDate.parse(date.toString()).plusDays(1) + "'").getResultList();
            InfoTomorrow.add(getDayOfWeek(LocalDate.parse(date.toString()).plusDays(1)).toUpperCase());
            for (Lesson lesson : lessonsDayTwo){
                InfoTomorrow.add(num + ": " + lesson.getSubjectname());
                num++;
            }

            num=1;
            List<Lesson> lessonsDayThree = session.createQuery("From " + Lesson.class.getSimpleName() +
                    " Where GradeID = '" + gradeNumber + "'" +
                    "AND date = '" + LocalDate.parse(date.toString()).plusDays(2) + "'").getResultList();
            InfoTomorrow.add(getDayOfWeek(LocalDate.parse(date.toString()).plusDays(2)).toUpperCase());
            for (Lesson lesson : lessonsDayThree){
                InfoDayThree.add(num + ": " + lesson.getSubjectname());
                num++;
            }

            num=1;
            List<Lesson> lessonsDayFour = session.createQuery("From " + Lesson.class.getSimpleName() +
                    " Where GradeID = '" + gradeNumber + "'" +
                    "AND date = '" + LocalDate.parse(date.toString()).plusDays(3) + "'").getResultList();
            InfoTomorrow.add(getDayOfWeek(LocalDate.parse(date.toString()).plusDays(3)).toUpperCase());
            for (Lesson lesson : lessonsDayFour){
                InfoDayFour.add(num + ": " + lesson.getSubjectname());
                num++;
            }

            num=1;
            List<Lesson> lessonsDayFive = session.createQuery("From " + Lesson.class.getSimpleName() +
                    " Where GradeID = '" + gradeNumber + "'" +
                    "AND date = '" + LocalDate.parse(date.toString()).plusDays(4) + "'").getResultList();
            InfoTomorrow.add(getDayOfWeek(LocalDate.parse(date.toString()).plusDays(4)).toUpperCase());
            for (Lesson lesson : lessonsDayFive){
                InfoDayFive.add(num + ": " + lesson.getSubjectname());
                num++;
            }

            num=1;
            List<Lesson> lessonsDaySix = session.createQuery("From " + Lesson.class.getSimpleName() +
                    " Where GradeID = '" + gradeNumber + "'" +
                    "AND date = '" + LocalDate.parse(date.toString()).plusDays(5) + "'").getResultList();
            InfoTomorrow.add(getDayOfWeek(LocalDate.parse(date.toString()).plusDays(5)).toUpperCase());
            for (Lesson lesson : lessonsDaySix){
                InfoDaySix.add(num + ": " + lesson.getSubjectname());
                num++;
            }

            num=1;
            List<Lesson> lessonsDaySeven = session.createQuery("From " + Lesson.class.getSimpleName() +
                    " Where GradeID = '" + gradeNumber + "'" +
                    "AND date = '" + LocalDate.parse(date.toString()).plusDays(6) + "'").getResultList();
            InfoTomorrow.add(getDayOfWeek(LocalDate.parse(date.toString()).plusDays(6)).toUpperCase());
            for (Lesson lesson : lessonsDaySeven){
                InfoDaySeven.add(num + ": " + lesson.getSubjectname());
                num++;
            }
            infoToday = InfoToday;
            infoTomorrow = InfoTomorrow;
            infoDayThree = InfoDayThree;
            infoDayFour = InfoDayFour;
            infoDayFive = InfoDayFive;
            infoDaySix = InfoDaySix;
            infoDaySeven = InfoDaySeven;

            session.close();
        }

        model.addAttribute("infoToday",infoToday);
        model.addAttribute("infoTomorrow",infoTomorrow);
        model.addAttribute("infoDayThree",infoDayThree);
        model.addAttribute("infoDayFour",infoDayFour);
        model.addAttribute("infoDayFive",infoDayFive);
        model.addAttribute("infoDaySix",infoDaySix);
        model.addAttribute("infoDaySeven",infoDaySeven);
        model.addAttribute("daysOfWeek",daysOfWeek);

        return "schedule";
    }



    @GetMapping("/hello")
    public String hello(Model model){

        {
            String username;
            String GradeID = "";
            String studID = "";
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            Integer num = 1;
            String subjectInfo = "";
            Session session = getSession();

            List<String> Info = new ArrayList<>();
            List<String> subjectNameInfo = new ArrayList<>();
            List<String> academicPerf = new ArrayList<>();


            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }

            List<Student> user = session.createQuery("From " + Student.class.getSimpleName() + " Where userName = '" + username + "'").getResultList();
            for (Student currentUser : user){
                GradeID = Integer.toString(currentUser.getGradeID());
                studID = Integer.toString(currentUser.getStudentID());
            }

            List<Lesson> lessons = session.createQuery("From " + Lesson.class.getSimpleName() + " Where GradeID ='" + GradeID + "' and date ='" + date+"'").getResultList();
            for (Lesson lesson : lessons){
                Info.add(num + ": " + lesson.getSubjectname());
                num++;
            }
            List marks = session.createQuery(
                    "SELECT DISTINCT M.subjectname FROM Mark M").getResultList();
            for (Object subj : marks){
                subjectNameInfo.add(subj.toString());
            }
            for (int i = 0; i!=subjectNameInfo.size(); i++){
                subjectInfo="";
                String subjName = subjectNameInfo.get(i);
                System.out.println(subjName);
                subjectInfo = subjectInfo.concat(subjName+":");
                List<Mark> markList = session.createQuery("From " + Mark.class.getSimpleName() +
                        " a Where StudentID = '" + studID +
                        "' and subjectname = '" + subjName + "'").getResultList();
                for (Mark mark : markList){
                    subjectInfo = subjectInfo.concat(" " + mark.getMark());
                }
                academicPerf.add(subjectInfo);
            }
            infoToday = Info;
            academicPerformanceInfo = academicPerf;

            session.close();
        }

        model.addAttribute("infos",infoToday);
        model.addAttribute("academicPerformanceInfo",academicPerformanceInfo);

        return "hello";
    }

}
