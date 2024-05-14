package com.example.dailype.Controller;

import com.example.dailype.Entity.User;
import com.example.dailype.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/test")
public class HomeController {

    @Autowired
    private UserService userService;


    @PostMapping("/create_user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        return userService.createUser(user.getFullName(), user.getMobNum(), user.getPanNum(), user.getManager() != null ? user.getManager().getId() : null);
    }

    @PostMapping("/get_users")
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) UUID userId,
                               @RequestParam(required = false) String mobNum,
                               @RequestParam(required = false) UUID managerId) {
        return userService.getUsers(userId, mobNum, managerId);
    }

    @PostMapping("/delete_user")
    public ResponseEntity<String> deleteUser(@RequestParam(required = false) UUID userId,
                             @RequestParam(required = false) String mobNum) {
        return userService.deleteUser(userId, mobNum);
    }

    @PostMapping("/update_user")
    public ResponseEntity<String> updateUser(@RequestBody List<UUID> userIds,
                             @RequestBody User updateData) {
        return userService.updateUser(userIds, updateData);
    }
}
