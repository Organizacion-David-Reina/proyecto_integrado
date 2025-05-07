package com.proyectointegrado.reina_cabrera_david.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyectointegrado.reina_cabrera_david.bean.User;
import com.proyectointegrado.reina_cabrera_david.bean.UserRequest;
import com.proyectointegrado.reina_cabrera_david.constants.ApiConstants;
import com.proyectointegrado.reina_cabrera_david.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(ApiConstants.ENDPOINT)
public class Controller {

	private UserService userService;

	protected Controller(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("login")
	public User login(@RequestParam String corporateMail, @RequestParam String password) {
		return userService.login(corporateMail, password);
	}
	
	@PostMapping("sign-up")
	public ResponseEntity<String> signUp(@RequestBody UserRequest request) {
		userService.signUp(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("users-list")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}
	
	@PutMapping("update-user")
	public ResponseEntity<String> updateUser(@RequestBody User user) {
		userService.updateUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
