package com.fintrack.service;

import com.fintrack.model.Income;
import com.fintrack.model.User;
import com.fintrack.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    // Save income for logged-in user
    public Income saveIncome(Income income, User user) {
        income.setUser(user); // bind income with logged-in user
        return incomeRepository.save(income);
    }

    @Transactional
    public void deleteIncomeByIdAndUser(Long id, User user) {
        incomeRepository.deleteByIdAndUser(id, user);
    }

    // Get all incomes of a user
    public List<Income> getIncomesByUser(User user) {
        return incomeRepository.findByUser(user);
    }

    public Optional<Income> getIncomeById(Long id) {
        return incomeRepository.findById(id);
    }

    // Get incomes in a date range
    public List<Income> getIncomesByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return incomeRepository.findByUserAndDateBetween(user, startDate, endDate);
    }

    // Search incomes by category name
    public List<Income> searchIncomesByCategory(User user, String categoryName) {
        return incomeRepository.findByUserAndCategoryNameContainingIgnoreCase(user, categoryName);
    }

    // Combined filter: date range + category search
    public List<Income> filterIncomes(User user, LocalDate startDate, LocalDate endDate, String categoryName) {
        return incomeRepository.findByUserAndDateBetweenAndCategoryNameContainingIgnoreCase(
                user, startDate, endDate, categoryName);
    }

    public double getTotalIncome() {
        return incomeRepository.findAll()
                .stream()
                .mapToDouble(Income::getAmount)
                .sum();
    }

    public List<Income> getRecentIncomes(int limit) {
        return incomeRepository.findTop5ByOrderByDateDesc();
    }

    // Filtering incomes

    public List<Income> filterIncomes(LocalDate startDate, LocalDate endDate,
            String search) {
        if (startDate != null && endDate != null && search != null &&
                !search.isEmpty()) {
            return incomeRepository.findByDateBetweenAndCategory_NameContainingIgnoreCase(startDate,
                    endDate, search);
        } else if (startDate != null && endDate != null) {
            return incomeRepository.findByDateBetween(startDate, endDate);
        } else if (search != null && !search.isEmpty()) {
            return incomeRepository.findByCategory_NameContainingIgnoreCase(search);
        } else {
            return incomeRepository.findAll();
        }
    }

    // public List<Income> filterIncomes(User user, LocalDate fromDate, LocalDate
    // toDate, Long categoryId) {
    // if (fromDate == null)
    // fromDate = LocalDate.of(2000, 1, 1);
    // if (toDate == null)
    // toDate = LocalDate.now();

    // if (categoryId != null) {
    // return incomeRepository.findByUserAndCategoryIdAndDateBetween(user,
    // categoryId, fromDate, toDate);
    // } else {
    // return incomeRepository.findByUserAndDateBetween(user, fromDate, toDate);
    // }
    // }
}
