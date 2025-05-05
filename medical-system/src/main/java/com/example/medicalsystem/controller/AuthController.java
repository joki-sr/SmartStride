package com.example.medicalsystem.controller;

import com.example.medicalsystem.dto.UserDTO;
import com.example.medicalsystem.dto.UserRegistrationDTO;
import com.example.medicalsystem.exception.ConflictException;
import com.example.medicalsystem.exception.UnauthorizedException;
import com.example.medicalsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        try {
            UserDTO user = userService.login(username, password);
            return ResponseEntity.ok(user);
        } catch (UnauthorizedException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO userDTO) {
        try {
            UserDTO user = userService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (ConflictException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}
