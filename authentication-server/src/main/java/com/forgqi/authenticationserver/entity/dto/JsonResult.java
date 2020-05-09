package com.forgqi.authenticationserver.entity.dto;


public class JsonResult {
    /**
     * 状态文本
     */
    private String status;
    /**
     * 消息
     */
    private String msg;
    /**
     * 数据
     */
    private Object data;

    private JsonResult(String status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static JsonResult ok() {
        return new JsonResult("ok", "success", null);
    }

    public static JsonResult ok(String msg) {
        return new JsonResult("ok", msg, null);
    }

    public static JsonResult ok(Object data) {
        return new JsonResult("ok", "success", data);
    }

    public static JsonResult ok(String msg, Object data) {
        return new JsonResult("ok", msg, data);
    }

    public static JsonResult error() {
        return new JsonResult("error", "error", null);
    }

    public static JsonResult error(String msg) {
        return new JsonResult("error", msg, null);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
