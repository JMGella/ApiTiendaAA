package org.example.apitiendaaa.controller;

import org.example.apitiendaaa.domain.DTO.ProductOutDTO;
import org.example.apitiendaaa.domain.Product;
import org.example.apitiendaaa.exception.CategoryNotFoundException;
import org.example.apitiendaaa.exception.ErrorResponse;
import org.example.apitiendaaa.exception.ProductNotFoundException;
import org.example.apitiendaaa.service.ProductService;
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
public class ProductController {
    @Autowired
    private ProductService productService;

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping("/categories/{categoryId}/products")
    public ResponseEntity<Product> addProduct(@PathVariable long categoryId, @RequestBody Product product) throws CategoryNotFoundException {
        logger.info("BEGIN addProduct");
        Product newProduct = productService.add(categoryId, product);
        logger.info("END addProduct");
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductOutDTO>> getAll(@RequestParam (value = "name", defaultValue = "") String name,
                                                      @RequestParam(value = "price", defaultValue = "0") double price,
                                                      @RequestParam(value = "category", defaultValue = "0") long categoryId) throws CategoryNotFoundException {
        logger.info("BEGIN products getAll");
        List<ProductOutDTO> products = productService.getAll(name, price, categoryId);
        logger.info("END products getAll");
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable long productId) throws CategoryNotFoundException {
        logger.info("BEGIN getProduct");
        Product product = productService.get(productId);
        logger.info("END getProduct");
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/categories/{categoryId}/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable long categoryId, @PathVariable long productId, @RequestBody Product product) throws CategoryNotFoundException, ProductNotFoundException {
        logger.info("BEGIN updateProduct");
        Product updatedProduct = productService.update(categoryId, productId, product);
        logger.info("END updateProduct");
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long productId) throws ProductNotFoundException {
        logger.info("BEGIN deleteProduct");
        productService.delete(productId);
        logger.info("END deleteProduct");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException exception) {
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
