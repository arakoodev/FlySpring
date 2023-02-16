package com.htmx.thymeleaf.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InfoRepo extends JpaRepository<Info, Integer> {
    Optional<Info> findByDate(String date);
}
