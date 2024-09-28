package com.springboot.estore.Estore.converters;

import com.springboot.estore.Estore.dtos.UserDto;
import com.springboot.estore.Estore.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    @Autowired
    private ModelMapper mapper;

    public User UserDtoToEntity(UserDto userDto)
    {
//        User user = User.builder()
//                .user_id(userDto.getUser_id())
//                .email(userDto.getEmail())
//                .about(userDto.getAbout())
//                .name(userDto.getName())
//                .gender(userDto.getGender())
//                .password(userDto.getPassword())
//                .image_name(userDto.getImage_name()).build();

        return mapper.map(userDto,User.class);


    }

    public UserDto UserEntityToDto(User user)
    {
//        UserDto userDto = UserDto.builder()
//                .user_id(user.getUser_id())
//                .about(user.getAbout())
//                .email(user.getEmail())
//                .gender(user.getGender())
//                .name(user.getName())
//                .password(user.getPassword())
//                .image_name(user.getImage_name()).build();
        return mapper.map(user,UserDto.class);
    }

    public List<UserDto> UserEntityListToDtoList(List<User> users)
    {
        return users.stream().map((user)->UserEntityToDto(user)).collect(Collectors.toList());
    }

    public List<User> UserDtoListToEntityList(List<UserDto> userDtos)
    {
        return userDtos.stream().map((userDto) -> UserDtoToEntity(userDto)).collect(Collectors.toList());
    }
}
