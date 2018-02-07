package com.poradnia.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.poradnia.model.Role;
import com.poradnia.model.User;
import com.poradnia.repository.PsychologistRepository;
import com.poradnia.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired 
	private PsychologistRepository psychologistRepository;
	
	@Override
	public User findByLogin(String login) {
		return userRepository.findByLogin(login);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findById(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public User save(User user) throws Exception {
		if (user.getId() == null) {
			if (!user.getPassword().equals(user.getPasswordConfirm())) {
				throw new Exception("Password is diffrend");
			}
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setPasswordConfirm(bCryptPasswordEncoder.encode(user.getPasswordConfirm()));
			Set<Role> roleSet = new HashSet<>();
			roleSet.add(roleService.findByName("user"));
			user.setRoles(roleSet);
			user.setPsychlogist(psychologistRepository.findOne(1L));
		}
		return userRepository.save(user);
	}

	@Override
	public org.springframework.security.core.userdetails.User getUserFromContext() {
		return (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
	}

	@Override
	public boolean activeUser(User user, String token) {
		if (token.trim().equals(user.getToken().trim())) {
			try {
				user.setActive(true);
				save(user);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}
}
