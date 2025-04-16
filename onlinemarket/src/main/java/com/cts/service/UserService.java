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
import com.cts.dto.UserDTO;
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
    
    /*
    * Retrieve user details by ID.
    */
   public ResponseDTO getUserDetailsById(int userId) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
       return userMapper.toDTO(user);
   }

   /**
    * Retrieve user ID by email.
    */
   public Integer getUserIdByEmail(String email) {
       User user = userRepository.findByEmail(email);
       if (user == null) {
           throw new UserNotFoundException("User not found with email: " + email);
       }
       return user.getUserID();
   }

   /**
    * Retrieve user image by ID.
    */
   public byte[] getUserImage(int userId) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
       return user.getPhoto();
   }

   /*
     Update existing user details.
    
//   public ResponseDTO updateUserDetails(int userId, RequestDTO requestDTO, MultipartFile imageFile) throws IOException {
//       User user = userRepository.findById(userId)
//               .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
//
//       // Update fields using DTO and image file
//       updateUserFields(user, requestDTO, imageFile);
//
//       User updatedUser = userRepository.save(user);
//       return userMapper.toDTO(updatedUser);
//   }
   */
   public User updateUserDetails(
           int userId,
           String firstName,
           String lastName,
           String email,
           String password,
           byte[] photoBytes,
           String nickName,
           String addressLine1,
           String addressLine2,
           Integer postalCode,
           String contactNumber,
           Date dateOfBirth,
           Boolean isActive
   ) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

       // Update fieldsif (firstName != null && !firstName.isEmpty()) user.setFirstName(firstName);
       if (lastName != null && !lastName.isEmpty()) user.setLastName(lastName);
       if (email != null && !email.isEmpty()) user.setEmail(email);
       if (password != null && !password.isEmpty()) user.setPassword(util.hashPassword(password));
       if (photoBytes != null) user.setPhoto(photoBytes);
       if (nickName != null && !nickName.isEmpty()) user.setNickName(nickName);
       if (addressLine1 != null) user.setAddressLine1(addressLine1);
       if (addressLine2 != null) user.setAddressLine2(addressLine2);
       if (postalCode != null && postalCode > 0) user.setPostalCode(postalCode);
       if (contactNumber != null && !contactNumber.isEmpty()) user.setContactNumber(contactNumber);
       if (dateOfBirth != null) user.setDateOfBirth(dateOfBirth);
       if (isActive != null) user.setActive(isActive);

       return userRepository.save(user);
   }

   /**
    * Helper method to update user fields.
    */
   private void updateUserFields(User user, RequestDTO requestDTO, MultipartFile imageFile) throws IOException {
       if (requestDTO.getFirstName() != null && !requestDTO.getFirstName().isEmpty()) {
           user.setFirstName(requestDTO.getFirstName());
       }
       if (requestDTO.getLastName() != null && !requestDTO.getLastName().isEmpty()) {
           user.setLastName(requestDTO.getLastName());
       }
       if (requestDTO.getEmail() != null && !requestDTO.getEmail().isEmpty()) {
           user.setEmail(requestDTO.getEmail());
       }
       if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
           user.setPassword(util.hashPassword(requestDTO.getPassword()));
       }
       if (imageFile != null && !imageFile.isEmpty()) {
           user.setPhoto(imageFile.getBytes());
       }
       if (requestDTO.getNickName() != null && !requestDTO.getNickName().isEmpty()) {
           user.setNickName(requestDTO.getNickName());
       }
       if (requestDTO.getContactNumber() != null && !requestDTO.getContactNumber().isEmpty()) {
           user.setContactNumber(requestDTO.getContactNumber());
       }
       if (requestDTO.getDateOfBirth() != null) {
           user.setDateOfBirth(requestDTO.getDateOfBirth());
       }
       user.setAddress(requestDTO.getAddress());
   }

   /**
    * Create a new user.
    */
   public ResponseDTO createUser(RequestDTO requestDTO, MultipartFile imageFile) throws IOException {
       User user = userMapper.toEntity(requestDTO);
       if (imageFile != null && !imageFile.isEmpty()) {
           user.setPhoto(imageFile.getBytes());
       }

       userValidationService.validate(user);
       user.setPassword(util.hashPassword(user.getPassword()));
       User savedUser = userRepository.save(user);

       return userMapper.toDTO(savedUser);
   }

   /**
    * Retrieve all users.
    */
   public List<ResponseDTO> getAllUsers() {
       return userRepository.findAll().stream()
               .map(userMapper::toDTO)
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
    
    public String generateResetLink(String email) 
    {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return "User not found";
        }
        return "http://127.0.0.1:3000/reset-page?email=" + email;
    }

}
 