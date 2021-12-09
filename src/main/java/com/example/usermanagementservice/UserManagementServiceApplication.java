package com.example.usermanagementservice;

import com.example.usermanagementservice.model.Role;
import com.example.usermanagementservice.model.User;
import com.example.usermanagementservice.services.user.UserService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer",in = SecuritySchemeIn.HEADER)
public class UserManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementServiceApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


     @Bean
      CommandLineRunner run(UserService userService){
        return args -> {
            userService.saveRole(new Role(null,"ROLE_USER"));
            userService.saveRole(new Role(null,"ROLE_MANAGER"));
            userService.saveRole(new Role(null,"ROLE_ADMIN"));
            userService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));

            userService.saveUser(new User(null,"john","1234","john@gmail.com","John Travolta",new ArrayList<>()));
            userService.saveUser(new User(null,"will","1234","will@gmail.com","Will Smith",new ArrayList<>()));
            userService.saveUser(new User(null,"jim","1234","jim@gmail.com","Jim Carry",new ArrayList<>()));
            userService.saveUser(new User(null,"arnold","1234","arnold@gmail.com","Arnold Schwarzenegger",new ArrayList<>()));
            userService.saveUser(new User(null,"muho","12345","*******","Muharrem Koc",new ArrayList<>()));
            userService.saveUser(new User(null,"mahmut","1234","mahmut@gmail.com","Mahmut",new ArrayList<>()));


            userService.addRoleToUser("john","ROLE_USER");
            userService.addRoleToUser("john","ROLE_ADMIN");
            userService.addRoleToUser("will","ROLE_MANAGER");
            userService.addRoleToUser("jim","ROLE_ADMIN");
            userService.addRoleToUser("jim","ROLE_USER");
            userService.addRoleToUser("arnold","ROLE_ADMIN");
            userService.addRoleToUser("arnold","ROLE_USER");
            userService.addRoleToUser("muho","ROLE_USER");
            userService.addRoleToUser("muho","ROLE_ADMIN");
            userService.addRoleToUser("muho","ROLE_SUPER_ADMIN");
            userService.addRoleToUser("muho","ROLE_SUPER_ADMIN");
            userService.addRoleToUser("mahmut","ROLE_USER");




        };
      }
}
