package com.pxp.SQLite.demo.Resolver;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/*
 * Created By: baseer
 * Project: Fiver
 * Dated: 15-10-2022 0950 pm
 * Stackoverflow: https://stackoverflow.com/questions/73996101/spring-argumentresolver-to-get-pathvariable-map-and/74061738#74061738
 */

 
public class ArkRequest {

        public Map<String, String> pathVariables;
        public MultiValueMap<String, String> queryParameters;
        public JsonNode requestBody;


        ArkRequest() {
        }

        public ArkRequest(Map<String, String> pathVarsMap , MultiValueMap<String , String> mvm, JsonNode rbody) {
            this.pathVariables = pathVarsMap;
            this. queryParameters = mvm;
            this.requestBody = rbody;

        }

        public Map<String, String> getPathVariables() {
                return pathVariables;
        }

        public void setPathVariables(Map<String, String> pathVariables) {
                this.pathVariables = pathVariables;
        }

        public MultiValueMap<String, String> getQueryParameters() {
                return queryParameters;
        }

        public void setQueryParameters(MultiValueMap<String, String> queryParameters) {
                this.queryParameters = queryParameters;
        }

        public JsonNode getRequestBody() {
                return requestBody;
        }

        public void setRequestBody(JsonNode requestBody) {
                this.requestBody = requestBody;
        }
}
