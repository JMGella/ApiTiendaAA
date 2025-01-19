package org.example.apitiendaaa.exception;

public class OrderNotFoundException extends Exception {

        public OrderNotFoundException() {
            super("Pedido no encontrado");
        }

        public OrderNotFoundException(String message) {
            super(message);
        }
}
