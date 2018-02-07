package com.poradnia.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poradnia.email.EmailSender;
import com.poradnia.email.EmailStatus;
import com.poradnia.model.User;
import com.poradnia.service.UserService;

@Controller
public class UserController {

	@Autowired
	private EmailSender emailHtmlSender;

	@Autowired
	UserService userService;


	@RequestMapping(value = "registration", method = RequestMethod.GET)
	public String registrationFormGET(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}

	@RequestMapping(value = "registration", method = RequestMethod.POST)
	public String registrationFormPOST(Model model, User user, BindingResult result, final HttpServletRequest request) {
		String hash = null;
		try {

			if (result.getErrorCount() == 0) {
				hash = String.valueOf(user.hashCode());
				user.setToken(hash);
				// Jesli chcesz rejestracje bez potwierdzenia miala to odkomentuj to a zakomentuj linijkę tą co jest napisane
				user.setActive(true);
				userService.save(user);
				if (user.getId() != null) {
					/// jesli nie chcecie wysyłać mail dla testów to trzeba zakomentować tą linikę
					//sendConfirmation(user.getId(), hash, user.getEmail());
				}
				model.addAttribute("success", "success.user.create");
			} else {
				model.addAttribute("error", "size.userForm.password");
			}
		} catch (DataIntegrityViolationException ex) {
			model.addAttribute("error", "errors.user.not.unique");
			return "registration";
		} catch (Exception e) {
			model.addAttribute("error", "errors.password.not.equals");
			return "registration";
		}
		return "registration";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		if (error != null) {
			model.addAttribute("error", "error.user.login");
		}
		if (logout != null)
			model.addAttribute("success", "success.user.logout");

		return "login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	private void sendConfirmation(Long id, String token, String email) {

		EmailStatus emailStatus = emailHtmlSender.sendPlainText(email, "Confirm your account",
				"Activation link: http://localhost:8080/active?id=" + String.valueOf(id) + "&token=" + token);
	}

	@RequestMapping(value = "/active", method = RequestMethod.GET)
	public String userActivation(Model model, @PathParam("id") Long id, @PathParam("token") String token) {
		User user = userService.findById(id);
		boolean succes = false;
		if (user != null) {
			succes = userService.activeUser(user, token);
		}
		if (succes) {
			model.addAttribute("success", "user.activation.success");
		} else {
			model.addAttribute("error", "user.activation.error");
		}
		return "login";
	}

	@RequestMapping("profile/{id}")
	public String profileForAdmin(Model model, @PathVariable Long id) {
		boolean admin = false;
		org.springframework.security.core.userdetails.User userFromContext = userService.getUserFromContext();
		for (GrantedAuthority authority : userFromContext.getAuthorities()) {
			if (authority.getAuthority().equals("admin")) {
				admin = true;
				break;
			}
		}
		if (admin) {
			User userById = userService.findById(id);
			model.addAttribute("user", userById);
			return "profile";
		}

		return "home";
	}

	@RequestMapping(value = "profile", method = RequestMethod.POST)
	public String editProfil(Model model, User user) {
		try {
			User userToSave = userService.findById(user.getId());
			userToSave.setFirstname(user.getFirstname());
			userToSave.setLastname(user.getLastname());
			userToSave.setEmail(user.getEmail());
			userToSave.setActive(user.isActive());
			userService.save(userToSave);
			model.addAttribute("success", "successs.user.update");
		} catch (Exception e) {
			model.addAttribute("error", "error.user.update");
			e.printStackTrace();
		}
		return "home";
	}

	@RequestMapping("profile")
	public String profileGET(Model model) {
		org.springframework.security.core.userdetails.User contextUser = userService.getUserFromContext();
		String userName = contextUser.getUsername();

		User currentUser = userService.findByLogin(userName);
		model.addAttribute("user", currentUser);
		
	/*	List<Test> tests = testService.findByUserLogin(userName);
		model.addAttribute("tests", tests);

		List<Visit> visits = visitService.findByUserLogin(userName);
		model.addAttribute("visits", visits);
		*/
		return "profile";
	}

	@RequestMapping("/admin")
	public String carForUrzednik(Model model) {
		boolean admin = false;
		org.springframework.security.core.userdetails.User userFromContext = userService.getUserFromContext();
		for (GrantedAuthority authority : userFromContext.getAuthorities()) {
			if (authority.getAuthority().equals("admin")) {
				admin = true;
				break;
			}
		}
		if (admin) {
			List<User> users = userService.findAll();
			model.addAttribute("users", users);
			return "usersList";
		}

		return "home";
	}
}
