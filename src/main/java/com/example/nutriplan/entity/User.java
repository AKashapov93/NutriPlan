package com.example.nutriplan.entity;

import com.example.nutriplan.enums.Goal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor()
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Column
    private String username;

    @Column
    @Email
    private String email;

    @Column
    private int age;

    @Column
    private double weight;

    @Column
    private double height;

    @Column
    @Enumerated
    private Goal goal;

    @Column
    private double dailyCalorieIntake;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Meal> mealList = new ArrayList<>();


}
