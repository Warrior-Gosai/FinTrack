package com.fintrack.controller;

import com.fintrack.model.Income;
import com.fintrack.model.Expense;
import com.fintrack.model.User;
import com.fintrack.service.IncomeService;
import com.fintrack.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public DashboardController(IncomeService incomeService, ExpenseService expenseService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedUser");

        if (loggedInUser == null) {
            return "redirect:/signin"; // not logged in
        }

        // Fetch data for this user
        List<Income> incomes = incomeService.getIncomesByUser(loggedInUser);
        List<Expense> expenses = expenseService.getExpensesByUser(loggedInUser);

        // Calculate totals
        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double totalBalance = totalIncome - totalExpense;

        // Add to model
        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("totalBalance", totalBalance);

        // Show only recent 5
        model.addAttribute("recentIncomes", incomes.stream().limit(5).toList());
        model.addAttribute("recentExpenses", expenses.stream().limit(5).toList());

        return "dashboard"; // dashboard.html
    }
}
