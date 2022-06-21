package com.blogapi.blogappapis.controllers;

import com.blogapi.blogappapis.exceptions.ApiException;
import com.blogapi.blogappapis.payloads.JwtAuthRequest;
import com.blogapi.blogappapis.payloads.JwtAuthResponse;
import com.blogapi.blogappapis.payloads.UserDto;
import com.blogapi.blogappapis.security.JwtTokenHelper;
import com.blogapi.blogappapis.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {


    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {
            this.authenticate(request.getUsername(),request.getPassword());

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());

            String token = this.jwtTokenHelper.generateToken(userDetails);

            JwtAuthResponse response = new JwtAuthResponse();
            response.setToken(token);

            return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
           UserDto registeredUser = this.userService.registerNewUser(userDto);

           return new ResponseEntity<UserDto>(registeredUser,HttpStatus.CREATED);
    }

    private void authenticate(String username, String password) throws Exception {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,password);

        try {
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch (BadCredentialsException e){
            System.out.println("Invalid Details !");
            throw new ApiException("Invalid Uername Or Password");
        }

    }
}
