package com.springboot.estore.Estore.repository;

import com.springboot.estore.Estore.entities.RefreshToken;
import com.springboot.estore.Estore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}
