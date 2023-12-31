package com.mehedi.nitex.controller;

import com.mehedi.nitex.exceptions.ExceptionHandling;
import com.mehedi.nitex.exceptions.model.*;
import com.mehedi.nitex.model.Book;
import com.mehedi.nitex.model.User;
import com.mehedi.nitex.model.UserPrincipal;
import com.mehedi.nitex.service.UserService;
import com.mehedi.nitex.util.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<User> registerUser(@RequestBody User user) throws EmailExistException, UsernameExistException, UserNotFoundException {
        User newUser = userService.register(user.getFullName(), user.getUsername(), user.getEmail(), user.getPassword());
        return new ResponseEntity<>(newUser, OK);
    }

    // login user
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) throws UserNotFoundException {
        User loginUser = userService.checkUserExist(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    // get current user profile
    @GetMapping("/profile")
    public ResponseEntity<User> getUser(Principal principal) throws UserNotFoundException {
        User user = userService.checkUserExist(principal.getName());
        return new ResponseEntity<>(user, OK);
    }

    // update user
    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user, Principal principal
    ) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User updatedUser = userService.updateUser(principal.getName(), user.getFullName(), user.getUsername(), user.getEmail());
        return new ResponseEntity<>(updatedUser, OK);
    }

    // change password
    @PutMapping("/change-password")
    public ResponseEntity<User> changePassword(@RequestParam(name = "currentPassword") String currentPassword, @RequestParam(name = "newPassword") String newPassword, Principal principal
    ) throws UserNotFoundException, PasswordNotMatchException {
        User updatedUser = userService.changePassword(principal.getName(), currentPassword, newPassword);
        return new ResponseEntity<>(updatedUser, OK);
    }

    // add book in user collection
    @PutMapping("/add-book")
    public ResponseEntity<User> addBook(@RequestBody Book book, Principal principal
    ) throws UserNotFoundException, NotFoundException {
        User add = userService.addCollection(book, principal.getName());
        return new ResponseEntity<>(add, OK);
    }

    // add book in user collection
    @PutMapping("/remove-book")
    public ResponseEntity<User> removeBook(@RequestBody Book book, Principal principal
    ) throws UserNotFoundException, NotFoundException {
        User add = userService.removeCollection(book, principal.getName());
        return new ResponseEntity<>(add, OK);
    }

    // get jwt token
    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

}
