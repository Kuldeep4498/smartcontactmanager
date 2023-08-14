package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

import javax.validation.Valid;

@Controller
public class HomeController {
	

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping(value = "home")
	public String home(Model model) {
		model.addAttribute("title", "Home - smart contact manager");
		return "home";

	}

	@GetMapping(value = "about")
	public String about(Model model) {
		model.addAttribute("title", "About - smart contact manager");

		return "about";

	}

	@GetMapping(value = "signup")
	public String signup(Model model) {

		model.addAttribute("title", "Register - smart contact manager");
		model.addAttribute("user", new User());

		return "signup";

	}

	// handling register user request
	
	@PostMapping(value = "do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult bindingResult, @RequestParam(value = "agreement",defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {

		try {

			if (!agreement) {
				System.out.println("You have not agreed to terms and conditions!");
				throw new Exception("You have not agreed to terms and conditions!");
			}
			
			if(bindingResult.hasErrors())
			{
				System.out.println("ERROR: "+bindingResult.toString());
				model.addAttribute("user",user);
				
				return "signup"; 
					
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("Default.png");
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

			System.out.println("agreement" + agreement);
			System.out.println(user);

			User result = this.userRepository.save(user);

			model.addAttribute("user", new User());
			
			session.setAttribute("message", new Message("Succeessfully Registered!", "alert-success"));
			
			return "signup";

		} catch (Exception e) {
			// TODO: handle exception
			model.addAttribute("user", user);

			session.setAttribute("message", new Message("something went wrong" + e.getMessage(),"alert-danger"));
			e.printStackTrace();
			
			return "signup";
		}

		

	}

}
