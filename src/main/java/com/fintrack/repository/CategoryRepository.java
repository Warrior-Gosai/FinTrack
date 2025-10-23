package com.fintrack.repository;

import com.fintrack.model.Category;
import com.fintrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Get all categories for a specific user
    List<Category> findByUserId(Long userId);

    // Get categories for a specific user filtered by type (income/expense)
    List<Category> findByUserIdAndDescription(Long userId, String description);

    List<Category> findByUserAndDescription(User user, String description);
}
