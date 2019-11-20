package com.forgqi.resourcebaseserver.dto;

import com.forgqi.resourcebaseserver.entity.User;
import org.springframework.beans.BeanUtils;

public class StuInfoDTO {
    private Long id;
    private String name;
    private String college;
    private String subject;
    private String education;
    private String grade;
    private String classNo;
    private String idCard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public User convertToUser(LoginDTO loginDTO){
        User user = new User();
        BeanUtils.copyProperties(this, user);
        BeanUtils.copyProperties(loginDTO, user);
        return user;
    }
    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", college='" + college + '\'' +
                ", subject='" + subject + '\'' +
                ", education='" + education + '\'' +
                ", grade='" + grade + '\'' +
                ", classNo='" + classNo + '\'' +
                ", idCard='" + idCard + '\'' +
                '}';
    }
}
