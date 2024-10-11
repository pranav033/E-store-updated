package com.springboot.estore.Estore.services.impl;

import com.springboot.estore.Estore.dtos.RefreshTokenDto;
import com.springboot.estore.Estore.dtos.UserDto;
import com.springboot.estore.Estore.entities.RefreshToken;
import com.springboot.estore.Estore.entities.User;
import com.springboot.estore.Estore.exceptions.ResourceNotFoundException;
import com.springboot.estore.Estore.repository.RefreshTokenRepository;
import com.springboot.estore.Estore.repository.UserRepository;
import com.springboot.estore.Estore.services.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RefreshTokenDto createRefreshToken(String userName) {

        User user = userRepository.findByEmail(userName).orElseThrow(() -> new ResourceNotFoundException("No user found with ths username"));

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(null);

        if(refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusSeconds(5 * 24 * 60 * 60))
                    .build();
        }
        else
        {
            refreshToken.setExpiryDate(Instant.now().plusSeconds(5 * 24 * 60 * 60));
            refreshToken.setToken(UUID.randomUUID().toString());
        }

        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);


        return modelMapper.map(savedRefreshToken,RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto findByToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("No token found in the Database."));

        return modelMapper.map(refreshToken,RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto) {
            var refreshToken = modelMapper.map(refreshTokenDto,RefreshToken.class);
            if(refreshTokenDto.getExpiryDate().compareTo(Instant.now()) < 0)
            {
                refreshTokenRepository.delete(refreshToken);
                throw new RuntimeException("Expired or invalid token");
            }
            return refreshTokenDto;
    }

    @Override
    public UserDto getUser(RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenDto.getToken()).orElseThrow(() -> new ResourceNotFoundException("No token found in the Database."));
        User user = refreshToken.getUser();
        return modelMapper.map(user,UserDto.class);
    }
}
