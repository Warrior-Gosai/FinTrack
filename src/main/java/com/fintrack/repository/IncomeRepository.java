package com.fintrack.repository;

import com.fintrack.model.Income;
import com.fintrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

        // Fetch all incomes of a specific user
        List<Income> findByUser(User user);

        @Modifying
        void deleteByIdAndUser(Long id, User user);

        // Search by category name (case-insensitive) for a user
        List<Income> findByUserAndCategoryNameContainingIgnoreCase(User user, String categoryName);

        // Filter by user + date + category search
        List<Income> findByUserAndDateBetweenAndCategoryNameContainingIgnoreCase(
                        User user, LocalDate startDate, LocalDate endDate, String categoryName);

        List<Income> findTop5ByOrderByDateDesc();

        // Find by date range
        List<Income> findByDateBetween(LocalDate startDate, LocalDate endDate);

        // Find by category name (case-insensitive)
        List<Income> findByCategory_NameContainingIgnoreCase(String name);

        // Find by date range + category name
        List<Income> findByDateBetweenAndCategory_NameContainingIgnoreCase(
                        LocalDate startDate,
                        LocalDate endDate,
                        String name);

        List<Income> findByUserAndDateBetween(User user, LocalDate from, LocalDate to);

        List<Income> findByUserAndCategoryIdAndDateBetween(User user, Long categoryId, LocalDate from, LocalDate to);

}
