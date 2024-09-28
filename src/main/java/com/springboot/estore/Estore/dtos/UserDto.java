package com.springboot.estore.Estore.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String user_id;


    //@Column(name = "user_email",unique = true)
    @Pattern(regexp = "^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$",message = "Invalid email")
    @NotBlank(message = "Invalid email")
    private String email;

    //@Column(name = "user_password")
    @NotBlank(message = "Password required.")
    private String password;


    //@Column(name = "user_gender")
    @Size(min = 4,max = 6,message = "Invalid gender.")
    private String gender;

    //@Column(name = "user_about",length = 1000)
    @NotBlank(message = "Add something about yourself.")
    private String about;


    //@Column(name = "user_image_name")
    private String image_name;

    @Size(min = 3,max = 25,message = "Invalid name.")
    private String name;
}
