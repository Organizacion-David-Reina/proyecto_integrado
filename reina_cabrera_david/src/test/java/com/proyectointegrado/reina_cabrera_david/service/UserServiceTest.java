package com.proyectointegrado.reina_cabrera_david.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.proyectointegrado.reina_cabrera_david.bean.Credentials;
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CredentialsRepository credentialsRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void loginTest_Success() throws Exception {
        CredentialsEntity credentialsEntity = CredentialsEntity.builder()
                .userId(1)
                .password(EncryptionRSA.encrypt("password123", KeyStoreHelper.getPublicKey()))
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(1)
                .name("John")
                .lastname("Doe")
                .phoneNumber("123456789")
                .address("123 Main St")
                .dayOfBirth(LocalDate.now())
                .rol(RoleEntity.builder().id(1).rol("Admin").build())
                .build();

        when(credentialsRepository.findByCorporateMail(anyString()))
                .thenReturn(Optional.of(credentialsEntity));
        when(userRepository.findById(1))
                .thenReturn(Optional.of(userEntity));

        String decryptedPassword = "password123";

        User user = userService.login("john@corp.com", decryptedPassword);

        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getLastname());
        verify(credentialsRepository).findByCorporateMail(anyString());
        verify(userRepository).findById(1);
    }

    @Test
    public void loginTest_InvalidPassword() throws Exception {
        CredentialsEntity credentialsEntity = CredentialsEntity.builder()
                .userId(1)
                .password(EncryptionRSA.encrypt("password123", KeyStoreHelper.getPublicKey()))
                .build();

        when(credentialsRepository.findByCorporateMail(anyString()))
                .thenReturn(Optional.of(credentialsEntity));

        User result = userService.login("john@corp.com", "wrongPassword");

        assertNull(result);
        verify(credentialsRepository).findByCorporateMail(anyString());
    }

    @Test
    public void signUpTest_Success() {
        UserRequest request = new UserRequest();
        User user = new User();
        user.setRole(Role.builder().id(1).rol("Admin").build());
        request.setUser(user);

        Credentials credentials = new Credentials();
        credentials.setCorporateMail("user@example.com");
        credentials.setPassword("password123");
        request.setCredentials(credentials);

        UserEntity savedUserEntity = UserEntity.builder().id(1).build();
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(savedUserEntity);

        assertDoesNotThrow(() -> userService.signUp(request));
        verify(userRepository).save(any(UserEntity.class));
        verify(credentialsRepository).save(any(CredentialsEntity.class));
    }


    @Test
    public void getAllUsersTest() {
        UserEntity userEntity1 = UserEntity.builder()
                .id(1)
                .name("John")
                .lastname("Doe")
                .phoneNumber("123456789")
                .address("123 Main St")
                .dayOfBirth(LocalDate.now())
                .rol(RoleEntity.builder().id(1).rol("Admin").build())
                .build();

        UserEntity userEntity2 = UserEntity.builder()
                .id(2)
                .name("Jane")
                .lastname("Smith")
                .phoneNumber("987654321")
                .address("456 Second St")
                .dayOfBirth(LocalDate.now())
                .rol(RoleEntity.builder().id(2).rol("User").build())
                .build();

        when(userRepository.getAllUsersExceptDirectors())
                .thenReturn(Arrays.asList(userEntity1, userEntity2));
        when(credentialsRepository.findMailById(anyInt()))
                .thenReturn("mail@corp.com");

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository).getAllUsersExceptDirectors();
        verify(credentialsRepository, times(2)).findMailById(anyInt());
    }

    @Test
    public void updateUserTest_Success() {
        User user = User.builder()
                .id(1)
                .name("UpdatedName")
                .lastname("UpdatedLastName")
                .address("Updated Address")
                .phoneNumber("987654321")
                .role(Role.builder().id(1).rol("Admin").build())
                .corporateMail("updated@corp.com")
                .build();

        UserEntity existingUser = UserEntity.builder()
                .id(1)
                .dayOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(existingUser));

        assertDoesNotThrow(() -> userService.updateUser(user));

        verify(userRepository).save(any(UserEntity.class));
        verify(credentialsRepository).updateCorporateMail(eq(1), eq("updated@corp.com"));
    }

    @Test
    public void deleteUserTest_Success() {
        UserEntity userEntity = UserEntity.builder().id(1).build();
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));

        assertDoesNotThrow(() -> userService.deleteUser(1));

        verify(credentialsRepository).deleteById(1);
        verify(userRepository).delete(userEntity);
    }

    @Test
    public void deleteUserTest_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        InternalServerException exception = assertThrows(InternalServerException.class,
                () -> userService.deleteUser(1));

        assertEquals(ErrorConstants.USER_NOT_EXISTS, exception.getMessage());
        verify(userRepository).findById(1);
    }
}
