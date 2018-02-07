package com.poradnia.service;

import java.util.List;

import com.poradnia.model.Psychologist;
import com.poradnia.model.User;
import com.poradnia.model.Visit;


public interface VisitService {

	public Visit save(Visit visit, User user, Psychologist psych) throws Exception;

	Visit findByStartDate(String startDate);

	Visit findById(Long id);
	
	List<Visit> findAll();

}
