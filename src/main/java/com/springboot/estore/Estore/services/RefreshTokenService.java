package com.springboot.estore.Estore.services;

import com.springboot.estore.Estore.dtos.RefreshTokenDto;

public interface RefreshTokenService {

    RefreshTokenDto createRefreshToken(String userName);

    RefreshTokenDto findByToken(String token);

    void verifyRefreshToken(String token);
}
