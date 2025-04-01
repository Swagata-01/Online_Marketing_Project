package com.cts.controller;
 
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cts.dto.RequestDTO;
import com.cts.dto.ResponseDTO;
import com.cts.dto.SignInRequest;
import com.cts.dto.SignInResponse;
import com.cts.service.UserService;
 
@RestController

@RequestMapping("/api/auth")
 @CrossOrigin(origins="http://127.0.0.1:3000")

public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
public ResponseDTO createUser(
    @RequestParam String firstName,
    @RequestParam String lastName,
    @RequestParam String email,
    @RequestParam String password,
    @RequestParam String nickName,
    @RequestParam String address,
    @RequestParam String contactNumber,
    @RequestParam String dateOfBirth, 
    @RequestParam MultipartFile imageFile 
) throws IOException, ParseException {

    RequestDTO requestDTO = new RequestDTO();
    requestDTO.setFirstName(firstName);
    requestDTO.setLastName(lastName);
    requestDTO.setEmail(email);
    requestDTO.setPassword(password);
    requestDTO.setNickName(nickName);
    requestDTO.setAddress(address);
    requestDTO.setContactNumber(contactNumber);

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date dob = formatter.parse(dateOfBirth); 
    requestDTO.setDateOfBirth(dob);

    return userService.createUser(requestDTO, imageFile);
}
    @GetMapping("/getAllUser")
    public List<ResponseDTO> getAllUser() {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> loginUser( @RequestBody SignInRequest signInRequest) {
        System.out.println(userService.validateUser(signInRequest));
        return ResponseEntity.ok(userService.validateUser(signInRequest));
    }

}
 