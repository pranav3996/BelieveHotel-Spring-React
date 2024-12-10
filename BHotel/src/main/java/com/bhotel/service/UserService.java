package com.bhotel.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bhotel.exception.UserAlreadyExistsException;
import com.bhotel.model.Role;
import com.bhotel.model.User;
import com.bhotel.repository.RoleRepository;
import com.bhotel.repository.UserRepository;
import com.bhotel.service.Interfaces.IUserService;

import jakarta.transaction.Transactional;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public User registerUser(User user) {
		// TODO Auto-generated method stub
		if (userRepository.existByEmail(user.getEmail())) {
			throw new UserAlreadyExistsException(user.getEmail() + "already exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Role userRole = roleRepository.findByName("ROLE_USER").get();
		user.setRoles(Collections.singletonList(userRole));

		return userRepository.save(user);
	}

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Transactional
	@Override
	public void deleteUser(String email) {
		// TODO Auto-generated method stub
		User theUser = getUser(email);
		if (theUser != null) {
			userRepository.deleteByEmail(email);
		}
	}

	@Override
	public User getUser(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
	}

}
