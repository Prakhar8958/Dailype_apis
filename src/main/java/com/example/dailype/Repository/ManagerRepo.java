package com.example.dailype.Repository;

import com.example.dailype.Entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ManagerRepo extends JpaRepository<Manager, UUID> {

    boolean existsByIdAndIsActive(UUID id, boolean isActive);
}
