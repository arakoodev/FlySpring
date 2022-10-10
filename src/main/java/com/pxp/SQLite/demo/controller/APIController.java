package com.pxp.SQLite.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pxp.SQLite.demo.entity.PersonEntity;
import com.pxp.SQLite.demo.repository.PersonRepo;
import com.pxp.SQLite.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/API")
public class APIController {

    @Autowired
    PersonRepo iPersonRepo;

    @RequestMapping("/")
    public String helloWorld(){
        return "Hello World from Spring Boot";
    }

    @RequestMapping(value = {"/article", "/article/{id}", "/article/{name}"} , method= RequestMethod.GET,
            consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JsonNode> get (@PathVariable Map<String, String> pathVarsMap,
                                         @RequestParam MultiValueMap<String,String> test, @RequestBody(required = false) JsonNode
                                                 requestBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
        String id  = pathVarsMap.get("id");
        String name  = pathVarsMap.get("name");
        // Code to parse multiplemap list

        Set<String> keys = test.keySet();

        for (String key : keys) {
            System.out.println("Key = " + key);
            System.out.println("Values = " + test.get(key));
            List<String> list = (List<String>) test.get(key);

            System.out.println("id : " +list.get(0));
            System.out.println("name : " + list.get(1));
        }
        if( id == null && name == null)
            return ResponseEntity.ok( mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}"));

        Optional<PersonEntity> pr =  iPersonRepo.findById(Integer.parseInt(id));
        pr.get().setId(Integer.parseInt(id));
        pr.get().setName(name);
        json = mapper.readTree("{\"ID\":  \""+pr.get().getId()+"\", \"name\": \""+pr.get().getName()+"\"}");

        return ResponseEntity.ok(json);
    }

    // INSERT Data in DB
    @RequestMapping(value = {"/article", "/article/{id}", "/article/{name}"} , method= RequestMethod.POST,
            consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JsonNode> PostData (@PathVariable Map<String, String> pathVarsMap,
                                              @RequestParam MultiValueMap<String,String> test, @RequestBody(required = false) JsonNode
                                                      requestBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
        String id  = pathVarsMap.get("id");
        String name  = pathVarsMap.get("name");

        // Set json in a Object of Entity Type to store in DB
        PersonEntity p = new PersonEntity();
        p.setId(Integer.parseInt(id));
        p.setName(name);

        // Calling Save and flush to save and close db connection of transaction
        iPersonRepo.saveAndFlush(p);

        return ResponseEntity.ok(json);
    }

    // Update data on base on ID
    @RequestMapping(value = {"/article", "/article/{id}", "/article/{name}"} , method= RequestMethod.PUT,
            consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JsonNode> UpdateData (@PathVariable Map<String, String> pathVarsMap,
                                                @RequestParam MultiValueMap<String,String> test, @RequestBody(required = false) JsonNode
                                                        requestBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
        String id  = pathVarsMap.get("id");
        String name  = pathVarsMap.get("name");

        Optional<PersonEntity> pr =  iPersonRepo.findById(Integer.parseInt(id));
        pr.get().setId(Integer.parseInt(id));
        pr.get().setName(name);

        iPersonRepo.saveAndFlush(pr.get());

        return ResponseEntity.ok(json);
    }


    @RequestMapping(value = {"/article", "/article/{id}", "/article/{name}"} , method= RequestMethod.DELETE,
            consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JsonNode> DeleteData (@PathVariable Map<String, String> pathVarsMap,
                                                @RequestParam MultiValueMap<String,String> test, @RequestBody(required = false) JsonNode
                                                        requestBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"ID\":  \"Not Found\", \"name\": \"Not Found\"}");
        String id  = pathVarsMap.get("id");
        String name  = pathVarsMap.get("name");

        iPersonRepo.deleteById(Integer.parseInt(id));

        return ResponseEntity.ok(json);
    }


}
