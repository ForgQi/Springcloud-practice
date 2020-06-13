package com.forgqi.resourcebaseserver.entity;

public interface GradeOnly {
    Detail getDetail();

    interface Detail {
        String getGrade();
    }
}
