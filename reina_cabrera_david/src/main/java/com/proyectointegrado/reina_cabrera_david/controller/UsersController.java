package com.proyectointegrado.reina_cabrera_david.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyectointegrado.reina_cabrera_david.bean.User;
import com.proyectointegrado.reina_cabrera_david.constants.ApiConstants;
import com.proyectointegrado.reina_cabrera_david.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(ApiConstants.ENDPOINT)
public class UsersController {

	private UserService userService;

	protected UsersController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("login")
	public User login(@RequestParam String corporateMail, @RequestParam String password) {
		System.out.println(corporateMail + " " + password);
		return userService.login(corporateMail, password);
	}
}
