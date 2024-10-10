package com.springboot.estore.Estore.dtos;

import com.springboot.estore.Estore.entities.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenDto {

    private String token;


    private int id;

    private Instant expiryDate;

    @OneToOne
    private UserDto userDto;
}
