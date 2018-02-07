package com.poradnia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.poradnia.model.Psychologist;
import com.poradnia.model.User;
import com.poradnia.model.Visit;
import com.poradnia.service.PsychologistService;
import com.poradnia.service.UserService;
import com.poradnia.service.VisitService;

@Controller
public class VisitController {
	
	@Autowired
	PsychologistService psychologistService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	VisitService visitService;
	
	@RequestMapping(value="/datechoose/{id}", method=RequestMethod.GET)
	public String dateChoose(Model model) {		
		model.addAttribute("visit", new Visit());
		return "datechoose";
	}
	
	@RequestMapping(value = "/datechoose/{id}", method=RequestMethod.POST)
	public String processForm(Visit visit, Model model, @PathVariable Long id, User user) {
		Psychologist psychById = psychologistService.findById(id);
		org.springframework.security.core.userdetails.User contextUser = userService.getUserFromContext();
		String userName = contextUser.getUsername();
		User userToApply = userService.findByLogin(userName);
		try {
			visitService.save(visit, userToApply, psychById);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "datechoose";
		}
		model.addAttribute("success", "datechoose.success");
		return "datechoose";
	}
	
	@RequestMapping(value={"/visits"}, method = RequestMethod.GET)
	public String listVisits(Model model){
		org.springframework.security.core.userdetails.User contextUser = userService.getUserFromContext();
		String userName = contextUser.getUsername();
		User user = userService.findByLogin(userName);
		List<Visit> visits = visitService.findAll();
		List<Visit> myvisits = new ArrayList<>();
		for (int i = 0; i < visits.size(); i++) {
			if (visits.get(i).getUser() != null && visits.get(i).getUser().equals(user)) {
				myvisits.add(visits.get(i));
			}
		}
		model.addAttribute("visits", myvisits);
	    return "visits";
	}
}
