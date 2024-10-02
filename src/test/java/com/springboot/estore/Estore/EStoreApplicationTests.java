package com.springboot.estore.Estore;

import com.springboot.estore.Estore.entities.User;
import com.springboot.estore.Estore.repository.UserRepository;
import com.springboot.estore.Estore.security.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EStoreApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtHelper jwtHelper;

	@Test
	void contextLoads() {
	}

	@Test
	void testTokens()
	{
		System.out.println("Testing tokens...");

		User user = userRepository.findByEmail("pranav@gmail.com").get();

		String token = jwtHelper.generateToken(user);
		System.out.println("TOKEN --> "+token);

		System.out.println("Getting username from token...");

		String username = jwtHelper.getUsernameFromToken(token);
		System.out.println("USERNAME --> "+username);

		System.out.println("Whether token is expired...");
		Boolean tokenExpired = jwtHelper.isTokenExpired(token);
		System.out.println("EXPIRED --> "+tokenExpired);

	}

}
