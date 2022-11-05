package com.application.project.autoRoute;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolverSupport;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ArkRequestArgumentResolverSupport extends HandlerMethodArgumentResolverSupport {
    
    public ArkRequestArgumentResolverSupport(ReactiveAdapterRegistry adapterRegistry) {
        super(adapterRegistry);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext,
            ServerWebExchange exchange) {
                ArkRequest request = new ArkRequest();

                request.queryParameters = exchange.getRequest().getQueryParams();
                request.pathVariables = resolvePathVariables(exchange);
                request.requestBody = resolveRequestBody(exchange);
        return Mono.just(request);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
      return ArkRequest.class.isAssignableFrom(parameter.getParameterType());
    }


    private Map<String, String> resolvePathVariables(ServerWebExchange webRequest) {
        Map<String, String> uriTemplateVars =  webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        return new LinkedHashMap<>(uriTemplateVars);
    }

    private Mono<JsonNode> resolveRequestBody(ServerWebExchange webExchange){
        Flux<DataBuffer> req =webExchange.getRequest().getBody();
        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream inputStream = new PipedInputStream(1024*10);
        try {
            inputStream.connect(outputStream);
            DataBufferUtils.write(req, outputStream).subscribe();
            return readContentFromPipedInputStream(inputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return Mono.empty();
        

    }

    private Mono<JsonNode> readContentFromPipedInputStream(PipedInputStream stream) throws IOException {
        StringBuffer contentStringBuffer = new StringBuffer();
        try {
            Thread pipeReader = new Thread(() -> {
                try {
                    contentStringBuffer.append(readContent(stream));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            pipeReader.start();
            pipeReader.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            stream.close();
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(String.valueOf(contentStringBuffer));
    
        return Mono.just(actualObj);
    }

    String readContent(InputStream stream) throws IOException {
        StringBuffer contentStringBuffer = new StringBuffer();
        byte[] tmp = new byte[stream.available()];
        int byteCount = stream.read(tmp, 0, tmp.length);
        contentStringBuffer.append(new String(tmp));
        return String.valueOf(contentStringBuffer);
    }
    
}
