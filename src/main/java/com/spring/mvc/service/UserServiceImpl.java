package com.spring.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.mvc.dao.DataDao;
import com.spring.mvc.model.User;

@Service("userService")
public class UserServiceImpl implements UserService{
	
	@Autowired
	DataDao dataDao;
	

	public List<User> findAllUsers() throws Exception {
		return dataDao.getEntityList();
	}
	
	public User findById(long id) throws Exception {
		return dataDao.getEntityById(id);
	}
	
	public User findByName(String name) {		
		return null;
	}
	
	public void saveUser(User user) throws Exception {
		dataDao.addEntity(user);
	}

	public void updateUser(User user) throws Exception {
		dataDao.updateEntity(user);		
	}

	public void deleteUserById(long id) throws Exception {
		dataDao.deleteEntity(id);
	}

	public boolean isUserExist(User user) {
		return findByName(user.getUsername())!=null;
	}	
	
	public void deleteAllUsers(){
		System.out.println("UserServiceImpl.deleteAllUsers() No implementado");
	}


}
