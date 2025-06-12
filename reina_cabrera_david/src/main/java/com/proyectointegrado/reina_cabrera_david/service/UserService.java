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

/**
 * The Class UserService
 */
@Service
@Slf4j
public class UserService {

	/** The User Repository */
	private UserRepository userRepository;
	
	/** The Credentials Repository */
	private CredentialsRepository credentialsRepository;

	/**
	 * Constructor injecting required repositories.
	 * 
	 * @param userRepository        repository for UserEntity persistence
	 * @param credentialsRepository repository for CredentialsEntity persistence
	 */
	protected UserService(UserRepository userRepository, CredentialsRepository credentialsRepository) {
		this.userRepository = userRepository;
		this.credentialsRepository = credentialsRepository;
	}

	/**
	 * Authenticates a user with corporate email and password.
	 * Decrypts stored password and compares it with the provided password.
	 * 
	 * @param corporateMail user's corporate email
	 * @param password      user's plaintext password
	 * @return User object if authentication succeeds, otherwise null
	 * @throws InternalServerException if there is an error during credential validation
	 */
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
			throw new InternalServerException("Error during credential validation.", e);
		}

		log.info("login - end");
		return null;
	}

	/**
	 * Registers a new user along with their credentials.
	 * Encrypts the password before saving.
	 * 
	 * @param request UserRequest containing user and credentials info
	 * @throws InternalServerException if credentials are already registered or other internal errors occur
	 */
	public void signUp(UserRequest request) {
		log.info("signUp - request: {} ", request.toString());
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
			log.error("signUp - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.CREDENTIALS_ALREADY_REGISTERED, e);
		} catch (Exception e) {
			log.error("signUp - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.INTERNAL_ERROR, e);
		}
	}

	/**
	 * Retrieves all users excluding those with director roles.
	 * 
	 * @return List of User objects
	 */
	public List<User> getAllUsers() {
		List<User> users = userRepository.getAllUsersExceptDirectors().stream()
				.map(u -> User.builder().id(u.getId()).name(u.getName()).lastname(u.getLastname())
						.corporateMail(credentialsRepository.findMailById(u.getId())).phoneNumber(u.getPhoneNumber())
						.address(u.getAddress()).dayOfBirth(u.getDayOfBirth())
						.role(Role.builder().id(u.getRol().getId()).rol(u.getRol().getRol()).build()).build())
				.collect(Collectors.toList());
		return users;
	}

	/**
	 * Updates an existing user's information and credentials.
	 * Maintains original date of birth.
	 * 
	 * @param user User object with updated information
	 * @throws InternalServerException if user not found, or mail/phone is already registered,
	 *                                 or other internal errors
	 */
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

	/**
	 * Deletes a user and their credentials by user ID.
	 * 
	 * @param userId the ID of the user to delete
	 * @throws InternalServerException if the user does not exist or other errors occur
	 */
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

	/**
	 * Maps a UserRequest to a UserEntity.
	 * 
	 * @param request   the UserRequest containing user data
	 * @param roleEntity the RoleEntity associated with the user
	 * @return UserEntity mapped from request
	 */
	private UserEntity mapToUserEntity(UserRequest request, RoleEntity roleEntity) {
		return UserEntity.builder().name(request.getUser().getName()).lastname(request.getUser().getLastname())
				.address(request.getUser().getAddress()).phoneNumber(request.getUser().getPhoneNumber())
				.dayOfBirth(request.getUser().getDayOfBirth()).rol(roleEntity).build();
	}

	/**
	 * Maps a UserRequest to a CredentialsEntity.
	 * 
	 * @param request    the UserRequest containing credentials data
	 * @param savedUserId the ID of the saved user
	 * @return CredentialsEntity mapped from request
	 */
	private CredentialsEntity mapToCredentialsEntity(UserRequest request, Integer savedUserId) {
		return CredentialsEntity.builder().userId(savedUserId)
				.corporateMail(request.getCredentials().getCorporateMail()).nif(request.getCredentials().getNif())
				.password(request.getCredentials().getPassword()).build();
	}
}
