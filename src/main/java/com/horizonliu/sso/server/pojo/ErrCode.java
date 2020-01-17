package com.horizonliu.sso.server.pojo;

/**
 * @author horizonliu
 * @date 2020/1/14 8:43 下午
 */
public enum ErrCode {
    /**
     * 错误码及提示配置
     */
    SUCC(0, "ok"),
    SYSTEM_ERROR(10000, "system error"),
    INVALID_ARGS(10001, "invalid args"),


    HAS_NOT_REG(12008, "has not reg"),
    PWD_ERR(12012, "username/password error"),
    LOGIN_TOO_MANY_FAIL(12062, "login failed too many times(5), please try again in 30 minutes."),
    PASSWORD_FAIL_2TIMES(12070, "password has been wrong two times, you have only one chance");


    private final int code;
    private final String description;

    ErrCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }
}
