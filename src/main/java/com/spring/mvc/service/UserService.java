package com.spring.mvc.service;

import java.util.List;

import com.spring.mvc.model.User;



public interface UserService {
	
	User findById(long id) throws Exception;
	
	User findByName(String name) throws Exception;
	
	void saveUser(User user) throws Exception;
	
	void updateUser(User user) throws Exception;
	
	void deleteUserById(long id) throws Exception;

	List<User> findAllUsers() throws Exception;
	
	void deleteAllUsers() throws Exception;
	
	public boolean isUserExist(User user) throws Exception;
	
}
