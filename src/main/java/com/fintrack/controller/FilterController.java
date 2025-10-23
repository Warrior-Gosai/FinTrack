package com.fintrack.controller;

import com.fintrack.model.Income;
import com.fintrack.model.User;
import com.fintrack.model.Expense;
import com.fintrack.service.IncomeService;
import com.fintrack.service.ExpenseService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/filter")
public class FilterController {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public FilterController(IncomeService incomeService, ExpenseService expenseService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    @GetMapping
    public String showFilterPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedUser");

        if (loggedInUser == null) {
            return "redirect:/signin";
        }

        List<Map<String, Object>> transactions = new ArrayList<>();

        // incomes
        List<Income> incomes = incomeService.getIncomesByUser(loggedInUser);
        for (Income inc : incomes) {
            Map<String, Object> row = new HashMap<>();
            row.put("date", inc.getDate());
            row.put("amount", inc.getAmount());
            row.put("type", "Income");
            row.put("category", inc.getCategory().getName());
            transactions.add(row);
        }

        // expenses
        List<Expense> expenses = expenseService.getExpensesByUser(loggedInUser);
        for (Expense exp : expenses) {
            Map<String, Object> row = new HashMap<>();
            row.put("date", exp.getDate());
            row.put("amount", exp.getAmount());
            row.put("type", "Expense");
            row.put("category", exp.getCategory().getName());
            transactions.add(row);
        }

        model.addAttribute("transactions", transactions);
        return "filter";
    }
}
