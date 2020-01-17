package com.horizonliu.sso.server.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author horizonliu
 * @date 2020/1/14 8:42 下午
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DataResponse<T> extends BaseResponse {
    @JsonProperty("data")
    private T data;

    public DataResponse(T data) {
        super(ErrCode.SUCC);
        this.data = data;
    }

    public DataResponse(ErrCode code, T data) {
        super(code);
        this.data = data;
    }
}
