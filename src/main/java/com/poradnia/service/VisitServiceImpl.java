package com.poradnia.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.poradnia.model.Visit;
import com.poradnia.model.User;
import com.poradnia.model.Psychologist;
import com.poradnia.repository.PsychologistRepository;
import com.poradnia.repository.UserRepository;
import com.poradnia.repository.VisitRepository;


@Service
public class VisitServiceImpl implements VisitService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VisitRepository visitRepository;

	@Override
	public Visit findById(Long id) {
		return visitRepository.findOne(id);
	}
	
	@Override
	public List<Visit> findAll() {
		return visitRepository.findAll();
	}
	
	@Override
	public Visit findByStartDate(String startDate) {
		return visitRepository.findByStartDate(startDate);
	}

	@Override
	public Visit save(Visit visit, User user, Psychologist psych) throws Exception {
		visit.setUser(user);
		visit.setPsychologist(psych);
		visit.setStartDate(visit.getStartDate());
		List<Visit> visits = visitRepository.findAll();
		for (int i = 0; i < visits.size(); i++) {		
			if (visits.get(i).getPsychologist() != null && visits.get(i).getPsychologist().equals(psych)) {
				if (visits.get(i).getStartDate() != null && visits.get(i).getStartDate().equals(visit.getStartDate())) {
					throw new Exception("Date already taken");
				}
			}
		}
		String parts[] = visit.getStartDate().split("-");
		int endTime = Integer.parseInt(parts[3]) + 1;
		if (endTime > 20) {
			throw new Exception("Too late time");
		}
		if (endTime < 9) {
			throw new Exception("Too early time");
		}
		parts[3] = String.valueOf(endTime);
		String endDate = String.join("-", parts);
		visit.setEndDate(endDate);
		return visitRepository.save(visit);
	}
}
