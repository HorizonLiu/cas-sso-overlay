package com.horizonliu.sso.server.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author horizonliu
 * @date 2020/1/14 8:41 下午
 */
@Data
@AllArgsConstructor
@Slf4j
public class BaseResponse {
    @JsonProperty("code")
    private int code;
    @JsonProperty("msg")
    private String msg;

    public BaseResponse() {
        this(ErrCode.SUCC);
    }

    public BaseResponse(ErrCode code) {
        this.code = code.getCode();
        this.msg = code.getDescription();
    }
}
