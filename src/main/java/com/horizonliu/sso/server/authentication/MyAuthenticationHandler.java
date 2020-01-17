package com.horizonliu.sso.server.authentication;

import com.horizonliu.sso.server.pojo.BaseResponse;
import com.horizonliu.sso.server.pojo.ErrCode;
import com.horizonliu.sso.server.pojo.login.LoginRequest;
import com.horizonliu.sso.server.service.RemoteAccountService;
import lombok.Data;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;

/**
 * @author horizonliu
 * @date 2020/1/16 4:50 PM
 */
@Data
public class MyAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MyAuthenticationHandler.class);

    private static RemoteAccountService accountService = new RemoteAccountService();

    public MyAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential,
                                                                                        String originalPassword) throws GeneralSecurityException, PreventedException {
        // 调用账号系统 电话号码密码登录
        LoginRequest request = new LoginRequest();
        request.setPhone(credential.getUsername());
        request.setPassword(credential.getPassword());
        ResponseEntity<BaseResponse> response = accountService.login(request);
        int code = response.getBody().getCode();
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
        // 登录成功,自定义返回客户端的信息
        List<String> setCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        LOG.info("set cookie:{}", setCookie);
        // TODO: 怎样增加其他认证信息返回
        HashMap<String, Object> userInfo = new HashMap<>();
        for (int i = 0; i < setCookie.size(); ++i) {
            userInfo.put("set-cookie" + i, setCookie.get(i));
        }
        return createHandlerResult(credential, this.principalFactory.createPrincipal(request.getPhone(), userInfo));
    }
}
