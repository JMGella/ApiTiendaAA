package org.example.apitiendaaa.exception;

public class OrderDetailNotFoundException extends Exception{


    public OrderDetailNotFoundException(long id) {
        super("OrderDetail con id: " + id + " no encontrado.");
    }

    public OrderDetailNotFoundException(String message) {
        super(message);
    }

    public OrderDetailNotFoundException() {
        super("OrderDetail no encontrado");
    }

}
