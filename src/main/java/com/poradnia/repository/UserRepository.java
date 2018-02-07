package com.poradnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poradnia.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByLogin(String login);

	User findByLoginAndActive(String login, boolean active);
}
