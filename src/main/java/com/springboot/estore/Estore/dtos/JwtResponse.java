package com.springboot.estore.Estore.dtos;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private String token;
    private String refreshToken;
    UserDto userDto;
}
