package org.example.apitiendaaa.controller;

import org.example.apitiendaaa.domain.DTO.OrderDetailInDTO;
import org.example.apitiendaaa.domain.DTO.OrderDetailOutDTO;
import org.example.apitiendaaa.domain.OrderDetail;
import org.example.apitiendaaa.exception.*;
import org.example.apitiendaaa.service.OrderDetailService;
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
public class OrderDetailController {
    @Autowired
    OrderDetailService orderDetailService;


    @PostMapping("/users/{userId}/orders/{orderId}/details")
    public ResponseEntity<OrderDetail> addOrderDetail(@RequestParam long userId, @RequestParam long orderId, @RequestBody OrderDetailInDTO orderDetailIn) throws OrderNotFoundException, UserNotFoundException, ProductNotFoundException {
       OrderDetail orderDetail = orderDetailService.add(userId, orderId, orderDetailIn);
        return new ResponseEntity<>(orderDetail, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/orders/{orderId}/details")
    public ResponseEntity<List<OrderDetailOutDTO>> getDetails(@RequestParam long userId, @RequestParam long orderId, @RequestParam(value = "discount", defaultValue = "0") long discount) throws OrderNotFoundException, UserNotFoundException {
        List<OrderDetailOutDTO> orderDetails = orderDetailService.getAll(userId, orderId, discount);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}/details")
    public ResponseEntity<List<OrderDetailOutDTO>> getDetailsByOrder(@RequestParam long orderId) throws OrderNotFoundException {
        List<OrderDetailOutDTO> orderDetails = orderDetailService.findByOrder(orderId);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}/orders/{orderId}/details/{detailId}")
    public ResponseEntity<OrderDetail> updateOrderDetail(@RequestParam long userId, @RequestParam long orderId, @RequestParam long detailId, @RequestBody OrderDetailInDTO orderDetailIn) throws OrderNotFoundException, UserNotFoundException, ProductNotFoundException {
        OrderDetail orderDetail = orderDetailService.update(userId, orderId, detailId, orderDetailIn);
        return new ResponseEntity<>(orderDetail, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/orders/{orderId}/details/{detailId}")
    public ResponseEntity<Void> deleteOrderDetail(@RequestParam long userId, @RequestParam long orderId, @RequestParam long detailId) throws OrderNotFoundException, UserNotFoundException, OrderDetailNotFoundException {
        orderDetailService.delete(userId, orderId, detailId);
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

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleOrderDetailNotFoundException(OrderDetailNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse error = ErrorResponse.generalError(500, exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse error = ErrorResponse.validationError(400, "Validation error", errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }




}
