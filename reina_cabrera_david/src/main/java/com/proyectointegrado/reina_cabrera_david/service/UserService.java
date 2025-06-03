package com.proyectointegrado.reina_cabrera_david.service;

import java.time.LocalDate;
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
import com.proyectointegrado.reina_cabrera_david.encryption.EncryptionRSA;
import com.proyectointegrado.reina_cabrera_david.encryption.KeyStoreHelper;
import com.proyectointegrado.reina_cabrera_david.entity.CredentialsEntity;
import com.proyectointegrado.reina_cabrera_david.entity.RoleEntity;
import com.proyectointegrado.reina_cabrera_david.entity.UserEntity;
import com.proyectointegrado.reina_cabrera_david.exceptions.InternalServerException;
import com.proyectointegrado.reina_cabrera_david.repository.CredentialsRepository;
import com.proyectointegrado.reina_cabrera_david.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	private UserRepository userRepository;
	private CredentialsRepository credentialsRepository;

	protected UserService(UserRepository userRepository, CredentialsRepository credentialsRepository) {
		this.userRepository = userRepository;
		this.credentialsRepository = credentialsRepository;
	}

	public User login(String corporateMail, String password) {
		log.info("login - corporateMail: {} - password: // ", corporateMail);

		Optional<CredentialsEntity> credentialsOpt = credentialsRepository.findByCorporateMail(corporateMail);
		try {
			if (credentialsOpt.isPresent()) {
				CredentialsEntity credentials = credentialsOpt.get();
				String decryptedPassword = EncryptionRSA.decrypt(credentials.getPassword(),
						KeyStoreHelper.getPrivateKey());
				if (decryptedPassword.equals(password)) {
					Optional<UserEntity> userEntityOpt = userRepository.findById(credentials.getUserId());
					return userEntityOpt.map(u -> User.builder().id(u.getId()).name(u.getName())
							.lastname(u.getLastname()).corporateMail(corporateMail).phoneNumber(u.getPhoneNumber())
							.address(u.getAddress()).dayOfBirth(u.getDayOfBirth())
							.role(Role.builder().id(u.getRol().getId()).rol(u.getRol().getRol()).build()).build())
							.orElse(null);
				}
			}
		} catch (Exception e) {
			throw new InternalServerException("Error durante la validación de credenciales.", e);
		}

		log.info("login - end");
		return null;
	}

	public void signUp(UserRequest request) {
		log.info("singUp - request: {} ", request.toString());
		try {
			String encryptedPassword = EncryptionRSA.encrypt(request.getCredentials().getPassword(),
					KeyStoreHelper.getPublicKey());
			request.getCredentials().setPassword(encryptedPassword);

			RoleEntity roleEntity = RoleEntity.builder().id(request.getUser().getRole().getId())
					.rol(request.getUser().getRole().getRol()).build();
			UserEntity userEntity = mapToUserEntity(request, roleEntity);
			Integer savedUserId = userRepository.save(userEntity).getId();
			CredentialsEntity credentialsEntity = mapToCredentialsEntity(request, savedUserId);
			credentialsRepository.save(credentialsEntity);

		} catch (DataIntegrityViolationException e) {
			log.error("singUp - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.CREDENTIALS_ALREADY_REGISTERED, e);
		} catch (Exception e) {
			log.error("singUp - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.INTERNAL_ERROR, e);
		}
	}

	public List<User> getAllUsers() {
		List<User> users = userRepository.getAllUsersExceptDirectors().stream()
				.map(u -> User.builder().id(u.getId()).name(u.getName()).lastname(u.getLastname())
						.corporateMail(credentialsRepository.findMailById(u.getId())).phoneNumber(u.getPhoneNumber())
						.address(u.getAddress()).dayOfBirth(u.getDayOfBirth())
						.role(Role.builder().id(u.getRol().getId()).rol(u.getRol().getRol()).build()).build())
				.collect(Collectors.toList());
		return users;
	}

	@Modifying(clearAutomatically = true)
	@Transactional
	public void updateUser(User user) {
		log.info("updateUser - user: {}", user);
		try {
			RoleEntity roleEntity = RoleEntity.builder().id(user.getRole().getId()).rol(user.getRole().getRol())
					.build();

			UserEntity existingUser = userRepository.findById(user.getId())
					.orElseThrow(() -> new InternalServerException(ErrorConstants.USER_NOT_FOUND));

			LocalDate dayOfBirth = existingUser.getDayOfBirth();

			UserEntity userEntity = UserEntity.builder().id(user.getId()).name(user.getName())
					.lastname(user.getLastname()).address(user.getAddress()).phoneNumber(user.getPhoneNumber())
					.dayOfBirth(dayOfBirth).rol(roleEntity).build();

			userRepository.save(userEntity);
			userRepository.flush();
			credentialsRepository.updateCorporateMail(user.getId(), user.getCorporateMail());
		} catch (DataIntegrityViolationException e) {
			log.error("updateUser - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.MAIL_OR_PHONE_ALREADY_REGISTERED, e);
		} catch (Exception e) {
			log.error("updateUser - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.INTERNAL_ERROR, e);
		}
	}

	@Transactional
	public void deleteUser(int userId) {
		log.info("deleteUser - userId: {} ", userId);
		try {
			Optional<UserEntity> optionalUser = userRepository.findById(userId);

			if (optionalUser.isPresent()) {
				UserEntity userEntity = optionalUser.get();
				credentialsRepository.deleteById(userEntity.getId());
				userRepository.delete(userEntity);
			} else {
				throw new InternalServerException(ErrorConstants.USER_NOT_EXISTS);
			}
		} catch (Exception e) {
			log.error("deleteUser - error - {}", e.getMessage());
			throw new InternalServerException(e.getMessage(), e);
		}
	}

	private UserEntity mapToUserEntity(UserRequest request, RoleEntity roleEntity) {
		return UserEntity.builder().name(request.getUser().getName()).lastname(request.getUser().getLastname())
				.address(request.getUser().getAddress()).phoneNumber(request.getUser().getPhoneNumber())
				.dayOfBirth(request.getUser().getDayOfBirth()).rol(roleEntity).build();
	}

	private CredentialsEntity mapToCredentialsEntity(UserRequest request, Integer savedUserId) {
		return CredentialsEntity.builder().userId(savedUserId)
				.corporateMail(request.getCredentials().getCorporateMail()).nif(request.getCredentials().getNif())
				.password(request.getCredentials().getPassword()).build();
	}
}
