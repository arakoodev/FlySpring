package com.pxp.SQLite.demo.Resolver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/*
 * Created By: baseer
 * Project: Fiver
 * Dated: 15-10-2022 0950 pm
 */
@RequiredArgsConstructor
public class ArkRequestArgumentResolver implements HandlerMethodArgumentResolver {
    private MappingJackson2HttpMessageConverter messageConverter;

    public ArkRequestArgumentResolver(MappingJackson2HttpMessageConverter messageConverter) {
        super();
        this.messageConverter = messageConverter;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return ArkRequest.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        ArkRequest request = new ArkRequest();

        request.queryParameters = resolveQueryParameters(webRequest);
        request.pathVariables = resolvePathVariables(webRequest);
        request.requestBody = resolveRequestBody(webRequest, parameter);

        return request;
    }

    private MultiValueMap<String, String> resolveQueryParameters(NativeWebRequest webRequest) {
        // resolve all query parameter into MultiValueMap
        Map<String, String[]> parameterMap = webRequest.getParameterMap();
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                result.add(key, value);
            }
        });
        return result;
    }

    private Map<String, String> resolvePathVariables(NativeWebRequest webRequest) {
        Map<String, String> uriTemplateVars = (Map<String, String>) webRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);

        if (!uriTemplateVars.isEmpty()) {
            return new LinkedHashMap<>(uriTemplateVars);
        } else {
            return Collections.emptyMap();
        }
    }

    private JsonNode resolveRequestBody(NativeWebRequest webRequest, MethodParameter parameter)
            throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {

        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpInputMessage inputMessage = new ServletServerHttpRequest(servletRequest);

        MediaType contentType;
        try {
            contentType = inputMessage.getHeaders().getContentType();
        } catch (InvalidMediaTypeException ex) {
            throw new HttpMediaTypeNotSupportedException(ex.getMessage());
        }
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        }
        Class<?> contextClass = parameter.getContainingClass();

        JsonNode body = JsonNodeFactory.instance.objectNode();

        if (messageConverter.canRead(JsonNode.class, contextClass, contentType)) {
            body = (JsonNode) messageConverter.read(JsonNode.class, inputMessage);
        }

        return body;
    }
}
