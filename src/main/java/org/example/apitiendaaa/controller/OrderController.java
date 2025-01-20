package org.example.apitiendaaa.controller;

import org.example.apitiendaaa.domain.DTO.OrderOutDTO;
import org.example.apitiendaaa.domain.Order;
import org.example.apitiendaaa.exception.ErrorResponse;
import org.example.apitiendaaa.exception.OrderNotFoundException;
import org.example.apitiendaaa.exception.UserNotFoundException;
import org.example.apitiendaaa.service.OrderService;
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
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public List<OrderOutDTO> getAll(@RequestParam(value = "status", defaultValue = "") String status,
                                    @RequestParam(value = "total", defaultValue = "") String total,
                                    @RequestParam(value = "creationDate", defaultValue = "") String creationDate) {

        return orderService.getAll(status, total, creationDate);

    }

    @PostMapping("users/{userId}/orders")
    public ResponseEntity<Order> addOrder(@RequestParam long userId, @RequestBody Order order) throws UserNotFoundException {
        orderService.add(userId, order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("users/{userId}/orders")

    public ResponseEntity<List<OrderOutDTO>> getOrdersByUser(@RequestParam long userId) throws UserNotFoundException {
        List<OrderOutDTO> orderout = orderService.getOrdersByUser(userId);
        return new ResponseEntity<>(orderout, HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrder(@RequestParam long orderId) throws OrderNotFoundException {
        Order order = orderService.get(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}/orders/{orderId}")
    public ResponseEntity<Order> updateOrder(@RequestParam long userId, @RequestParam long orderId, @RequestBody Order order) throws UserNotFoundException, OrderNotFoundException {
        Order ordertoupdate = orderService.update(userId, orderId, order);
        return new ResponseEntity<>(ordertoupdate, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@RequestParam long orderId) throws OrderNotFoundException {
        orderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse error = ErrorResponse.generalError(500, exception.getMessage());
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
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
