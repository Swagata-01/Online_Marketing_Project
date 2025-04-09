package com.cts.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Pattern;

import com.cts.entity.User;
import com.cts.exception.AgeValidationException;
import com.cts.exception.PhotoSizeValidationException;
import com.cts.exception.InvalidInputException;
import com.cts.exception.InvalidEmailFormatException;
import com.cts.exception.DuplicateEmailException;
import com.cts.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {

    @Autowired
    private UserRepository userRepository; // For database operations

    public void validate(User user) {
        validateMandatoryFields(user); // Validate mandatory fields
        validateAge(user.getDateOfBirth()); // Validate age
        validatePhotoSize(user.getPhoto()); // Validate photo size
    }

    private void validateMandatoryFields(User user) {
        if (!isValidFirstName(user.getFirstName()) ||
            !isValidLastName(user.getLastName()) ||
            !isValidNickName(user.getNickName()) ||
            !isValidEmail(user.getEmail()) ||
            !isValidContactNumber(user.getContactNumber()) ||
            !isValidPassword(user.getPassword()) ||
            isBlank(user.getAddress()) ||
            isBlank(user.getContactNumber()) ||
            user.getDateOfBirth() == null) {
            throw new InvalidInputException("Invalid input: Missing or invalid mandatory fields.");
        }
    }
    private boolean isBlank(String field) {
        return field == null || field.isBlank(); // Null or blank check
    }



    //checking the validation of the first name
    private boolean isValidFirstName(String firstName) {
        if (firstName == null || firstName.length() < 3 || firstName.length() > 15) {
            throw new InvalidInputException("Invalid Format of First Name: Length must be between 3 and 15 characters.");
        }
        if (!firstName.matches("[a-zA-Z._0-9]+")) { // validate for alphabetic, dot, and underscore
            throw new InvalidInputException("Invalid Format of First Name: Only alphabetic characters, dots, and underscores are allowed.");
        }
        return true; // if all validations pass
    }



    //checking the validation of the last name
    private boolean isValidLastName(String lastName) {
        if (lastName == null || lastName.length() < 3 || lastName.length() > 15) {
            throw new InvalidInputException("Invalid Format of Last Name: Length must be between 3 and 15 characters.");
        }
        if (!lastName.matches("[a-zA-Z._0-9]+")) { // validate for alphabetic, dot, and underscore
            throw new InvalidInputException("Invalid Format of Last Name: Only alphabetic characters, dots, and underscores are allowed.");
        }
        return true; // if all validations pass
    }



    //validating nick name format
    private boolean isValidNickName(String nickName) {
        if(nickName == null || nickName.length() < 3 || !nickName.matches("[a-zA-Z]+")){
            throw new InvalidInputException("Invalid Format of Nick Name");
        }
        return true;
    }



    //checking that if the email is valid/in format
    private boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        // simpler regex to validate email structure
        String emailRegex = "^[a-zA-Z0-9]+([._-]?[a-zA-Z0-9]+)*@[a-zA-Z0-9]+\\.(com|net|org)$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (!pattern.matcher(email).matches()) {
            throw new InvalidEmailFormatException("Invalid email format.");
        }

        if (isDuplicateEmail(email)) {
            throw new DuplicateEmailException("Duplicate email: Email already exists in the database.");
        }

        return true;
    }

    private boolean isDuplicateEmail(String email) {
        return userRepository.existsByEmail(email); 
    }


    //contact number validation
    private boolean isValidContactNumber(String contactNumber){
        if(contactNumber.length()!=10 || !contactNumber.matches("[1-9][0-9]{9}")){
            throw new InvalidInputException("Wrong format of contact number");
        }
        return true;
    }


    //password validation
    private boolean isValidPassword(String password) {
        String valid_password = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]{6,}$";
        if (password == null || password.contains(" ") || password.contains("/")) {
            throw new InvalidInputException("Password must not contain spaces or slashes (/).");
        }

        if (!password.matches(valid_password)) {
            throw new InvalidInputException("Password must contain at least one numeric digit, one lowercase letter, one uppercase letter, one special character, and be at least 6 characters long.");
        }
    
        return true;
    }
    


    //validate the age of the user(must be greater or equal to 18 years old)
    private void validateAge(Date dateOfBirth) {
        LocalDate birthDate = new java.sql.Date(dateOfBirth.getTime()).toLocalDate();
        int age = LocalDate.now().getYear() - birthDate.getYear();
        if (age < 18) {
            throw new AgeValidationException("User must be at least 18 years old.");
        }
    }



    //validate the photo size that the size of the photo should be in between 10KB to 20 KB
    private void validatePhotoSize(byte[] photo) {
        if (photo != null) {
            int photoSize = photo.length / 1024; 
            if (photoSize < 10 || photoSize > 50) {
                throw new PhotoSizeValidationException("Photo size must be between 10KB and 20KB.");
            }
        }
    }
}
