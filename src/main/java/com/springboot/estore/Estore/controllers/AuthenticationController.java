package com.springboot.estore.Estore.controllers;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.springboot.estore.Estore.dtos.GoogleLoginRequest;
import com.springboot.estore.Estore.dtos.JwtRequest;
import com.springboot.estore.Estore.dtos.JwtResponse;
import com.springboot.estore.Estore.dtos.UserDto;
import com.springboot.estore.Estore.entities.Providers;
import com.springboot.estore.Estore.entities.User;
import com.springboot.estore.Estore.exceptions.BadApiRequest;
import com.springboot.estore.Estore.exceptions.ResourceNotFoundException;
import com.springboot.estore.Estore.security.JwtHelper;
import com.springboot.estore.Estore.services.UserService;
import jakarta.validation.constraints.NotBlank;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    @Autowired
    private JwtHelper jwtHelper;

    @Value("${app.google.client_id}")
    private String gooleClientid;

    @Value("${app.google.client_password}")
    private String googleProviderDefaultPassword;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

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

    // google login controller which accepts id token from the frontend

    @PostMapping("/login-with-google")
    public ResponseEntity<JwtResponse> handleLoginWithGoogle(@RequestBody GoogleLoginRequest googleLoginRequest) throws GeneralSecurityException, IOException {
        logger.info("Id Token --> "+googleLoginRequest.getIdToken());
        //verify token

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new ApacheHttpTransport(), new GsonFactory())
                .setAudience(List.of(gooleClientid))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(googleLoginRequest.getIdToken());

        if(googleIdToken != null) {

            //verified
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            String userName = payload.getSubject();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            logger.info("Email --> "+email);
            logger.info("Name --> "+name);
            logger.info("Picture --> "+pictureUrl);
            logger.info("Username --> "+userName);

            UserDto userDto = new UserDto();
            userDto.setEmail(email);
            userDto.setName(name);
            userDto.setImage_name(pictureUrl);
            userDto.setPassword(googleProviderDefaultPassword);
            userDto.setAbout("user is created using google");
            userDto.setProviders(Providers.GOOGLE);


            UserDto userDto1 = null;

            try{
                userDto1 = userService.getOneUserByEmail(userDto.getEmail());
                logger.info("user is loaded from the DB");

                logger.info("Providers --> "+userDto1.getProviders().toString());

                if(userDto1.getProviders().equals(userDto.getProviders()))
                {
                    logger.info("Inside if condition");
                }
                else {
                    throw new BadApiRequest("Your email is already registered. Try login-with-google");
                }
            }
            catch(ResourceNotFoundException e){
                logger.info("user is created");
                userDto1 = userService.addUser(userDto);
            }




            this.doAuthenticate(userDto1.getEmail(), userDto1.getPassword());

            User user = modelMapper.map(userDto1, User.class);

            String token = jwtHelper.generateToken(user);

            JwtResponse jwtResponse = JwtResponse.builder().token(token).userDto(userDto1).build();
            return ResponseEntity.ok(jwtResponse);

        }

        else {
            logger.error("Invalid ID Token");
            throw new BadApiRequest("Invalid ID Token");
        }

    }

}
