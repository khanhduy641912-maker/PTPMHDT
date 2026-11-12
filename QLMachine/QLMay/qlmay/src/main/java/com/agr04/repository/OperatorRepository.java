package com.agr04.repository;

import com.agr04.model.Operators;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OperatorRepository extends JpaRepository<Operators, Integer> {
    List<Operators> findByIsEligibleTrue();
    Optional<Operators> findByUser_UserId(Integer userId);
}