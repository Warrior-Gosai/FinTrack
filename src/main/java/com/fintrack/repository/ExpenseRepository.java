package com.fintrack.repository;

import com.fintrack.model.Expense;
import com.fintrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

        List<Expense> findByUser(User user);

        @Modifying
        void deleteByIdAndUser(Long id, User user);

        List<Expense> findTop5ByOrderByDateDesc();

        // Find by date range
        List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);

        // Find by category name (case-insensitive)
        List<Expense> findByCategory_NameContainingIgnoreCase(String name);

        // Find by date range + category name
        List<Expense> findByDateBetweenAndCategory_NameContainingIgnoreCase(
                        LocalDate startDate,
                        LocalDate endDate,
                        String name);

        List<Expense> findByUserAndDateBetween(User user, LocalDate from, LocalDate to);

        List<Expense> findByUserAndCategoryIdAndDateBetween(User user, Long categoryId, LocalDate from, LocalDate to);

}
