package com.horizonliu.sso.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 账号系统配置
 *
 * @author horizonliu
 * @date 2020/1/16 7:02 PM
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "remote-account")
@Data
public class RemoteAccountConfig {
    private String address = "http://127.0.0.1:80";
}
