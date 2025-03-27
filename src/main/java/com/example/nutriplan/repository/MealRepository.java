package com.example.nutriplan.repository;

import com.example.nutriplan.entity.Meal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findMealsByUserId(Long userId);

    @Query(value = """
    SELECT COALESCE(SUM(m.calories_per_meal), 0) 
    FROM meal m 
    WHERE m.user_id = :userId 
    AND m.create_meal_date BETWEEN :startOfDay AND :endOfDay
""", nativeQuery = true)
    Double calculateTotalCaloriesForDay(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
    @EntityGraph(attributePaths = {"user","dishes"})
    @Query("""
    SELECT m FROM Meal m 
    WHERE m.user.id = :userId 
    AND m.createMealDate BETWEEN :startOfDay AND :endOfDay
""")
    List<Meal> findMealsForUserOnDate(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

}
