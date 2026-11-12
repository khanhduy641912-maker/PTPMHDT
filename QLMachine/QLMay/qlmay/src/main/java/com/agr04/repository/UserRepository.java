package com.agr04.repository;

import com.agr04.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByPhone(String phone);
    boolean existsByPhone(String phone);
}