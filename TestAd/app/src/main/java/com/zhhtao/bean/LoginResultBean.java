package com.zhhtao.bean;

/**
 * @author Android客户端组-ZhangHaiTao
 * @version V1.0
 * @Title: LoginResultBean
 * @Package com.zhhtao.bean
 * @Description: TODO
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/6/8 13:55
 */
public class LoginResultBean {

    /**
     * userinfo : {"masterTel":null,"userId":"80","authToken":"TLFK8Z-8R7BQJWMOJIQTYMOYHYM1-ABZ6G6PI-I3"}
     */

    private ParamBean param;
    /**
     * param : {"userinfo":{"masterTel":null,"userId":"80","authToken":"TLFK8Z-8R7BQJWMOJIQTYMOYHYM1-ABZ6G6PI-I3"}}
     * returnValue : null
     * errMsg : null
     * resultCode : null
     * success : true
     */

    private Object returnValue;
    private Object errMsg;
    private Object resultCode;
    private boolean success;

    @Override
    public String toString() {
        return "LoginResultBean{" +
                "param=" + param +
                ", returnValue=" + returnValue +
                ", errMsg=" + errMsg +
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

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Object getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(Object errMsg) {
        this.errMsg = errMsg;
    }

    public Object getResultCode() {
        return resultCode;
    }

    public void setResultCode(Object resultCode) {
        this.resultCode = resultCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class ParamBean {
        /**
         * masterTel : null
         * userId : 80
         * authToken : TLFK8Z-8R7BQJWMOJIQTYMOYHYM1-ABZ6G6PI-I3
         */

        private UserinfoBean userinfo;

        public UserinfoBean getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(UserinfoBean userinfo) {
            this.userinfo = userinfo;
        }

        public static class UserinfoBean {
            private Object masterTel;
            private String userId;
            private String authToken;

            public Object getMasterTel() {
                return masterTel;
            }

            public void setMasterTel(Object masterTel) {
                this.masterTel = masterTel;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getAuthToken() {
                return authToken;
            }

            public void setAuthToken(String authToken) {
                this.authToken = authToken;
            }
        }
    }
}
