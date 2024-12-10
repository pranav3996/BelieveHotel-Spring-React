package com.bhotel.service.Interfaces;

import java.util.List;

import com.bhotel.model.User;

public interface IUserService {

	User registerUser(User user);

	List<User> getUsers();

	void deleteUser(String email);

	User getUser(String email);

}
