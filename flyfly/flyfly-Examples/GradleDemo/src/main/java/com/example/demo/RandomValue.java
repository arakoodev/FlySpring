package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RandomValue {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Double value;

  protected RandomValue() {}

  public RandomValue(Double value) {
    this.value = value;
  }

  public Long getId() {
    return id;
  }

  public Double getValue() {
    return value;
  }
}
