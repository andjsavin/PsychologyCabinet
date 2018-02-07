package com.poradnia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.poradnia.model.Psychologist;
import com.poradnia.model.User;
import com.poradnia.repository.PsychologistRepository;



@Service
public class PsychologistServiceImpl implements PsychologistService {

	@Autowired
	private PsychologistRepository psychologistRepository;

	@Override
	public List<Psychologist> findAll() {
		return psychologistRepository.findAll();
	}
	
	@Override
	public Psychologist findById(Long id) {
		return psychologistRepository.findOne(id);
	}

}
