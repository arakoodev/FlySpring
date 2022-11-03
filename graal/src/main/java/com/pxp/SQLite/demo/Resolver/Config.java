package com.pxp.SQLite.demo.Resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/*
 * Created By: baseer
 * Project: Fiver
 * Dated: 15-10-2022 0950 pm
 */
@Configuration
public class Config implements WebMvcConfigurer {
    @Autowired
    private MappingJackson2HttpMessageConverter messageConverter;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ArkRequestArgumentResolver(messageConverter));
    }

}
