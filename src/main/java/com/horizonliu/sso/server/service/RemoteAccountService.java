package com.horizonliu.sso.server.service;

import com.horizonliu.sso.server.pojo.BaseResponse;
import com.horizonliu.sso.server.pojo.ErrCode;
import com.horizonliu.sso.server.pojo.login.LoginRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;

/**
 * @author horizonliu
 * @date 2020/1/16 6:59 PM
 */
public class RemoteAccountService extends BaseService {

    private final static String ACCOUNT_ADDRESS_FORMAT = "http://127.0.0.1:80/%s";
    private final static String LOGIN_URL = "v1/account/login/submit";

    public ResponseEntity<BaseResponse> login(LoginRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("companyId", "1");
        return post(String.format(ACCOUNT_ADDRESS_FORMAT, LOGIN_URL), headers, request, BaseResponse.class);
    }

    public void handleException(int code) throws Exception {
        if (code == ErrCode.HAS_NOT_REG.getCode()) {
            throw new AccountNotFoundException(" your account has not reg, plz reg first");
        } else if (code == ErrCode.PWD_ERR.getCode()) {
            throw new FailedLoginException("wrong password");
        } else if (code == ErrCode.LOGIN_TOO_MANY_FAIL.getCode()) {
            throw new AccountLockedException("login failed too many times(5), please try again in 30 minutes.");
        } else if (code == ErrCode.PASSWORD_FAIL_2TIMES.getCode()) {
            throw new FailedLoginException("password has been wrong two times, you have only one chance");
        } else if (code != ErrCode.SUCC.getCode()) {
            throw new AccountException("unknown exception");
        }
    }

}
