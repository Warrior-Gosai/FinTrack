package com.fintrack.service;

import com.fintrack.model.Category;
import com.fintrack.model.User;
import com.fintrack.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Save category
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    // Delete category by ID
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // Get all categories for a specific user
    public List<Category> getAllCategoriesByUser(Long userId) {
        return categoryRepository.findByUserId(userId);
    }

    // Get categories by user + type (income / expense)
    public List<Category> getUserCategoriesByDescription(Long userId, String description) {
        return categoryRepository.findByUserIdAndDescription(userId, description);
    }

    public List<Category> getCategoriesByUserAndDescription(User user, String description) {
        return categoryRepository.findByUserAndDescription(user, description);
    }
}
