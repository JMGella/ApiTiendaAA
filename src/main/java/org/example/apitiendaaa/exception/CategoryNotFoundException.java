package org.example.apitiendaaa.exception;

public class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException( ) {
        super("Categoría no encontrada");

    }
    public CategoryNotFoundException(String message) {


        super(message);
        }

}
