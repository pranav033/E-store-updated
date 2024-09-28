package com.springboot.estore.Estore.controllers;

import com.springboot.estore.Estore.converters.UserConverter;
import com.springboot.estore.Estore.dtos.ApiResponseMessage;
import com.springboot.estore.Estore.dtos.ImageResponse;
import com.springboot.estore.Estore.dtos.PageableResponse;
import com.springboot.estore.Estore.dtos.UserDto;
import com.springboot.estore.Estore.entities.User;
import com.springboot.estore.Estore.services.FileService;
import com.springboot.estore.Estore.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserConverter userConverter;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    @Autowired
    private FileService fileService;


    //create
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
    {
        UserDto userDto1 = userService.addUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId, @Valid @RequestBody UserDto userDto)
    {
        UserDto userDto2 = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(userDto2,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId)
    {
        userService.deleteUser(userId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("User Deleted.")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
    }


    //getall
    @GetMapping("/getall")
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "2",required = false) int pagesize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc" ,required = false) String sortDir

    )
    {
        PageableResponse<UserDto> allUsers = userService.getAllUsers(pageNumber,pagesize,sortBy,sortDir);
        return new ResponseEntity<>(allUsers,HttpStatus.OK);
    }


    //getsingle
    @GetMapping("/getone/{userId}")
    public ResponseEntity<UserDto> getOneUser(@PathVariable("userId") String userId)
    {
        UserDto oneUserByUserId = userService.getOneUserByUserId(userId);
        return new ResponseEntity<>(oneUserByUserId,HttpStatus.OK);
    }


    //get single by email
    @GetMapping("/getonebyemail/{emailId}")
    public ResponseEntity<UserDto> getOneUserByEmail(@PathVariable("emailId") String emailId)
    {
        UserDto oneUserByEmail = userService.getOneUserByEmail(emailId);
        return new ResponseEntity<>(oneUserByEmail,HttpStatus.OK);
    }


    //TODO
    //search users
    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<UserDto>> searchUsersByKeywords(@PathVariable("keyword") String keyword,
    @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
    @RequestParam(value = "pageSize",defaultValue = "2",required = false) int pagesize,
    @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
    @RequestParam(value = "sortDir",defaultValue = "asc" ,required = false) String sortDir
    )
    {
        PageableResponse<UserDto> userDtos = userService.searchUser(keyword,pageNumber,pagesize,sortBy,sortDir);
        return new ResponseEntity<>(userDtos,HttpStatus.OK);
    }


    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage") MultipartFile image,
            @PathVariable("userId") String userId
            ) throws IOException {
        String uploadedFile = fileService.uploadFile(image, imageUploadPath);
        UserDto userDto = userService.getOneUserByUserId(userId);
        userDto.setImage_name(uploadedFile);
        userService.updateUser(userDto,userId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(uploadedFile).success(true).status(HttpStatus.CREATED).message("Image uploaded successfully!").build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    @GetMapping("/serveimage/{userId}")
    public void serveUserImage(@PathVariable("userId") String userId, HttpServletResponse response) throws IOException {

        UserDto userDto = userService.getOneUserByUserId(userId);
        logger.info("user image name : "+userDto.getImage_name());
        InputStream resource = fileService.getResource(imageUploadPath, userDto.getImage_name());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }





}
