package com.fintrack.service;

import com.fintrack.model.Expense;
import com.fintrack.model.User;
import com.fintrack.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getExpensesByUser(User user) {
        return expenseRepository.findByUser(user);
    }

    public void saveExpense(Expense expense) {
        expenseRepository.save(expense);
    }

    @Transactional
    public void deleteExpenseByIdAndUser(Long id, User user) {
        expenseRepository.deleteByIdAndUser(id, user);
    }

    public double getTotalExpense() {
        return expenseRepository.findAll()
                .stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public List<Expense> getRecentExpenses(int limit) {
        return expenseRepository.findTop5ByOrderByDateDesc();
    }

    // Filtering logic
    public List<Expense> filterExpenses(LocalDate startDate, LocalDate endDate,
            String search) {
        if (startDate != null && endDate != null && search != null &&
                !search.isEmpty()) {
            return expenseRepository.findByDateBetweenAndCategory_NameContainingIgnoreCase(startDate,
                    endDate, search);
        } else if (startDate != null && endDate != null) {
            return expenseRepository.findByDateBetween(startDate, endDate);
        } else if (search != null && !search.isEmpty()) {
            return expenseRepository.findByCategory_NameContainingIgnoreCase(search);
        } else {
            return expenseRepository.findAll();
        }
    }

    // public List<Expense> filterExpenses(User user, LocalDate fromDate, LocalDate
    // toDate, Long categoryId) {
    // if (fromDate == null)
    // fromDate = LocalDate.of(2000, 1, 1);
    // if (toDate == null)
    // toDate = LocalDate.now();

    // if (categoryId != null) {
    // return expenseRepository.findByUserAndCategoryIdAndDateBetween(user,
    // categoryId, fromDate, toDate);
    // } else {
    // return expenseRepository.findByUserAndDateBetween(user, fromDate, toDate);
    // }
    // }

}
