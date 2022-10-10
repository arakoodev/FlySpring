package com.pxp.SQLite.demo.repository;

import com.pxp.SQLite.demo.entity.PersonEntity;
import com.pxp.SQLite.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PersonRepo extends JpaRepository<PersonEntity, Integer> {

  Optional<PersonEntity> findById(Integer id);

}
