package com.application.project.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonNodeMapper {

    private ObjectMapper mapper = new ObjectMapper();


    public JsonNode notFound(){
        try {
            return mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
        
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public JsonNode success(String message){
        try {
            return mapper.readTree("{\"ID\":  \"Success\", \"message\": \""+message+"\"}");
        
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public JsonNode getJsonValue(Object object){
        return mapper.convertValue(object, JsonNode.class);
    }
    
}
