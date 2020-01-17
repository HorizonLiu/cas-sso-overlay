package com.horizonliu.sso.server.service;

import com.horizonliu.sso.server.exception.ReturnException;
import com.horizonliu.sso.server.pojo.ErrCode;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 封装Base POST请求
 *
 * @author milanyang
 * @date 2018/6/5
 */
public class BaseService {
    private static final Logger LOG = LoggerFactory.getLogger(BaseService.class);
    private static RestTemplate restTemplate = getRestTemplate();

    /**
     * 通用HTTP POST接口 -- 分为调用 微服务和非微服务 的三个接口
     *
     * @param uri          请求路径
     * @param headers      请求header
     * @param body         请求body
     * @param responseType 响应类型
     * @return
     */
    protected <T> ResponseEntity<T> post(String uri, HttpHeaders headers, Object body, Class<T> responseType) {
        return doPost(restTemplate, uri, headers, body, responseType);
    }

    private <T> ResponseEntity<T> doPost(RestTemplate restTemplate, String uri, HttpHeaders headers, Object body, Class<T> responseType) {
        HttpEntity request = new HttpEntity<>(body, headers);
        LOG.info("POST {} HTTP/1.1\nREQUEST: {} ", uri, request);
        try {
            ResponseEntity<T> responseEntity = restTemplate.postForEntity(uri, request, responseType);
            LOG.info("INNER RESPONSE: {}", responseEntity.getBody());
            return responseEntity;
        } catch (Exception ex) {
            throw new ReturnException(ErrCode.SYSTEM_ERROR.getCode(), uri + " execute wrong!! " + ex.getClass() + " " + ex.getMessage(), ex.getCause());
        }
    }

    private <T> ResponseEntity<T> doPost(RestTemplate restTemplate, String uri, HttpHeaders headers, Object body, Class<T> responseType, Object... uriVariables) {
        HttpEntity request = new HttpEntity<>(body, headers);
        LOG.info("POST {} HTTP/1.1\nREQUEST: {} ", uri, request);
        try {
            ResponseEntity<T> responseEntity = restTemplate.postForEntity(uri, request, responseType, uriVariables);
            LOG.info("INNER RESPONSE: {}", responseEntity.getBody());
            return responseEntity;
        } catch (Exception ex) {
            throw new ReturnException(ErrCode.SYSTEM_ERROR.getCode(), uri + " execute wrong!! " + ex.getClass() + " " + ex.getMessage(), ex.getCause());
        }
    }

    /**
     * 通用HTTP GET 请求，分为调用 微服务和非微服务 的两个接口
     *
     * @param uri          请求路径
     * @param headers      请求头
     * @param responseType 响应类型
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> get(String uri, HttpHeaders headers, Class<T> responseType) {
        return doGet(restTemplate, uri, headers, responseType);
    }

    private <T> ResponseEntity<T> doGet(RestTemplate restTemplate, String uri, HttpHeaders headers, Class<T> responseType) {
        LOG.info("GET {} HTTP/1.1 ", uri);
        HttpEntity entity = new HttpEntity(headers);
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, responseType);
            LOG.info("INNER RESPONSE: {}", responseEntity.getBody());
            return responseEntity;
        } catch (Exception ex) {
            throw new ReturnException(ErrCode.SYSTEM_ERROR.getCode(), uri + " execute wrong!! " + ex.getMessage(), ex.getCause());
        }
    }


    /**
     * 通用restTemplate
     */
    private static RestTemplate getRestTemplate() {

        HttpComponentsClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(getHttpClient());
        requestFactory.setConnectionRequestTimeout(3000);
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(15000);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        // 添加拦截器
        List<ClientHttpRequestInterceptor> interceptorList = new ArrayList<>();
        restTemplate.setInterceptors(interceptorList);
        // 设置messageConverter
        setHttpMessageConverter(restTemplate);
        return restTemplate;
    }

    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom().disableCookieManagement()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setMaxConnTotal(8192)
                .setMaxConnPerRoute(256)
                .build();
    }

    private static void setHttpMessageConverter(RestTemplate restTemplate) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }
}
