package com.forgqi.resourcebaseserver.service.dto;

import lombok.Data;

import java.util.List;

@Data
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
}
