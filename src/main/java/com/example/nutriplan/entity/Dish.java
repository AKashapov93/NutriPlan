package com.example.nutriplan.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dishes")
public class Dish extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double caloriesPerServing;

    @Column(nullable = false)
    private double proteins;

    @Column(nullable = false)
    private double fats;

    @Column(nullable = false)
    private double carbohydrates;

    @ManyToMany(mappedBy = "dishes", fetch = FetchType.LAZY)
    private List<Meal> meals = new ArrayList<>();

}