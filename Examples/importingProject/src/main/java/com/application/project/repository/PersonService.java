package com.application.project.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.application.project.entity.PersonEntity;
import com.application.project.utilities.JsonNodeMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import reactor.core.publisher.Mono;

@Service
public class PersonService {

    private JsonNodeMapper jsonMapper = new JsonNodeMapper();


    @Autowired
    PersonRepo personRepo;



    public Mono<JsonNode> getPersonByid(Integer id){
        Optional<PersonEntity> person =personRepo.findById(id);

        if(person.isEmpty()){
            return Mono.just(jsonMapper.notFound());
        }else{
            JsonNode json =jsonMapper.getJsonValue(person.get());
            return Mono.just(json);
        }


    }

    public Mono<JsonNode> savePerson(PersonEntity person){
         PersonEntity p=personRepo.saveAndFlush(person);
         JsonNode json =jsonMapper.getJsonValue(p);
        return Mono.just(json);

    }

    public Mono<JsonNode> updatePerson(PersonEntity person, int id){
        person.setId(id);
        PersonEntity p=personRepo.save(person);
        JsonNode json =jsonMapper.getJsonValue(p);
       return Mono.just(json);
    }

    public Mono<JsonNode> deletePerson( int id){
        personRepo.deleteById(id);
        if(personRepo.findById(id).isEmpty()){
            JsonNode json =jsonMapper.success("Deleted");
            return Mono.just(json);

        }else{
            JsonNode json =jsonMapper.success("Not Deleted");
            return Mono.just(json);
        }
    }
    
}
