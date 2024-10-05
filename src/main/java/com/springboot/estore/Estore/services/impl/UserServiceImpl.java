package com.springboot.estore.Estore.services.impl;

import com.springboot.estore.Estore.config.AppConstants;
import com.springboot.estore.Estore.converters.UserConverter;
import com.springboot.estore.Estore.dtos.PageableResponse;
import com.springboot.estore.Estore.dtos.UserDto;
import com.springboot.estore.Estore.entities.Role;
import com.springboot.estore.Estore.entities.User;
import com.springboot.estore.Estore.exceptions.ResourceNotFoundException;
import com.springboot.estore.Estore.helpers.Helper;
import com.springboot.estore.Estore.repository.RoleRepository;
import com.springboot.estore.Estore.repository.UserRepository;
import com.springboot.estore.Estore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.module.ResolutionException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;
    
    @Value("${user.profile.image.path}")
    private String imagePath;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public UserDto addUser(UserDto userDto) {
        String userId = UUID.randomUUID().toString();
        userDto.setUser_id(userId);
        User user = userConverter.UserDtoToEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = new Role();
        role.setRoleId(UUID.randomUUID().toString());
        role.setRoleName("ROLE_"+ AppConstants.ROLE_NORMAL);
        Role roleNormal = roleRepository.findByRoleName("ROLE_"+AppConstants.ROLE_NORMAL).orElse(role);
        user.setRoles(List.of(roleNormal));
        userRepository.save(user);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with this id"));
        user.setAbout(userDto.getAbout());
        user.setName(userDto.getName());
        user.setGender(userDto.getGender());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setImage_name(userDto.getImage_name());
        User savedUser = userRepository.save(user);
        UserDto userDto1 = userConverter.UserEntityToDto(savedUser);
        return userDto1;
    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<User> userPage = userRepository.findAll(pageable);
        PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(userPage,UserDto.class);
        return  pageableResponse;

    }

    @Override
    public UserDto getOneUserByUserId(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with this id"));
        UserDto userDto = userConverter.UserEntityToDto(user);
        return userDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with this id"));
        //delete user image 
        String imageName = user.getImage_name();
        String fullImagePath = imagePath + imageName;
        Path path = Paths.get(fullImagePath);
        try {
            Files.delete(path);
        } catch (NoSuchFileException ex){
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //delete user
        userRepository.delete(user);

    }

    @Override
    public UserDto getOneUserByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        UserDto userDto = userConverter.UserEntityToDto(user);
        return userDto;
    }

    @Override
    public PageableResponse<UserDto> searchUser(String keyword,int pageNumber,int pageSize,String sortBy,String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);

        Page<User> userList = userRepository.findByNameContaining(keyword,pageable);
        PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(userList,UserDto.class);
        return  pageableResponse;
    }
}
