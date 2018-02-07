package com.poradnia.service;

import java.util.List;

import com.poradnia.model.User;


public interface UserService {

	User save(User user) throws Exception;

	User findByLogin(String login);

	User findById(Long id);

	List<User> findAll();

	org.springframework.security.core.userdetails.User getUserFromContext();

	boolean activeUser(User user, String token);

}
