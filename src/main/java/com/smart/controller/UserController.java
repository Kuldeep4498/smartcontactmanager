package com.smart.controller;

import java.awt.print.Printable;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute
	public void addCommanData(Model model, Principal principal) {

		String userName = principal.getName();

		User user = userRepository.getUserByUserName(userName);

		System.out.println("USER--" + user);

		model.addAttribute("user", user);

	}

	@GetMapping("index")
	public String dashboard(Model model, Principal principal) {

		model.addAttribute("title", "User Dashboard");

		return "normal/user_dashboard";

	}
	// open add form handler

	@GetMapping("add-contact")
	public String openAddContactForm(Model model) {

		model.addAttribute("title", "Add contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form";

	}

	// processing add contact form
	@PostMapping(value = "process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {

		try {

			String name = principal.getName();

			User user = this.userRepository.getUserByUserName(name);

			// processing and uploading image file

			if (file.isEmpty()) {

				// if file is empty then try our message
				System.out.println("File is Empty!!!");
			} else {

				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("Image is uploaded!!!!");
			}

			contact.setUser(user);

			user.getContacts().add(contact);

			this.userRepository.save(user);

			System.out.println("DATA" + contact);

			System.out.println("Added to database");

			// message success.....

			session.setAttribute("message", new Message("Your contact is added !! Add new more", "success"));

		} catch (Exception e) {
			// TODO: handle exception

			System.out.println("Error" + e.getMessage());
			e.printStackTrace();

			// message error.....

			session.setAttribute("message", new Message("Something went wrong !! Contact not added", "danger"));
		}
		return "normal/add_contact_form";
	}

	// Show contacts handler
	@GetMapping(value = "show-contacts")
	public String showContacts(Model model,Principal principal) {

		model.addAttribute("title", "Show-Contacts");

		// contact list

		
		String username = principal.getName();
		
		User user = this.userRepository.getUserByUserName(username);
		
		 List<Contact> contacts = this.contactRepository.findContactByUser(user.getId());
		 
		 model.addAttribute("contacts", contacts);
		
		
		
		/*
		 * String username = principal.getName();
		 * 
		 * User user = this.userRepository.getUserByUserName(username);
		 * 
		 * List<Contact> contact = user.getContacts();
		 */
		
		return "normal/show_contacts";
	}

}
