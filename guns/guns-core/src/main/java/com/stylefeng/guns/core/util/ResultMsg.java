package com.stylefeng.guns.core.util;


/**
 * Created by Enzo Cotter on 2018/7/16.
 */
public class ResultMsg {

    private String success;
    private String message;
    private String detail;
    private Object data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResultMsg success(String message, String detail, Object data) {
        ResultMsg resultMsg = new ResultMsg();

        resultMsg.setSuccess("ok");
        resultMsg.setMessage(message);
        resultMsg.setDetail(detail);
        if (data != null)
            resultMsg.setData(data);

        return resultMsg;
    }

    public static ResultMsg fail(String message, String detail, Object data) {
        ResultMsg resultMsg = new ResultMsg();

        resultMsg.setSuccess("fail");
        resultMsg.setMessage(message);
        resultMsg.setDetail(detail);
        if (data != null)
            resultMsg.setData(data);

        return resultMsg;
    }

    public static ResultMsg unSuccess(String message) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setMessage(message);
        return resultMsg;
    }

}
