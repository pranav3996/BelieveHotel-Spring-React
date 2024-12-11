package com.bhotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhotel.exception.UserAlreadyExistsException;
import com.bhotel.model.User;
import com.bhotel.service.Interfaces.IUserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
	@Autowired
	private IUserService userService;

	@PostMapping("/register-user")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		try {
			userService.registerUser(user);
			return ResponseEntity.ok("Registration Successfull!");
		} catch (UserAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}

}
