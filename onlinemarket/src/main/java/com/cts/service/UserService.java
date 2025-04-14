package com.cts.service;
 
import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cts.dto.RequestDTO;
import com.cts.dto.ResetPasswordDTO;
import com.cts.dto.ResponseDTO;
import com.cts.dto.SignInRequest;
import com.cts.dto.SignInResponse;
import com.cts.entity.Products;
import com.cts.entity.User;
import com.cts.exception.InvalidCredentialsException;
import com.cts.exception.EmailNotVerifiedException;
import com.cts.exception.UserNotFoundException;
import com.cts.mapper.UserMapper;
import com.cts.repository.UserRepository;
import com.cts.util.PasswordUtil;
 
@Service

public class UserService {
 
    @Autowired

    private UserRepository userRepository;

    @Autowired
    private PasswordUtil util;

    @Autowired
    UserValidationService userValidationService;
    
    @Autowired
    private UserMapper userMapper;
    
    public ResponseDTO getUserDetailsById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        ResponseDTO userDTO = userMapper.toDTO(user);
        return userDTO;
    }

    public Integer getUserIdByEmail(String emailId) {
    	User user = userRepository.findByEmail(emailId);
    	return user.getUserID();
    }
    
    public byte[] getUserImage(int id) {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        return user.getPhoto();

    }
    
    public User updateUserDetails(
            int userId, 
            String firstName, 
            String lastName, 
            String email, 
            String password, 
            byte[] photo, 
            String nickName, 
            String address, 
            String contactNumber, 
            Date dateOfBirth, 
            Boolean isActive
        ) throws IOException, ParseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (firstName != null && !firstName.isEmpty()) {
            user.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            user.setLastName(lastName);
        }
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }
        if (password != null && !password.isEmpty()) {
            user.setPassword(PasswordUtil.hashPassword(password));
        }
        if (photo != null) {
            user.setPhoto(photo);
        }
        if (nickName != null && !nickName.isEmpty()) {
            user.setNickName(nickName);
        }
        if (address != null && !address.isEmpty()) {
            user.setAddress(address);
        }
        if (contactNumber != null && !contactNumber.isEmpty()) {
            user.setContactNumber(contactNumber);
        }
        if (dateOfBirth != null) {
            user.setDateOfBirth(dateOfBirth);
        }
        if (isActive != null) {
            user.setActive(isActive);
        }
        return userRepository.save(user);
    }

    
 
    public ResponseDTO createUser(RequestDTO requestDTO,MultipartFile imageFile) throws IOException {
     
		User user = UserMapper.toEntity(requestDTO);
        user.setPhoto(imageFile.getBytes());
        userValidationService.validate(user);
		user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

     public List<ResponseDTO> getAllUsers() {
 
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());

    }
    
    public SignInResponse validateUser(SignInRequest request) {

        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) 
        {
            throw new UserNotFoundException("User not found");
        }


        if ( !util.checkPassword(request.getPassword(), user.getPassword())) 
        {
            throw new InvalidCredentialsException("Invalid Credentials");
        }

        if (!user.isActive()) 
        {
            throw new EmailNotVerifiedException("Account is not active. Please verify your email.");
        }

        // If validation succeeds
        return new SignInResponse("Login Successfull",true,user.getEmail());
    }
    
 // Method to reset password
    public String resetPassword(ResetPasswordDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail());
        if (user == null) {
            return "User not found!";
        }
 
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return "Passwords do not match!";
        }
 
        user.setPassword(PasswordUtil.hashPassword(dto.getNewPassword()));
        userRepository.save(user);
 
        return "Password updated successfully!";
    }
 
// Verify Email and Activate User
    public String verifyEmail(String email)
    {
        User user = userRepository.findByEmail(email);
        if (user == null)
        {
            return "User not found!";
        }
 
        user.setEmailVerification(true);
        user.setActive(true);
        userRepository.save(user);
 
        return "Email verified successfully! Account is now active.";
    }

}
 