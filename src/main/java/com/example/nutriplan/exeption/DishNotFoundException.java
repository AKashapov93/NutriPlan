package com.example.nutriplan.exeption;

public class DishNotFoundException extends RuntimeException {
    public DishNotFoundException(String massage) {
        super(massage);
    }

}
