package com.example.demo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  @Autowired RandomValueRepository randomValueRepository;

  @GetMapping("/saveRandom")
  String saveOne() {
    double r = Math.random();
    randomValueRepository.save(new RandomValue(r));
    return "saved: " + r;
  }

  @GetMapping("/getAll")
  List<RandomValue> getAll() {
    return randomValueRepository.findAll();
  }

  @GetMapping("/")
  String home() {
    return "value:" + Math.random();
  }
}
