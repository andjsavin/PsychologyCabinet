package com.poradnia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poradnia.model.Psychologist;
import com.poradnia.model.User;
import com.poradnia.model.Visit;
import com.poradnia.service.PsychologistService;
import com.poradnia.service.VisitService;

@Controller
public class PsychologistController {
	
	@Autowired
	PsychologistService psychologistService;
	
	@Autowired
	VisitService visitService;
	
	@RequestMapping(value={"/application"}, method = RequestMethod.GET)
	public String listPsychologists(Model model){    
		List<Psychologist> psychs = psychologistService.findAll();
		model.addAttribute("psychs", psychs);
	    return "application";
	}
	
	@RequestMapping(value= {"/home"})
	public String returnHome(Model model) {
		return "home";
	}
	
	
}
