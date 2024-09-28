package com.springboot.estore.Estore.services;

import com.springboot.estore.Estore.dtos.PageableResponse;
import com.springboot.estore.Estore.dtos.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto userDto,String userId);

    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    UserDto getOneUserByUserId(String userId);

    void deleteUser(String userId);

    UserDto getOneUserByEmail(String userEmail);


    //TODO
    PageableResponse<UserDto> searchUser(String keyword,int pageNumber,int pageSize,String sortBy,String sortDir);
}
