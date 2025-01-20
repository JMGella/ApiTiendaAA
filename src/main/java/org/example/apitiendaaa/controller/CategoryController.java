package org.example.apitiendaaa.controller;

import org.example.apitiendaaa.domain.Category;
import org.example.apitiendaaa.domain.DTO.CategoryOutDTO;
import org.example.apitiendaaa.exception.CategoryNotFoundException;
import org.example.apitiendaaa.exception.ErrorResponse;
import org.example.apitiendaaa.service.CategoryService;
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

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        Category newCategory = categoryService.add(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryOutDTO>> getAll(@RequestParam(value = "name", defaultValue = "") String name ,
                                                       @RequestParam(value = "active", defaultValue = "") Boolean active,
                                                       @RequestParam(value = "creationDate",defaultValue = "") String creationdate) {

        List<CategoryOutDTO> categories = categoryService.getAll(name, active, creationdate);

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable long categoryId) throws CategoryNotFoundException {
        Category category = categoryService.get(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable long categoryId, @RequestBody Category category) throws CategoryNotFoundException {
        Category updatedCategory = categoryService.update(categoryId, category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long categoryId) throws CategoryNotFoundException {
        categoryService.delete(categoryId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException exception) {
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
