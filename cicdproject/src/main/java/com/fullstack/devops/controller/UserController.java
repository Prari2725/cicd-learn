package com.fullstack.devops.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fullstack.devops.exception.RecordNotFoundException;
import com.fullstack.devops.model.User;
import com.fullstack.devops.repository.UserRepository;

@Controller
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserRepository repository;

	@GetMapping("/users")
	public String getAllUsers(Model model) {
		List<User> list = new ArrayList<>();
		repository.findAll().forEach(list::add);
		model.addAttribute("users", list);
		return "list-users";     // Thymeleaf template
	}

	@GetMapping("/user/add")
	public String addUser(Model model) {
		model.addAttribute("user", new User());
		return "add-user";       // Thymeleaf template
	}

	@PostMapping("/user/create")
	public String createUser(User user) {
		repository.save(user);
		return "redirect:/api/users";
	}

	@GetMapping("/user/update/{id}")
	public String editUser(Model model, @PathVariable("id") Long id)
			throws RecordNotFoundException {

		Optional<User> user = repository.findById(id);

		if (!user.isPresent()) {
			throw new RecordNotFoundException("User not found");
		}

		model.addAttribute("user", user.get());
		return "update-user";
	}

	@PostMapping("/user/update")
	public String updateUser(User user) {
		repository.save(user);
		return "redirect:/api/users";
	}

	@GetMapping("/user/delete/{id}")
	public String deleteUserById(@PathVariable("id") Long id) {
		repository.deleteById(id);
		return "redirect:/api/users";
	}

	// FIXED — supports both /deleteall and /deleteall/
	@GetMapping(value = {"/user/deleteall", "/user/deleteall/"}, produces = "text/html")
	public String deleteAllUsers() {
		repository.deleteAll();
		return "redirect:/api/users";
	}
}
