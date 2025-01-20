package org.example.apitiendaaa.controller;

import org.example.apitiendaaa.domain.DTO.OrderOutDTO;
import org.example.apitiendaaa.domain.Order;
import org.example.apitiendaaa.exception.ErrorResponse;
import org.example.apitiendaaa.exception.OrderNotFoundException;
import org.example.apitiendaaa.exception.UserNotFoundException;
import org.example.apitiendaaa.service.OrderService;
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
public class OrderController {
    @Autowired
    private OrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @GetMapping("/orders")
    public ResponseEntity<List<OrderOutDTO>> getAll(@RequestParam(value = "status", defaultValue = "") String status,
                                    @RequestParam(value = "total", defaultValue = "") String total,
                                    @RequestParam(value = "creationDate", defaultValue = "") String creationDate) {
        logger.info("BEGIN orders getAll");
        List<OrderOutDTO> orders = orderService.getAll(status, total, creationDate);
        logger.info("END orders getAll");
        return new ResponseEntity<>(orders, HttpStatus.OK);

    }

    @PostMapping("users/{userId}/orders")
    public ResponseEntity<Order> addOrder(@RequestParam long userId, @RequestBody Order order) throws UserNotFoundException {
        logger.info("BEGIN addOrder");
        orderService.add(userId, order);
        logger.info("END addOrder");
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("users/{userId}/orders")

    public ResponseEntity<List<OrderOutDTO>> getOrdersByUser(@RequestParam long userId) throws UserNotFoundException {
        logger.info("BEGIN getOrdersByUser");
        List<OrderOutDTO> orderout = orderService.getOrdersByUser(userId);
        logger.info("END getOrdersByUser");
        return new ResponseEntity<>(orderout, HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrder(@RequestParam long orderId) throws OrderNotFoundException {
        logger.info("BEGIN getOrder");
        Order order = orderService.get(orderId);
        logger.info("END getOrder");
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}/orders/{orderId}")
    public ResponseEntity<Order> updateOrder(@RequestParam long userId, @RequestParam long orderId, @RequestBody Order order) throws UserNotFoundException, OrderNotFoundException {
       logger.info("BEGIN updateOrder");
        Order ordertoupdate = orderService.update(userId, orderId, order);
        logger.info("END updateOrder");
        return new ResponseEntity<>(ordertoupdate, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@RequestParam long orderId) throws OrderNotFoundException {
        logger.info("BEGIN deleteOrder");
        orderService.delete(orderId);
        logger.info("END deleteOrder");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
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
