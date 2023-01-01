package com.example.demo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface RandomValueRepository extends CrudRepository<RandomValue, Long> {

  List<RandomValue> findAll();

  RandomValue findById(long id);
}
