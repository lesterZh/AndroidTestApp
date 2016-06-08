package com.zhhtao.bean;

/**
 * @author Android客户端组-ZhangHaiTao
 * @version V1.0
 * @Title: SmsVerificationCodeBean
 * @Package com.zhhtao.bean
 * @Description: TODO
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/6/8 11:15
 */
public class SmsVerificationCodeBean {

    private ParamBean param;
    /**
     * param : {}
     * returnValue : null
     * errMsg : E013
     * resultCode : null
     * success : true
     */

    private Integer returnValue;
    private String errMsg;
    private Integer resultCode;
    private boolean success;

    @Override
    public String toString() {
        return "SmsVerificationCodeBean{" +
                "param=" + param +
                ", returnValue=" + returnValue +
                ", errMsg='" + errMsg + '\'' +
                ", resultCode=" + resultCode +
                ", success=" + success +
                '}';
    }

    public ParamBean getParam() {
        return param;
    }

    public void setParam(ParamBean param) {
        this.param = param;
    }

    public Integer getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Integer returnValue) {
        this.returnValue = returnValue;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class ParamBean {
    }
}
