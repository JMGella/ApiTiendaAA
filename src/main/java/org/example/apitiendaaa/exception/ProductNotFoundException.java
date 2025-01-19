package org.example.apitiendaaa.exception;

public class ProductNotFoundException extends Exception{

    public ProductNotFoundException(){
        super("Producto no encontrado");
    }

    public ProductNotFoundException(String message){
        super(message);
    }
}
