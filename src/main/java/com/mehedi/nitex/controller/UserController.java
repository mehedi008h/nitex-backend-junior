package com.mehedi.nitex.controller;

import com.mehedi.nitex.exceptions.ExceptionHandling;
import com.mehedi.nitex.exceptions.model.EmailExistException;
import com.mehedi.nitex.exceptions.model.UserNotFoundException;
import com.mehedi.nitex.exceptions.model.UsernameExistException;
import com.mehedi.nitex.model.HttpResponse;
import com.mehedi.nitex.model.User;
import com.mehedi.nitex.model.UserPrincipal;
import com.mehedi.nitex.service.UserService;
import com.mehedi.nitex.util.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.mehedi.nitex.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.*;


@RequestMapping(value = "/api/v1/user")
@RestController
public class UserController extends ExceptionHandling {
    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JWTTokenProvider jwtTokenProvider) {
        this.userService = userService;

        this.jwtTokenProvider = jwtTokenProvider;
    }

    // register user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws UserNotFoundException, EmailExistException, UsernameExistException {
        User newUser = userService.register(user.getFullName(), user.getUsername(), user.getEmail(), user.getPassword());
        return new ResponseEntity<>(newUser, OK);
    }

    // login user
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        User loginUser = userService.findUserByUsername(user.getUsername());
        System.out.println("Name" + loginUser.getFullName());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    // get current user profile
    @GetMapping("/profile")
    public ResponseEntity<User> getUser(Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        return new ResponseEntity<>(user, OK);
    }

    // custom response
    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

    // get jwt token
    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

}
