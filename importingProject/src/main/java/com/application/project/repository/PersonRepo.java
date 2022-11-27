package com.application.project.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.project.entity.PersonEntity;

@Repository
public interface PersonRepo extends JpaRepository<PersonEntity, Integer> {

  Optional<PersonEntity> findById(Integer id);

Optional<PersonEntity> saveAndFlush(Optional<PersonEntity> person);

}
