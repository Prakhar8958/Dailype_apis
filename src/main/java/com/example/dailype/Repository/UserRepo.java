package com.example.dailype.Repository;

import com.example.dailype.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    boolean existsByMobNum(String mobNum);
    User findByMobNum(String mobNum);
    List<User> findByManager_Id(UUID managerId);
}
