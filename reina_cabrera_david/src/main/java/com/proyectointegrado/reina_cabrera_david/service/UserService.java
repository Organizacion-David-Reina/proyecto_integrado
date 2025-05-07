package com.proyectointegrado.reina_cabrera_david.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.proyectointegrado.reina_cabrera_david.bean.Role;
import com.proyectointegrado.reina_cabrera_david.bean.User;
import com.proyectointegrado.reina_cabrera_david.bean.UserRequest;
import com.proyectointegrado.reina_cabrera_david.constants.ErrorConstants;
import com.proyectointegrado.reina_cabrera_david.entity.CredentialsEntity;
import com.proyectointegrado.reina_cabrera_david.entity.RoleEntity;
import com.proyectointegrado.reina_cabrera_david.entity.UserEntity;
import com.proyectointegrado.reina_cabrera_david.exceptions.InternalServerException;
import com.proyectointegrado.reina_cabrera_david.repository.CredentialsRepository;
import com.proyectointegrado.reina_cabrera_david.repository.RoleRepository;
import com.proyectointegrado.reina_cabrera_david.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UserService {

	private UserRepository userRepository;
	
	private CredentialsRepository credentialsRepository;
	
	private RoleRepository roleRepository;

	protected UserService(UserRepository repository, CredentialsRepository credentialsRepository, RoleRepository roleRepository) {
		this.userRepository = repository;
		this.credentialsRepository = credentialsRepository; 
		this.roleRepository = roleRepository;
	}

	public User login(String corporateMail, String password) {
		log.info("login - corporateMail: {} - password: // ", corporateMail);

		Integer userId = credentialsRepository.authUser(corporateMail, password);

		if (userId != null) {
			Optional<UserEntity> userEntityOpt = userRepository.findById(userId);

			return userEntityOpt
					.map(u -> User.builder().id(u.getId()).name(u.getName()).lastname(u.getLastname())
							.corporateMail(corporateMail)
							.role(Role.builder().id(u.getRol().getId()).rol(u.getRol().getRol()).build()).build())
					.orElse(null);
		}

		log.info("login - end");
		return null;
	}
	
	public void signUp(UserRequest request) {
		log.info("singUp - request: {} ", request.toString());
		try {
			boolean nifExists = credentialsRepository.existsByNif(request.getCredentials().getNif());
	        boolean mailExists = credentialsRepository.existsByCorporateMail(request.getUser().getCorporateMail());

	        if (nifExists || mailExists) {
	            throw new DataIntegrityViolationException(ErrorConstants.NIF_MAIL_ALREADY_REGISTERED);
	        }
	        
			RoleEntity roleEntity = RoleEntity.builder().id(request.getUser().getRole().getId())
					.rol(request.getUser().getRole().getRol()).build();
			UserEntity userEntity = mapToUserEntity(request, roleEntity);
			Integer savedUserId = userRepository.save(userEntity).getId();
			CredentialsEntity credentialsEntity = mapToCredentialsEntity(request, savedUserId);
			credentialsRepository.save(credentialsEntity);
			
		} catch (DataIntegrityViolationException e) {
			log.error("singUp - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.NIF_MAIL_ALREADY_REGISTERED, e);
		} catch (Exception e) {
			log.error("singUp - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.INTERNAL_ERROR, e);
		}
	}
	
	public List<User> getAllUsers() {
		List<User> users = userRepository.getAllUsersExceptDirectors().stream()
				.map(u -> User.builder().id(u.getId()).name(u.getName()).lastname(u.getLastname())
						.corporateMail(credentialsRepository.findMailById(u.getId()))
						.role(Role.builder().id(u.getRol().getId()).rol(u.getRol().getRol()).build()).build())
				.collect(Collectors.toList());
		return users;
	}
	
	@Modifying(clearAutomatically = true)
	@Transactional
	public void updateUser(User user) {
		log.info("updateUser - user: {} ", user.toString());
		try {
			RoleEntity roleEntity = RoleEntity.builder().id(user.getRole().getId())
					.rol(user.getRole().getRol()).build();
			UserEntity userEntity = UserEntity.builder().id(user.getId()).name(user.getName())
					.lastname(user.getLastname()).rol(roleEntity).build();
			userRepository.save(userEntity);
			credentialsRepository.updateCorporateMail(user.getId(), user.getCorporateMail());
		} catch (DataIntegrityViolationException e) {
			log.error("singUp - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.MAIL_ALREADY_REGISTERED, e);
		} catch (Exception e) {
			log.error("singUp - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.INTERNAL_ERROR, e);
		}
	}

	private UserEntity mapToUserEntity(UserRequest request, RoleEntity roleEntity) {
		return UserEntity.builder().name(request.getUser().getName())
				.lastname(request.getUser().getLastname()).rol(roleEntity).build();
	}

	private CredentialsEntity mapToCredentialsEntity(UserRequest request, Integer savedUserId) {
		return CredentialsEntity.builder().userId(savedUserId)
				.corporateMail(request.getCredentials().getCorporateMail()).nif(request.getCredentials().getNif())
				.password(request.getCredentials().getPassword()).build();
	}
}
