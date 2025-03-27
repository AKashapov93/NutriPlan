package com.example.nutriplan.entity;


import com.example.nutriplan.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "meal")

public class Meal extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "create_meal_date")
    private LocalDateTime createMealDate;

    @Column(nullable = false)
    private double caloriesPerMeal;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "meal_dish",
            joinColumns = @JoinColumn(name = "meal_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    private List<Dish> dishes;
}
