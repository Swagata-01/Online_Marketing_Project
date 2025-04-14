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
import com.cts.dto.RequestDTO;
import com.cts.dto.ResetPasswordDTO;
import com.cts.dto.ResponseDTO;
import com.cts.dto.SignInRequest;
import com.cts.dto.SignInResponse;
import com.cts.service.UserService;
import com.cts.service.ProductService;

@RestController
@RequestMapping("/OMP")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    /**
     * Register a new user.
     */
    @PostMapping("/register")
    public ResponseDTO createUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String nickName,
            @RequestParam String addressLine1,
            @RequestParam String addressLine2,
            @RequestParam int postalCode,
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
        requestDTO.setAddressLine1(addressLine1);
        requestDTO.setAddressLine2(addressLine2);
        requestDTO.setPostalCode(postalCode);
        requestDTO.setContactNumber(contactNumber);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = formatter.parse(dateOfBirth);
        requestDTO.setDateOfBirth(dob);

        return userService.createUser(requestDTO, imageFile);
    }

    /**
     * Get details of all users.
     */
    @GetMapping("/getAllUser")
    public List<ResponseDTO> getAllUser() {
        return userService.getAllUsers();
    }

    /**
     * Get user ID by email.
     */
    @GetMapping("/getUserIdByEmail")
    public Integer getUserIdByEmail(@RequestParam String emailId) {
        return userService.getUserIdByEmail(emailId);
    }

    /**
     * Get user image by ID.
     */
    @GetMapping("user/image/{userId}")
    public ResponseEntity<byte[]> getImage(@PathVariable int userId) {
        byte[] image = userService.getUserImage(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

    /**
     * Get user details by ID.
     */
    @GetMapping("/myDetails")
    public ResponseEntity<ResponseDTO> getUserDetailsById(@RequestParam int userId) {
        try {
            ResponseDTO user = userService.getUserDetailsById(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    /**
     * Update user details.
     */
    @PutMapping("/updateUser")
    public ResponseEntity<ResponseDTO> updateUserDetails(
            @RequestParam int userId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) MultipartFile photo,
            @RequestParam(required = false) String nickName,
            @RequestParam(required = false) String addressLine1,
            @RequestParam(required = false) String addressLine2,
            @RequestParam(required = false) int  postalCode,
            @RequestParam(required = false) String contactNumber,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfBirth,
            @RequestParam(required = false) Boolean isActive
    ) throws IOException {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setFirstName(firstName);
        requestDTO.setLastName(lastName);
        requestDTO.setEmail(email);
        requestDTO.setPassword(password);
        requestDTO.setNickName(nickName);
        requestDTO.setAddressLine1(addressLine1);
        requestDTO.setAddressLine2(addressLine2);
        requestDTO.setPostalCode(postalCode);
        requestDTO.setContactNumber(contactNumber);
        requestDTO.setDateOfBirth(dateOfBirth);

        ResponseDTO updatedUser = userService.updateUserDetails(userId, requestDTO, photo);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * User login.
     */
    @PostMapping("/login")
    public ResponseEntity<SignInResponse> loginUser(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(userService.validateUser(signInRequest));
    }

    /**
     * Verify user email.
     */
    @PostMapping("/verify-email")
    public String verifyEmail(@RequestParam String email) {
        return userService.verifyEmail(email);
    }

    /**
     * Reset user password.
     */
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordDTO dto) {
        return userService.resetPassword(dto);
    }

    /**
     * Get product subscription list for a user.
     */
    @GetMapping("/getProductSubscriptionList")
    public List<ProductDTO> getProductSubscriptionList(@RequestParam int userId) {
        return productService.getProductSubscriptionList(userId);
    }
}
