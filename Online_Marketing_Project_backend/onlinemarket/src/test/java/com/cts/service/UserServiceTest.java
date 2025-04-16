//package com.cts.omssignin.service;
// 
//import com.cts.omssignin.dto.*;
//import com.cts.omssignin.exception.*;
//import com.cts.omssignin.model.User;
//import com.cts.omssignin.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.junit.jupiter.api.extension.ExtendWith;
// 
//import java.util.Optional;
// 
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
// 
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//    @Mock private UserRepository userRepository;
//    @InjectMocks private UserService userService;
// 
//    @Test
//    void testValidateUser_UserNotFound() {
//        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
// 
//        assertThrows(UserNotFoundException.class, () -> userService.validateUser(new SignInRequest("test@example.com", "password")));
//    }
//}
// 
// package com.cts.service;

// import com.cts.dto.SignInRequest;
// import com.cts.dto.SignInResponse;
// import com.cts.dto.RequestDTO;
// import com.cts.dto.ResponseDTO;
// import com.cts.exception.EmailNotVerifiedException;
// import com.cts.exception.InvalidCredentialsException;
// import com.cts.exception.UserNotFoundException;
// import com.cts.entity.User;
// import com.cts.repository.UserRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// //import org.springframework.security.crypto.password.PasswordEncoder;

// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// class UserServiceTest{

//     @Mock
//     private UserRepository userRepository;

//     //@Mock
//     //private PasswordEncoder passwordEncoder;

//     @InjectMocks
//     private UserService userService;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

    

//     @Test
//     void validateUser_shouldReturnSuccessResponse() {
//         SignInRequest signInRequest = new SignInRequest("john.doe@example.com", "password123");
//         User user = new User();
//         user.setEmail(signInRequest.getEmail());
//         user.setPassword("encodedPassword");
//         user.emailVerification(true);

//         when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));
//         when(passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())).thenReturn(true);

//         SignInResponse response = userService.validateUser(signInRequest);

//         assertEquals("Login successful", response.getMessage());
//         assertTrue(response.getSuccess());
//     }

//     @Test
//     void validateUser_shouldThrowUserNotFoundException() {
//         SignInRequest signInRequest = new SignInRequest("john.doe@example.com", "password123");

//         when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.empty());

//         assertThrows(UserNotFoundException.class, () -> userService.validateUser(signInRequest));
//     }

//     @Test
//     void validateUser_shouldThrowInvalidCredentialsException() {
//         SignInRequest signInRequest = new SignInRequest("john.doe@example.com", "password123");
//         User user = new User();
//         user.setEmail(signInRequest.getEmail());
//         user.setPassword("encodedPassword");
//         user.setEmailVerified(true);

//         when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));
//         when(passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())).thenReturn(false);

//         assertThrows(InvalidCredentialsException.class, () -> userService.validateUser(signInRequest));
//     }

//     @Test
//     void validateUser_shouldThrowEmailNotVerifiedException() {
//         SignInRequest signInRequest = new SignInRequest("john.doe@example.com", "password123");
//         User user = new User();
//         user.setEmail(signInRequest.getEmail());
//         user.setPassword("encodedPassword");
//         user.setEmailVerified(false); // Email is not verified

//         when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));
//         when(passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())).thenReturn(true);

//         // Ensure that the exception is thrown due to email not being verified
//         assertThrows(EmailNotVerifiedException.class, () -> userService.validateUser(signInRequest));
//     }
// }
// package com.cts.service;

// import com.cts.dto.SignInRequest;
// import com.cts.dto.SignInResponse;
// import com.cts.entity.User;
// import com.cts.exception.EmailNotVerifiedException;
// import com.cts.exception.InvalidCredentialsException;
// import com.cts.exception.UserNotFoundException;
// import com.cts.repository.UserRepository;
// import com.cts.util.PasswordUtil;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// class UserServiceTest {

//     @Mock
//     private UserRepository userRepository;

//     @Mock
//     private PasswordUtil passwordUtil;

//     @InjectMocks
//     private UserService userService;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void validateUser_shouldReturnSuccessResponse() {
//         // Arrange
//         SignInRequest signInRequest = new SignInRequest("john.doe@example.com", "password123");
//         User user = new User();
//         user.setEmail(signInRequest.getEmail());
//         user.setPassword("encodedPassword");
//         user.setActive(true);

//         // Mocking repository and utility behavior
//         when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));
//         when(passwordUtil.checkPassword(signInRequest.getPassword(), user.getPassword())).thenReturn(true);

//         // Act
//         SignInResponse response = userService.validateUser(signInRequest);

//         // Assert
//         assertEquals("Login successful", response.getMessage());
//         assertTrue(response.getsuccess());
//     }

//     @Test
//     void validateUser_shouldThrowUserNotFoundException() {
//         // Arrange
//         SignInRequest signInRequest = new SignInRequest("nonexistent@example.com", "password123");

//         when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.empty());

//         // Act & Assert
//         assertThrows(UserNotFoundException.class, () -> userService.validateUser(signInRequest));
//     }

