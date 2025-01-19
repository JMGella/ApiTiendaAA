package org.example.apitiendaaa.exception;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(){
        super("El usuario no existe");
    }

    public UserNotFoundException(String message){
        super(message);
    }
}
