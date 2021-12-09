package com.example.usermanagementservice.services.user;

import com.example.usermanagementservice.model.Role;
import com.example.usermanagementservice.model.User;
import com.example.usermanagementservice.repository.RoleRepository;
import com.example.usermanagementservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService,UserDetailsService {

    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user=userRepository.findByUsername(username);
      if (user==null){
          log.error("User Not Founded!");
          throw  new UsernameNotFoundException("User Not Founded!");
      }else {
          log.info("User {} is Founded",username);
      }
      Collection<SimpleGrantedAuthority>authorities=new ArrayList<>();
      user.getRole().forEach(role -> {
          authorities.add(new SimpleGrantedAuthority(role.getRolename()));
      });
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }

    @Override
    public User saveUser(User user) {

        log.info("New user save is Successfully");
         user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {

        log.info("new role {} save is successfully",role.getRolename());

        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {

        log.info("Adding role {} to user {}",roleName,userName);

        User user=userRepository.findByUsername(userName);
         Role role=roleRepository.findByRolename(roleName);
         user.getRole().add(role);
    }

    @Override
    public User getUser(String userName) {

        return userRepository.findByUsername(userName);
    }

    @Override
    public void deleteUser(String username) {
         userRepository.deleteByUsername(username);
    }


    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }


    @Override
    public String userMail(String username) {
        User user=userRepository.findByUsername(username);
        String email=user.getEmail();
        return email;
    }


}
