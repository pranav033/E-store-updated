package com.springboot.estore.Estore.services;

import com.springboot.estore.Estore.dtos.RefreshTokenDto;
import com.springboot.estore.Estore.dtos.UserDto;

public interface RefreshTokenService {

    RefreshTokenDto createRefreshToken(String userName);

    RefreshTokenDto findByToken(String token);

    RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto);

    UserDto getUser(RefreshTokenDto refreshTokenDto);
}
