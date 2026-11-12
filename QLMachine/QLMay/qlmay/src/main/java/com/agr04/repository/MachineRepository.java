package com.agr04.repository;

import com.agr04.model.Machines;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<Machines, Integer> {
    java.util.List<Machines> findByOwner_UserIdOrderByMachineIdDesc(Integer userId);
}