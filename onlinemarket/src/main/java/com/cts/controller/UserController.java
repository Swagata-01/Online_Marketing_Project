package com.cts.controller;
 
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cts.dto.ProductDTO;
import com.cts.dto.ProductRatingSubscriptionDTO;
import com.cts.dto.RequestDTO;
import com.cts.dto.ResetPasswordDTO;
import com.cts.dto.ResponseDTO;
import com.cts.dto.SignInRequest;
import com.cts.dto.SignInResponse;
import com.cts.entity.User;
import com.cts.exception.UserNotFoundException;
import com.cts.service.ProductService;
import com.cts.service.UserService;
 
@RestController

@RequestMapping("/OMP")
//@CrossOrigin(origins="http://127.0.0.1:3000")

public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;

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
    
    @GetMapping("/getUserIdByEmail")
    public Integer getUserIdByEmail(@RequestParam String emailId) {
    	return userService.getUserIdByEmail(emailId);
    }
    
    @GetMapping("user/image/{userId}")
	public ResponseEntity<byte[]> getImage(@PathVariable int userId){
		byte[] image = userService.getUserImage(userId);
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_PNG)
				.body(image);
	}
	
    @GetMapping("/myDetails")
    public ResponseEntity<ResponseDTO> getUserDetailsById(@RequestParam int userId) {
        try {
            ResponseDTO user = userService.getUserDetailsById(userId);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<User> updateUserDetails(
            @RequestParam int userId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) MultipartFile photo,
            @RequestParam(required = false) String nickName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String contactNumber,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfBirth,
            @RequestParam(required = false) Boolean isActive
    ) throws IOException, ParseException {
        byte[] photoBytes = null;
        if (photo != null) {
            photoBytes = photo.getBytes();
        }

        User updatedUser = userService.updateUserDetails(userId,firstName,lastName,email,password,photoBytes,nickName,address,contactNumber,dateOfBirth,isActive);
        return ResponseEntity.ok(updatedUser);
    }


    @PostMapping("/login")
    public ResponseEntity<SignInResponse> loginUser( @RequestBody SignInRequest signInRequest) {
        System.out.println(userService.validateUser(signInRequest));
        return ResponseEntity.ok(userService.validateUser(signInRequest));
    }
    
 // Email Verification API
    @PostMapping("/verify-email")
    public String verifyEmail(@RequestParam String email)
    {
        return userService.verifyEmail(email);
    }
 
    // Reset Password API
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordDTO dto)
    {
        return userService.resetPassword(dto);
    }
    
    @GetMapping("/getProductSubscriptionList")
    public List<ProductDTO> getProductSubscriptionList(@RequestParam int userId){
        return productService.getProductSubscriptionList(userId);
    }

}
 