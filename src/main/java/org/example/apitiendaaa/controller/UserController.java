package org.example.apitiendaaa.controller;

import org.example.apitiendaaa.domain.DTO.UserOutDTO;
import org.example.apitiendaaa.domain.User;
import org.example.apitiendaaa.exception.ErrorResponse;
import org.example.apitiendaaa.exception.UserNotFoundException;
import org.example.apitiendaaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/users")
    public ResponseEntity<User> addUser(User user) {
        User newUser = userService.add(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);

    }

    @GetMapping("/users")
    public ResponseEntity<List<UserOutDTO>> getAll(@RequestParam(value = "name", defaultValue = "") String name,
                                                  @RequestParam(value = "email", defaultValue = "") String surname,
                                                    @RequestParam(value = "active", defaultValue = "false") boolean active) {
        List<UserOutDTO> users = userService.getAll(name, surname, active);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<User> getUser(long userId) throws UserNotFoundException {
        User user = userService.get(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@RequestParam long userId, @RequestBody User user) throws UserNotFoundException {
        User updatedUser = userService.update(userId, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<User> deleteUser(@RequestParam long userId) throws UserNotFoundException {
        userService.delete(userId);
        return  ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse error = ErrorResponse.generalError(500, exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse error = ErrorResponse.validationError(400, "Validation error", errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }



}
