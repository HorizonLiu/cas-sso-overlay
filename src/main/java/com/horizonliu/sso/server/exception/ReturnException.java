package com.horizonliu.sso.server.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * @author horizonliu
 * @date 2018/5/14
 */

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class ReturnException extends RuntimeException {
    /**
     * 错误码
     */
    int errCode;

    /**
     * 调用外部服务返回的状态码
     */
    HttpStatus httpStatus;

    public ReturnException(int errCode, String msg, Throwable throwable) {
        super(msg, throwable);
        this.errCode = errCode;
    }

    public ReturnException(HttpStatus status, String msg) {
        super(msg);
        this.httpStatus = status;
    }
}
