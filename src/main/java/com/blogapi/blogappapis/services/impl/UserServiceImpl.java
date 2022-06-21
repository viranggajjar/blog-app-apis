package com.blogapi.blogappapis.services.impl;

import com.blogapi.blogappapis.config.AppConstants;
import com.blogapi.blogappapis.entities.Role;
import com.blogapi.blogappapis.entities.User;
import com.blogapi.blogappapis.exceptions.ResourceNotFoundException;
import com.blogapi.blogappapis.payloads.UserDto;
import com.blogapi.blogappapis.repositories.RoleRepo;
import com.blogapi.blogappapis.repositories.UserRepo;
import com.blogapi.blogappapis.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public UserDto registerNewUser(UserDto userDto) {
        User user= this.modelMapper.map(userDto,User.class);

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        Role role = this.roleRepo.findById(AppConstants.ADMIN_USER).get();

        user.getRoles().add(role);

        User newUser = this.userRepo.save(user);

        return this.userToDto(newUser);
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        User user = this.dtoToUser(userDto);
        System.out.println(user);
        User savedUser = this.userRepo.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {

        User user= this.userRepo.findById(userId).orElseThrow((()-> new ResourceNotFoundException("User","Id",userId)));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        User updatedUser = this.userRepo.save(user);

        return this.userToDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Integer userId) {

        User user= this.userRepo.findById(userId).orElseThrow((()-> new ResourceNotFoundException("User","Id",userId)));


        return this.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {

        List<User> users = this.userRepo.findAll();

        return users.stream().map(this::userToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer userId) {

        User user= this.userRepo.findById(userId).orElseThrow((()-> new ResourceNotFoundException("User","Id",userId)));

        this.userRepo.delete(user);
    }

    public User dtoToUser(UserDto userDto){
        User user= this.modelMapper.map(userDto,User.class);

        /*user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());*/

        return user;

    }

    public UserDto userToDto(User user){
        UserDto userDto= this.modelMapper.map(user,UserDto.class);

        return userDto;
    }
}
