package com.example.dailype.Service;

import com.example.dailype.Entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {

    ResponseEntity<String> createUser(String fullName, String mobNum, String panNum, UUID managerId);

    ResponseEntity<List<User>> getUsers(UUID userId, String mobNum, UUID managerId);

    ResponseEntity<String> deleteUser(UUID userId, String mobNum);

    ResponseEntity<String> updateUser(List<UUID> userIds, User updateData);


}
