package com.example.nutriplan.exeption;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Неверный id пользователя!");
    }
}
