package com.forgqi.resourcebaseserver.common;

//切勿轻易改变顺序，保存在数据库中的是数字
public interface Voted{
    enum State {
        UP,
        DOWN
    }


    enum Type {
        POST,
        COMMENT,
        STUDY_MODE
    }
}


