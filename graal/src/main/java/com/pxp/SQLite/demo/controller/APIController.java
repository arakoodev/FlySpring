package com.pxp.SQLite.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pxp.SQLite.demo.InjectLogger;
import com.pxp.SQLite.demo.Resolver.ArkRequest;
import com.pxp.SQLite.demo.entity.PersonEntity;
import com.pxp.SQLite.demo.repository.PersonRepo;
//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/*
 * Created By: baseer
 * Project: Fiver
 * Dated: 15-10-2022 0950 pm
 *  *Stackoverflow:
 *  * https://stackoverflow.com/questions/73996101/spring-argumentresolver-to-get-pathvariable-map-and/74061738#74061738
 *  * https://stackoverflow.com/questions/6351082/using-java-annotation-to-inject-logger-dependency
 */
@RestController
@RequestMapping("/API")
public class APIController {

    @Autowired
    PersonRepo iPersonRepo;
    public static @InjectLogger Logger log;

    @RequestMapping(value = { "/HeloWorld/{name}/{id}" }, method = RequestMethod.GET)
    public static ResponseEntity<JsonNode> get(ArkRequest request) throws JsonProcessingException {

        log.info("Hello World Debugging log");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
        try {
            if (request.getPathVariables().get("name") != null || request.getPathVariables().get("id") != null) {
                log.info("Both parameters OK");
                 json = mapper.readTree("{\"ID\":  \"" + request.getPathVariables().get("name") + "\", \"name\": \"" + request.getPathVariables().get("id")  + "\"}");
            } else {
                log.warn("Null parameter warning!");
                json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
            }
        }
        catch(Exception e){
            log.error("Exception in Hello World");
        }

        return ResponseEntity.ok(json);
    }

    @RequestMapping(value = { "/Data/{name}/{id}" }, method = RequestMethod.GET)
    public ResponseEntity<JsonNode> Fetch (ArkRequest request) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
        try {
            if (request.getPathVariables().get("name") != null || request.getPathVariables().get("id") != null) {
                log.info("Both parameters OK");
                PersonEntity p = iPersonRepo.getOne(Integer.parseInt(request.getPathVariables().get("id")));
                json = mapper.readTree("{\"ID\":  \"" + p.getId() + "\", \"name\": \"" + p.getName() + "\"}");
            } else {
                log.warn("Null parameter warning!");
                json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
            }
        }
        catch(Exception e){}

        return ResponseEntity.ok(json);
    }

    @RequestMapping(value = { "/Data/{name}/{id}" }, method = RequestMethod.POST)
    public ResponseEntity<JsonNode> Save(ArkRequest request) throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
        if( request.getPathVariables().get("name") != null ||  request.getPathVariables().get("id") != null) {

            log.info("Both parameters OK");
            PersonEntity p = new PersonEntity();
            p.setId(Integer.parseInt(request.getPathVariables().get("id")));
            p.setName(request.getQueryParameters().get("test").get(0));

            iPersonRepo.saveAndFlush(p);

            json = mapper.readTree("{\"ID\":  \""+ p.getId() +"\", \"name\": \""+p.getName()+"\"}");
        }
        else
        {
            log.warn("Null parameter warning!");
            json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
        }
       return ResponseEntity.ok(json);
    }

    @RequestMapping(value = { "/Data/{name}/{id}" }, method = RequestMethod.PUT)
    public ResponseEntity<JsonNode> Update(ArkRequest request) throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
        if( request.getPathVariables().get("name") != null ||  request.getPathVariables().get("id") != null) {

            log.info("Both parameters OK");
            PersonEntity p = iPersonRepo.getOne(Integer.parseInt(request.getPathVariables().get("id")));
            p.setName(request.getQueryParameters().get("test").get(0));

            iPersonRepo.saveAndFlush(p);
            json = mapper.readTree("{\"ID\":  \""+ p.getId() +"\", \"name\": \""+p.getName()+"\"}");
        }
        else {
            log.warn("Null parameter warning!");
            json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
        }
        return ResponseEntity.ok(json);
    }

    @RequestMapping(value = { "/Data/{name}/{id}" }, method = RequestMethod.DELETE)
    public ResponseEntity<JsonNode> Delete(ArkRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
        if( request.getPathVariables().get("name") != null ||  request.getPathVariables().get("id") != null) {

            log.info("Both parameters OK");
            PersonEntity p = iPersonRepo.getOne(Integer.parseInt(request.getPathVariables().get("id")));
            json = mapper.readTree("{\"ID\":  \""+ p.getId() +"\", \"name\": \""+p.getName()+"\"}");
        }
        else {
            log.warn("Null parameter warning!");
            json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
        }
        iPersonRepo.deleteById(Integer.parseInt(request.getPathVariables().get("id")));

        return ResponseEntity.ok(json);
    }

}
