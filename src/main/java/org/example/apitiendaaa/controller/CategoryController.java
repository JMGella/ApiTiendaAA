package org.example.apitiendaaa.controller;

import jakarta.validation.Valid;
import org.example.apitiendaaa.domain.Category;
import org.example.apitiendaaa.domain.DTO.CategoryOutDTO;
import org.example.apitiendaaa.exception.CategoryNotFoundException;
import org.example.apitiendaaa.exception.ErrorResponse;
import org.example.apitiendaaa.service.CategoryService;
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
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(@Valid @RequestBody Category category) {
        logger.info("BEGIN categories addCategory");
        Category newCategory = categoryService.add(category);
        logger.info("END categories addCategory");
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryOutDTO>> getAll(@RequestParam(value = "name", defaultValue = "") String name ,
                                                       @RequestParam(value = "active", defaultValue = "") Boolean active,
                                                       @RequestParam(value = "creationDate",defaultValue ="") String creationDate) {
        logger.info("BEGIN categories getAll");
        List<CategoryOutDTO> categories = categoryService.getAll(name, active, creationDate);
        logger.info("END categories getAll");
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable long categoryId) throws CategoryNotFoundException {
        logger.info("BEGIN categories getCategory");
        Category category = categoryService.get(categoryId);
        logger.info("END categories getCategory");
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable long categoryId, @Valid @RequestBody Category category) throws CategoryNotFoundException {
        logger.info("BEGIN categories updateCategory");
        Category updatedCategory = categoryService.update(categoryId, category);
        logger.info("END categories updateCategory");
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long categoryId) throws CategoryNotFoundException {
        logger.info("BEGIN categories deleteCategory");
        categoryService.delete(categoryId);
        logger.info("END categories deleteCategory");
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/categories/{categoryId}")
    public ResponseEntity<Category> patchCategory(@PathVariable long categoryId, @RequestBody Category category) throws CategoryNotFoundException {
        logger.info("BEGIN categories patchCategory");
        Category updatedCategory = categoryService.update(categoryId, category);
        logger.info("END categories patchCategory");
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        logger.error("Error: " + error.getMessage() + " " + error.getErrorcode());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse error = ErrorResponse.generalError(500, exception.getMessage());
        logger.error("Error: " + error.getMessage() + " " + error.getErrorcode());
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
        logger.error("Error: " + error.getMessage() + " " + error.getErrorcode());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
