package com.example.dailype.Service;

import com.example.dailype.Entity.Manager;
import com.example.dailype.Entity.User;
import com.example.dailype.Repository.ManagerRepo;
import com.example.dailype.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userrepo;

    @Autowired
    private ManagerRepo managerrepo;

    List<UUID> managerids=new ArrayList<UUID>();
    @Override
    public ResponseEntity<String> createUser(User user) {
        if (user.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name must not be empty.");
        }

        String mobNum = validateMobileNumber(user.getMobNum());
        String panNum = user.getPanNum().toUpperCase();

        if (user.getManager().getId() != null && !managerrepo.existsByIdAndIsActive(user.getManager().getId(), true)) {
            throw new IllegalArgumentException("Manager ID is not valid or inactive.");
        }


        User newuser = new User();
        newuser.setFullName(user.getFullName());
        newuser.setMobNum(mobNum);
        newuser.setPanNum(panNum);
        if (user.getManager().getId() != null) {
            Manager manager = managerrepo.findById(user.getManager().getId()).orElseThrow(() -> new IllegalArgumentException("Manager not found"));
            newuser.setManager(manager);
            managerids.add(manager.getId());
        }
        /*if(user.getUser()!=null){
            User manger=userrepo.findById(user.getUser().getId()).orElseThrow(() -> new IllegalArgumentException("Not found"));
            newuser.setUser(manger);
        }*/
        if(user.getSubordinates()!=null){
            for(User subuser:user.getSubordinates()){
                User manger=userrepo.findById(subuser.getId()).orElseThrow(() -> new IllegalArgumentException("Not found"));
                newuser.getSubordinates().add(manger);
                newuser.setSubordinates(user.getSubordinates());
            }
        }

        newuser.setCreatedAt(Timestamp.from(Instant.now()));
        newuser.setUpdatedAt(Timestamp.from(Instant.now()));
        userrepo.save(newuser);

        try {
            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Try Again",HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<List<User>> getUsers(UUID userId, String mobNum, UUID managerId) {
        if (userId != null) {
            return new ResponseEntity<>(List.of(userrepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"))),HttpStatus.OK);
        } else if (mobNum != null) {
            return new ResponseEntity<>(List.of(userrepo.findByMobNum(mobNum)),HttpStatus.OK);
        } else if (managerId != null) {
            return new ResponseEntity<>(userrepo.findByManager_Id(managerId),HttpStatus.OK);
        }

        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> deleteUser(UUID userId, String mobNum) {
        if (userId != null) {
            userrepo.deleteById(userId);
        } else if (mobNum != null) {
            User user = userrepo.findByMobNum(mobNum);
            userrepo.delete(user);
        } else {
            throw new IllegalArgumentException("User ID or Mobile number must be provided.");
        }
        try {
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Try Again",HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<String> updateUser(List<UUID> userIds, User updateData) {
        for (UUID userId : userIds) {
            User user = userrepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

            if (updateData.getFullName() != null) {
                user.setFullName(updateData.getFullName());
            }
            if (updateData.getMobNum() != null) {
                user.setMobNum(validateMobileNumber(updateData.getMobNum()));
            }
            if (updateData.getPanNum() != null) {
                user.setPanNum(updateData.getPanNum().toUpperCase());
            }
            if (updateData.getManager() != null && !managerrepo.existsByIdAndIsActive(updateData.getManager().getId(), true)) {
                throw new IllegalArgumentException("Manager ID is not valid or inactive.");
            }

            user.setUpdatedAt(Timestamp.from(Instant.now()));
            userrepo.save(user);
        }
        try {
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Try Again",HttpStatus.BAD_REQUEST);

    }


    /*
    @Override
    public ResponseEntity<String> createNewUser(String fullName, String mobNum, String panNum, String mangerId) {
        Manager manager=managerrepo.getReferenceById(mangerId);
        User newuser=new User();
        if(managerids.contains(manager.getId())){
            newuser.setMangerid(manager);
        }
        if (fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name must not be empty.");
        }

        mobNum = validateMobileNumber(mobNum);
        panNum = panNum.toUpperCase();
        newuser.setCreatedAt(Timestamp.from(Instant.now()));
        newuser.setUpdatedAt(Timestamp.from(Instant.now()));
        userrepo.save(newuser);
        if(mangerId!=null){
            managerids.add(manager.getId());
        }
        try {
            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Try Again",HttpStatus.BAD_REQUEST);




    }
*/


    private String validateMobileNumber(String mobNum) {
        if (mobNum.startsWith("+91")) {
            mobNum = mobNum.substring(3);
        } else if (mobNum.startsWith("0")) {
            mobNum = mobNum.substring(1);
        }
        if (mobNum.length() != 10 || !mobNum.matches("\\d{10}")) {
            throw new IllegalArgumentException("Mobile number is not valid.");
        }
        return mobNum;
    }
}
