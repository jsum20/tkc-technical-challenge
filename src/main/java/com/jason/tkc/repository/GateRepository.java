package com.jason.tkc.repository;

import com.jason.tkc.model.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GateRepository extends JpaRepository<Gate, String> {
    // JPA usually has all the basic CRUD methods already, but can be used to add custom querying
}
