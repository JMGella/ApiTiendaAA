package org.example.apitiendaaa.controller;

import org.example.apitiendaaa.domain.DTO.UserOutDTO;
import org.example.apitiendaaa.domain.User;
import org.example.apitiendaaa.exception.ErrorResponse;
import org.example.apitiendaaa.exception.UserNotFoundException;
import org.example.apitiendaaa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        logger.info("BEGIN addUser");
        User newUser = userService.add(user);
        logger.info("END addUser");
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);

    }

    @GetMapping("/users")
    public ResponseEntity<List<UserOutDTO>> getAll(@RequestParam(value = "name", defaultValue = "") String name,
                                                  @RequestParam(value = "email", defaultValue = "") String surname,
                                                    @RequestParam(value = "active", defaultValue = "false") boolean active) {
        logger.info("BEGIN users getAll");
        List<UserOutDTO> users = userService.getAll(name, surname, active);
        logger.info("END users getAll");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<User> getUser(@PathVariable long userId) throws UserNotFoundException {
        logger.info("BEGIN getUser");
        User user = userService.get(userId);
        logger.info("END getUser");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable long userId, @RequestBody User user) throws UserNotFoundException {
        logger.info("BEGIN updateUser");
        User updatedUser = userService.update(userId, user);
        logger.info("END updateUser");
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable long userId) throws UserNotFoundException {
        logger.info("BEGIN deleteUser");
        userService.delete(userId);
        logger.info("END deleteUser");
        return  ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse error = ErrorResponse.generalError(500, exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse error = ErrorResponse.validationError(400, "Validation error", errors);
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }



}
