package com.blogapi.blogappapis;

import com.blogapi.blogappapis.config.AppConstants;
import com.blogapi.blogappapis.entities.Role;
import com.blogapi.blogappapis.payloads.RoleDto;
import com.blogapi.blogappapis.repositories.RoleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class BlogAppApisApplication implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    public static void main(String[] args) {
        SpringApplication.run(BlogAppApisApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Override
    public void run(String... args) throws Exception {
       // System.out.println(this.passwordEncoder.encode("virangs"));

        try {
            Role role = new Role();
            role.setId(AppConstants.ADMIN_USER);
            role.setName("ADMIN_USER");

            Role role1 = new Role();
            role1.setId(AppConstants.NORMAL_USER);
            role1.setName("NORMAL_USER");

            List<Role> roles =  List.of(role,role1);

            /*List<Role> result = this.roleRepo.saveAll(roles);

            result.forEach(r->{
                System.out.println(modelMapper().map(r, RoleDto.class).getName());
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
