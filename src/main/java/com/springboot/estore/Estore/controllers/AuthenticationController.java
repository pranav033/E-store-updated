package com.springboot.estore.Estore.controllers;


import com.springboot.estore.Estore.dtos.JwtRequest;
import com.springboot.estore.Estore.dtos.JwtResponse;
import com.springboot.estore.Estore.dtos.UserDto;
import com.springboot.estore.Estore.entities.User;
import com.springboot.estore.Estore.security.JwtHelper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper modelMapper;



    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {

            logger.info("Email --> "+jwtRequest.getEmail());
            logger.info("Password --> "+jwtRequest.getPassword());

            this.doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());

        User user = (com.springboot.estore.Estore.entities.User)userDetailsService.loadUserByUsername(jwtRequest.getEmail());

        String token = jwtHelper.generateToken(user);

        JwtResponse jwtResponse = JwtResponse.builder().token(token).userDto(modelMapper.map(user, UserDto.class)).build();
        return ResponseEntity.ok(jwtResponse);
    }

    private void doAuthenticate(String email, String password) {

        try{

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }
        catch(BadCredentialsException ex){
            throw new BadCredentialsException("Invalid email and password");
        }

    }

}
