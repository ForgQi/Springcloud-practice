package com.forgqi.resourcebaseserver.dto;

import java.util.List;

public class CourseDTO {
    private Integer user;
    private Boolean modify = false;
    private Integer courseNum;   //课程序号2
    private String courseName;   //课程名3
    private String teacher;   //授课老师4
    private String courseCredit;   // 学分5
    private String courseColor; //课程颜色6
    private Integer day;    //周几上课7
    private String time;
    private Integer courseStartTime;  //哪一节开始上课8
    private Integer span;//节数9
    private String room; //教室号10

    private List<String> weekList;    //11

    private String week; //什么时间上课，哪几周，点击弹框最后一条内容12

    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(String courseCredit) {
        this.courseCredit = courseCredit;
    }

    public String getCourseColor() {
        return courseColor;
    }

    public void setCourseColor(String courseColor) {
        this.courseColor = courseColor;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public int getCourseStartTime() {
        return courseStartTime;
    }

    public void setCourseStartTime(int courseStartTime) {
        this.courseStartTime = courseStartTime;
    }

    public Integer getSpan() {
        return span;
    }

    public void setSpan(Integer span) {
        this.span = span;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<String> getWeekList() {
        return weekList;
    }

    public void setWeekList(List<String> weekList) {
        this.weekList = weekList;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getModify() {
        return modify;
    }

    public void setModify(Boolean modify) {
        this.modify = modify;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }
}
