package org.example.apitiendaaa.controller;

import org.example.apitiendaaa.domain.DTO.OrderDetailInDTO;
import org.example.apitiendaaa.domain.DTO.OrderDetailOutDTO;
import org.example.apitiendaaa.domain.OrderDetail;
import org.example.apitiendaaa.exception.*;
import org.example.apitiendaaa.service.OrderDetailService;
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
public class OrderDetailController {
    @Autowired
    OrderDetailService orderDetailService;

    private final Logger logger = LoggerFactory.getLogger(OrderDetailController.class);


    @PostMapping("/users/{userId}/orders/{orderId}/details")
    public ResponseEntity<OrderDetail> addOrderDetail(@PathVariable long userId, @PathVariable long orderId, @RequestBody OrderDetailInDTO orderDetailIn) throws OrderNotFoundException, UserNotFoundException, ProductNotFoundException {
       logger.info("BEGIN addOrderDetail");
        OrderDetail orderDetail = orderDetailService.add(userId, orderId, orderDetailIn);
        logger.info("END addOrderDetail");
        return new ResponseEntity<>(orderDetail, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/orders/{orderId}/details")
    public ResponseEntity<List<OrderDetailOutDTO>> getDetails(@PathVariable long userId, @PathVariable long orderId, @RequestParam(value = "discount", defaultValue = "0") long discount) throws OrderNotFoundException, UserNotFoundException {
       logger.info("BEGIN getDetails");
        List<OrderDetailOutDTO> orderDetails = orderDetailService.getAll(userId, orderId, discount);
        logger.info("END getDetails");
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }



    @PutMapping("/users/{userId}/orders/{orderId}/details/{detailId}")
    public ResponseEntity<OrderDetailOutDTO> updateOrderDetail(@PathVariable long userId, @PathVariable long orderId, @PathVariable long detailId, @RequestBody OrderDetailInDTO orderDetailIn) throws OrderNotFoundException, UserNotFoundException, ProductNotFoundException {
        logger.info("BEGIN updateOrderDetail");
        OrderDetailOutDTO orderDetail = orderDetailService.update(userId, orderId, detailId, orderDetailIn);
        logger.info("END updateOrderDetail");
        return new ResponseEntity<>(orderDetail, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/orders/{orderId}/details/{detailId}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable long userId, @PathVariable long orderId, @PathVariable long detailId) throws OrderNotFoundException, UserNotFoundException, OrderDetailNotFoundException {
        logger.info("BEGIN deleteOrderDetail");
        orderDetailService.delete(userId, orderId, detailId);
        logger.info("END deleteOrderDetail");
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

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderDetailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderDetailNotFoundException(OrderDetailNotFoundException exception) {
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
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse error = ErrorResponse.validationError(400, "Validation error", errors);
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }




}
