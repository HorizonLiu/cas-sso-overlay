package com.horizonliu.sso.server.pojo.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CaptchaData implements Serializable {
    @JsonProperty("ticket")
    private String ticket = "";
    @JsonProperty("randStr")
    private String randStr = "";
}