//     @Test
//     void validateUser_shouldThrowInvalidCredentialsException() {
//         // Arrange
//         SignInRequest signInRequest = new SignInRequest("john.doe@example.com", "wrongpassword");
//         User user = new User();
//         user.setEmail(signInRequest.getEmail());
//         user.setPassword("encodedPassword");
//         user.setActive(true);

//         when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));
//         when(passwordUtil.checkPassword(signInRequest.getPassword(), user.getPassword())).thenReturn(false);

//         // Act & Assert
//         assertThrows(InvalidCredentialsException.class, () -> userService.validateUser(signInRequest));
//     }

//     @Test
//     void validateUser_shouldThrowEmailNotVerifiedException() {
//         // Arrange
//         SignInRequest signInRequest = new SignInRequest("john.doe@example.com", "password123");
//         User user = new User();
//         user.setEmail(signInRequest.getEmail());
//         user.setPassword("encodedPassword");
//         user.setActive(false); // Email not verified

//         when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));
//         when(passwordUtil.checkPassword(signInRequest.getPassword(), user.getPassword())).thenReturn(true);

//         // Act & Assert
//         assertThrows(EmailNotVerifiedException.class, () -> userService.validateUser(signInRequest));
//     }
// }

// package com.cts.service;

// import com.cts.dto.SignInRequest;
// import com.cts.dto.SignInResponse;
// import com.cts.entity.User;
// import com.cts.exception.EmailNotVerifiedException;
// import com.cts.exception.InvalidCredentialsException;
// import com.cts.exception.UserNotFoundException;
// import com.cts.repository.UserRepository;
// import com.cts.util.PasswordUtil;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// class UserServiceTest {

//     @Mock
//     private UserRepository userRepository;

//     @Mock
//     private PasswordUtil passwordUtil;

//     @InjectMocks
//     private UserService userService;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void validateUser_shouldReturnSuccessResponse() {
//         // Arrange
//         SignInRequest signInRequest = new SignInRequest("john.doe@example.com", "password123");
//         User user = new User();
//         user.setEmail(signInRequest.getEmail());
//         user.setPassword(PasswordUtil.hashPassword("password123"));
//         user.setActive(true);

//         when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));

//         // Act
//         SignInResponse response = userService.validateUser(signInRequest);

//         // Assert
//         assertEquals("Login successful", response.getMessage());
//         assertTrue(response.getSuccess());
//     }
//     @Test
//     void validateUser_shouldThrowUserNotFoundException() {
//         // Arrange
//         SignInRequest signInRequest = new SignInRequest("nonexistent@example.com", "password123");

//     User user = (User)Optional.empty().get();

//         when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(user);

//         // Act & Assert
//         assertThrows(UserNotFoundException.class, () -> userService.validateUser(signInRequest));
//     }

//     @Test
//     void validateUser_shouldThrowInvalidCredentialsException() {
//         // Arrange
//         SignInRequest signInRequest = new SignInRequest("john.doe@example.com", "wrongpassword");
//         User user = new User();
//         user.setEmail(signInRequest.getEmail());
//         user.setPassword("encodedPassword");
//         user.setActive(true);

//         when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user).get());
//         when(passwordUtil.checkPassword(signInRequest.getPassword(), user.getPassword())).thenReturn(false);

//         // Act & Assert
//         assertThrows(InvalidCredentialsException.class, () -> userService.validateUser(signInRequest));
//     }

//     @Test
//     void validateUser_shouldThrowEmailNotVerifiedException() {
//         // Arrange
//         SignInRequest signInRequest = new SignInRequest("john.doe@example.com", "password123");
//         User user = new User();
//         user.setEmail(signInRequest.getEmail());
//         user.setPassword("encodedPassword");
//         user.setActive(false); // Email not verified

//         when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user).get());
//         when(passwordUtil.checkPassword(signInRequest.getPassword(), user.getPassword())).thenReturn(true);

//         // Act & Assert
//         assertThrows(EmailNotVerifiedException.class, () -> userService.validateUser(signInRequest));
//     }
// }
package com.cts.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.cts.dto.SignInRequest;
import com.cts.entity.User;
import com.cts.exception.EmailNotVerifiedException;
import com.cts.exception.InvalidCredentialsException;
import com.cts.exception.UserNotFoundException;
import com.cts.repository.UserRepository;
import com.cts.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordUtil passwordUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateUser_UserNotFoundException() {
        SignInRequest request = new SignInRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userService.validateUser(request);
        });
    }

    @Test
    public void testValidateUser_InvalidCredentialsException() {
        SignInRequest request = new SignInRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongpassword");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hashedpassword");
        user.setActive(true);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(user);
        when(passwordUtil.checkPassword(request.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.validateUser(request);
        });
    }

    @Test
    public void testValidateUser_EmailNotVerifiedException() {
        SignInRequest request = new SignInRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hashedpassword");
        user.setActive(false);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(user);
        when(passwordUtil.checkPassword(request.getPassword(), user.getPassword())).thenReturn(true);

        assertThrows(EmailNotVerifiedException.class, () -> {
            userService.validateUser(request);
        });
    }
}
