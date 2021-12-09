package com.example.usermanagementservice.services.user;

import com.example.usermanagementservice.model.Role;
import com.example.usermanagementservice.model.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String userName,String roleName);
    User getUser(String username);
    void deleteUser(String username);

    List<User> getUsers();
    String userMail(String username);


}
