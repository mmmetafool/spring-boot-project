package com.diploma.diplomaproject.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    @GetMapping("/performance")
    public String performance(Model model){

        {
            String url ="jdbc:mysql://localhost:3306/projectforschool?useUnicode=true&serverTimezone=UTC";
            String user ="root";
            String pass ="Gottmituns7";
            String username;
            String studID;
            String subjectInfo;


            List<String> subjectNameInfo = new ArrayList<>();
            List<String> academicPerf = new ArrayList<>();

            try {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof UserDetails) {
                    username = ((UserDetails)principal).getUsername();
                } else {
                    username = principal.toString();
                }
                Connection connection = DriverManager.getConnection(url,user,pass);
                Statement statement = connection.createStatement();


                ResultSet resultSet2 = statement.executeQuery("SELECT * FROM projectforschool.students WHERE userName = \""+username+"" + "\"");
                resultSet2.next();
                studID = resultSet2.getString("StudentID");
                System.out.println(studID);

                ResultSet resultSet3 = statement.executeQuery("SELECT DISTINCT subjectname FROM projectforschool.marks WHERE StudentID = \""+studID+"" + "\"");
                while (resultSet3.next()){
                    String subjectname = resultSet3.getString("subjectname");
                    subjectNameInfo.add(subjectname);
                }
                float avg=0;
                float counter=0;
                for (int i = 0; i!=subjectNameInfo.size(); i++){
                    subjectInfo="";
                    String sname = subjectNameInfo.get(i);
                    System.out.println(sname);
                    subjectInfo = subjectInfo.concat(sname+":");
                    ResultSet resultSet4 = statement.executeQuery("SELECT mark FROM projectforschool.marks WHERE StudentID = \""+ studID+"" + "\" and subjectname = \"" + sname +"\"");
                    while (resultSet4.next()){
                        String mark = resultSet4.getString("mark");
                        System.out.println(mark);
                        avg+=Integer.parseInt(mark);
                        counter++;
                        subjectInfo = subjectInfo.concat(" " + mark);
                    }
                    avg=avg/counter;
                    subjectInfo = subjectInfo.concat(" // Средний балл по предмету: " + String.format("%.2f", avg));
                    System.out.println(subjectInfo);
                    academicPerf.add(subjectInfo);
                    avg=0; counter = 0;
                }


                academicPerformanceInfo = academicPerf;

                resultSet2.close();
                resultSet3.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("academicPerformanceInfo",academicPerformanceInfo);

        return "performance";
    }

    @GetMapping("/schedule")
    public String schedule(Model model){


        {
            String url ="jdbc:mysql://localhost:3306/projectforschool?useUnicode=true&serverTimezone=UTC";
            String user ="root";
            String pass ="Gottmituns7";
            String username;
            String gradeNumber = "";
            Date date = new Date(System.currentTimeMillis());
            Integer num = 1;

            List<String> InfoToday = new ArrayList<>();
            List<String> InfoTomorrow = new ArrayList<>();
            List<String> InfoDayThree = new ArrayList<>();
            List<String> InfoDayFour = new ArrayList<>();
            List<String> InfoDayFive = new ArrayList<>();
            List<String> InfoDaySix = new ArrayList<>();
            List<String> InfoDaySeven = new ArrayList<>();

            try {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof UserDetails) {
                    username = ((UserDetails)principal).getUsername();
                } else {
                    username = principal.toString();
                }

                Connection connection = DriverManager.getConnection(url,user,pass);
                Statement statement = connection.createStatement();
                ResultSet resultSet1 = statement.executeQuery("SELECT * FROM projectforschool.students WHERE userName = \""+username+"" + "\"");
                resultSet1.next();
                gradeNumber = resultSet1.getString("Grade_ID");
                ResultSet resultSet = statement.executeQuery("SELECT * FROM projectforschool.lesson WHERE GradeID = \"" + gradeNumber +"\" and date = \"" + date+"\"");
                InfoToday.add(getDayOfWeek(LocalDate.parse(date.toString())).toUpperCase());
                while (resultSet.next()){
                    String allInfo = resultSet.getString("subjectname");
                    if (allInfo.equals(gradeNumber));
                    InfoToday.add(num + ": " + allInfo);
                    num++;
                }


                num=1;
                ResultSet resultSet2 = statement.executeQuery("SELECT * FROM projectforschool.lesson WHERE GradeID = \"" + gradeNumber +"\" and date = \"" + LocalDate.parse(date.toString()).plusDays(1) +"\"");
                InfoTomorrow.add(getDayOfWeek(LocalDate.parse(date.toString()).plusDays(1)).toUpperCase());
                while (resultSet2.next()){
                    String allInfo = resultSet2.getString("subjectname");
                    if (allInfo.equals(gradeNumber));
                    InfoTomorrow.add(num + ": " + allInfo);
                    num++;
                }

                num=1;
                ResultSet resultSet3 = statement.executeQuery("SELECT * FROM projectforschool.lesson WHERE GradeID = \"" + gradeNumber +"\" and date = \"" + LocalDate.parse(date.toString()).plusDays(2) +"\"");
                InfoDayThree.add(getDayOfWeek(LocalDate.parse(date.toString()).plusDays(2)).toUpperCase());
                while (resultSet3.next()){
                    String allInfo = resultSet3.getString("subjectname");
                    if (allInfo.equals(gradeNumber));
                    InfoDayThree.add(num + ": " + allInfo);
                    num++;
                }

                num=1;
                ResultSet resultSet4 = statement.executeQuery("SELECT * FROM projectforschool.lesson WHERE GradeID = \"" + gradeNumber +"\" and date = \"" + LocalDate.parse(date.toString()).plusDays(3) +"\"");
                InfoDayFour.add(getDayOfWeek(LocalDate.parse(date.toString()).plusDays(3)).toUpperCase());
                while (resultSet4.next()){
                    String allInfo = resultSet4.getString("subjectname");
                    if (allInfo.equals(gradeNumber));
                    InfoDayFour.add(num + ": " + allInfo);
                    num++;
                }

                num=1;
                ResultSet resultSet5 = statement.executeQuery("SELECT * FROM projectforschool.lesson WHERE GradeID = \"" + gradeNumber +"\" and date = \"" + LocalDate.parse(date.toString()).plusDays(4) +"\"");
                InfoDayFive.add(getDayOfWeek(LocalDate.parse(date.toString()).plusDays(4)).toUpperCase());
                while (resultSet5.next()){
                    String allInfo = resultSet5.getString("subjectname");
                    if (allInfo.equals(gradeNumber));
                    InfoDayFive.add(num + ": " + allInfo);
                    num++;
                }

                num=1;
                ResultSet resultSet6 = statement.executeQuery("SELECT * FROM projectforschool.lesson WHERE GradeID = \"" + gradeNumber +"\" and date = \"" + LocalDate.parse(date.toString()).plusDays(5) +"\"");
                InfoDaySix.add(getDayOfWeek(LocalDate.parse(date.toString()).plusDays(5)).toUpperCase());
                while (resultSet6.next()){
                    String allInfo = resultSet6.getString("subjectname");
                    if (allInfo.equals(gradeNumber));
                    InfoDaySix.add(num + ": " + allInfo);
                    num++;
                }

                num=1;
                ResultSet resultSet7 = statement.executeQuery("SELECT * FROM projectforschool.lesson WHERE GradeID = \""
                        + gradeNumber +"\" and date = \""
                        + LocalDate.parse(date.toString()).plusDays(6) +"\"");
                InfoDaySeven.add(getDayOfWeek(LocalDate.parse(date.toString()).plusDays(6)).toUpperCase());
                while (resultSet7.next()){
                    String allInfo = resultSet7.getString("subjectname");
                    if (allInfo.equals(gradeNumber));
                    InfoDaySeven.add(num + ": " + allInfo);
                    num++;
                }

                infoToday = InfoToday;
                infoTomorrow = InfoTomorrow;
                infoDayThree = InfoDayThree;
                infoDayFour = InfoDayFour;
                infoDayFive = InfoDayFive;
                infoDaySix = InfoDaySix;
                infoDaySeven = InfoDaySeven;

                resultSet.close();
                resultSet1.close();
                resultSet2.close();
                resultSet3.close();
                resultSet4.close();
                resultSet6.close();
                resultSet7.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
            String url ="jdbc:mysql://localhost:3306/projectforschool?useUnicode=true&serverTimezone=UTC";
            String user ="root";
            String pass ="Gottmituns7";
            String username;
            String studentsList;
            String studID;
            Date date = new Date(System.currentTimeMillis());
            Integer num = 1;
            String subjectInfo = "";


            List<String> Info = new ArrayList<>();
            List<String> subjectNameInfo = new ArrayList<>();
            List<String> academicPerf = new ArrayList<>();

            try {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof UserDetails) {
                    username = ((UserDetails)principal).getUsername();
                } else {
                    username = principal.toString();
                }
                Connection connection = DriverManager.getConnection(url,user,pass);
                Statement statement = connection.createStatement();
                ResultSet resultSet1 = statement.executeQuery("SELECT * FROM projectforschool.students WHERE userName = \""+username+"" + "\"");
                resultSet1.next();
                studentsList = resultSet1.getString("Grade_ID");

                ResultSet resultSet = statement.executeQuery("SELECT * FROM projectforschool.lesson WHERE GradeID = \"" + studentsList+"\" and date = \"" + date+"\"");
                while (resultSet.next()){
                    String allInfo = resultSet.getString("subjectname");
                    if (allInfo.equals(studentsList));
                    Info.add(num + ": " + allInfo);
                    num++;
                }


                ResultSet resultSet2 = statement.executeQuery("SELECT * FROM projectforschool.students WHERE userName = \""+username+"" + "\"");
                resultSet2.next();
                studID = resultSet2.getString("StudentID");

                ResultSet resultSet3 = statement.executeQuery("SELECT DISTINCT subjectname FROM projectforschool.marks WHERE StudentID = \""+studID+"" + "\"");
                while (resultSet3.next()){
                    String subjectname = resultSet3.getString("subjectname");
                    subjectNameInfo.add(subjectname);
                }

                int count = 0;
                for (int i = 0; i!=subjectNameInfo.size(); i++){
                    if (count>5) break;
                    subjectInfo="";
                    String sname = subjectNameInfo.get(i);
                    subjectInfo = subjectInfo.concat(sname+":");
                    ResultSet resultSet4 = statement.executeQuery("SELECT mark FROM projectforschool.marks WHERE StudentID = \""+studID+"" + "\" and subjectname = \"" + sname +"\"");
                    while (resultSet4.next()){
                        String mark = resultSet4.getString("mark");

                        subjectInfo = subjectInfo.concat(" " + mark);
                    }
                    academicPerf.add(subjectInfo);
                    count++;
                }



                infoToday = Info;
                academicPerformanceInfo = academicPerf;

                resultSet.close();
                resultSet1.close();
                resultSet2.close();
                resultSet3.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("infos",infoToday);
        model.addAttribute("academicPerformanceInfo",academicPerformanceInfo);

        return "hello";
    }

}
