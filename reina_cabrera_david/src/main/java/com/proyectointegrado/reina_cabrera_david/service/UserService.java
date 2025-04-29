package com.proyectointegrado.reina_cabrera_david.service;

import org.springframework.stereotype.Service;

import com.proyectointegrado.reina_cabrera_david.bean.User;
import com.proyectointegrado.reina_cabrera_david.entity.UserEntity;
import com.proyectointegrado.reina_cabrera_david.repository.UserRepository;


@Service
public class UserService {

	private UserRepository userRepository;

	protected UserService(UserRepository repository) {
		this.userRepository = repository;
	}

	public User login(String corporateMail, String password) {
		User user = new User();
		UserEntity userEntity = userRepository.validateLogin(corporateMail, password);
		if (userEntity != null) {
			user = User.builder().id(userEntity.getId()).name(userEntity.getName()).lastname(userEntity.getLastname())
					.corporateMail(userEntity.getCorporateMail()).role(userEntity.getRol().getRol()).build();
		}
		System.out.println(user);
		return user;
	}
}
