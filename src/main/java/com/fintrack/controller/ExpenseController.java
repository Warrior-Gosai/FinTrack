package com.fintrack.controller;

import com.fintrack.model.Expense;
import com.fintrack.model.Category;
import com.fintrack.model.User;
import com.fintrack.service.ExpenseService;
import com.fintrack.service.CategoryService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    public ExpenseController(ExpenseService expenseService, CategoryService categoryService) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }

    // Show expenses
    @GetMapping
    public String listExpenses(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedUser");

        if (loggedInUser == null)
            return "redirect:/signin";

        List<Expense> expenses = expenseService.getExpensesByUser(loggedInUser);
        List<Category> categories = categoryService.getUserCategoriesByDescription(loggedInUser.getId(), "expense");

        model.addAttribute("expenses", expenses);
        model.addAttribute("categories", categories);

        List<String> labels = expenses.stream()
                .map(exp -> exp.getCategory().getName())
                .toList();

        List<Double> amounts = expenses.stream()
                .map(Expense::getAmount)
                .toList();

        model.addAttribute("expenseLabels", labels);
        model.addAttribute("expenseAmounts", amounts);

        return "expense"; // expense.html
    }

    // Add expense
    @PostMapping("/add")
    public String addExpense(@ModelAttribute Expense expense, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedUser");
        expense.setUser(loggedInUser); // Link expense to logged-in user
        expenseService.saveExpense(expense);
        return "redirect:/expense";
    }

    // Delete expense
    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedUser");

        if (loggedInUser != null) {
            expenseService.deleteExpenseByIdAndUser(id, loggedInUser);
        }
        return "redirect:/expense";
    }

    @GetMapping("/pdf")
    public void downloadExpensePdf(HttpServletResponse response, HttpSession session) throws IOException {
        // Get logged-in user
        User loggedInUser = (User) session.getAttribute("loggedUser");
        if (loggedInUser == null) {
            response.sendRedirect("/signin");
            return;
        }

        // Fetch expenses for this user
        List<Expense> expenses = expenseService.getExpensesByUser(loggedInUser);

        // Create PDF
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Title
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 800);
        contentStream.showText("Expense Report");
        contentStream.endText();

        // User info
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 770);
        contentStream.showText("User: " + loggedInUser.getName() + ", Email: " + loggedInUser.getEmail());
        contentStream.endText();

        // Table settings
        float margin = 50;
        float yStart = 740;
        // float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float yPosition = yStart;
        float rowHeight = 20;
        float tableBottomY = 100;

        // Define headers
        String[] headers = { "ID", "Source", "Amount", "Date" };
        float[] colWidths = { 50, 200, 100, 100 };

        // Draw table header
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        float nextX = margin;
        for (int i = 0; i < headers.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(nextX + 2, yPosition);
            contentStream.showText(headers[i]);
            contentStream.endText();
            nextX += colWidths[i];
        }
        yPosition -= rowHeight;

        // Table rows
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        for (Expense exp : expenses) {
            if (yPosition <= tableBottomY) {
                // Add new page if needed
                contentStream.close();
                page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = yStart;
            }

            nextX = margin;
            String[] rowData = {
                    String.valueOf(exp.getId()),
                    exp.getSource(),
                    String.valueOf(exp.getAmount()),
                    exp.getDate().toString()
            };

            for (int i = 0; i < rowData.length; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(nextX + 2, yPosition);
                contentStream.showText(rowData[i]);
                contentStream.endText();
                nextX += colWidths[i];
            }
            yPosition -= rowHeight;
        }

        contentStream.close();

        // Response setup
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=expense-report.pdf");

        document.save(response.getOutputStream());
        document.close();
    }
}
