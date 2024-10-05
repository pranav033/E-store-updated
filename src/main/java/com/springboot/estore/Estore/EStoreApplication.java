package com.springboot.estore.Estore;

import com.springboot.estore.Estore.config.AppConstants;
import com.springboot.estore.Estore.entities.Role;
import com.springboot.estore.Estore.entities.User;
import com.springboot.estore.Estore.repository.RoleRepository;
import com.springboot.estore.Estore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class EStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(EStoreApplication.class, args);
	}


	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {

		Role roleAdmin = roleRepository.findByRoleName("ROLE_"+ AppConstants.ROLE_ADMIN).orElse(null);

		if (roleAdmin == null) {
			Role role1 = new Role();
			role1.setRoleId(UUID.randomUUID().toString());
			role1.setRoleName("ROLE_"+ AppConstants.ROLE_ADMIN);
			roleRepository.save(role1);
		}

		Role roleNormal = roleRepository.findByRoleName("ROLE_" + AppConstants.ROLE_NORMAL).orElse(null);

		if (roleNormal == null) {
			Role role2 = new Role();
			role2.setRoleId(UUID.randomUUID().toString());
			role2.setRoleName("ROLE_" + AppConstants.ROLE_NORMAL);
			roleRepository.save(role2);
		}

		User user = userRepository.findByEmail("pranav@gmail.com").orElse(null);
		if(user == null) {
			user = new User();
			user.setEmail("pranav@gmail.com");
			user.setPassword(passwordEncoder.encode("pranav"));
			user.setName("Pranav");
			user.setGender("Male");
			user.setRoles(List.of(roleAdmin));
			user.setUser_id(UUID.randomUUID().toString());
			userRepository.save(user);

		}


	}
}
