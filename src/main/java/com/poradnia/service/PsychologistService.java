package com.poradnia.service;

import java.util.List;

import com.poradnia.model.Psychologist;
import com.poradnia.model.User;


public interface PsychologistService {
	List<Psychologist> findAll();
	Psychologist findById(Long id);
}
