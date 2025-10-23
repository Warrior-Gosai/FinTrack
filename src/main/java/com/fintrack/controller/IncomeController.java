package com.fintrack.controller;

import com.fintrack.model.Category;
import com.fintrack.model.Income;
import com.fintrack.model.User;
import com.fintrack.service.CategoryService;
import com.fintrack.service.IncomeService;

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
@RequestMapping("/income")
public class IncomeController {

    private final IncomeService incomeService;
    private final CategoryService categoryService;

    public IncomeController(IncomeService incomeService, CategoryService categoryService) {
        this.incomeService = incomeService;
        this.categoryService = categoryService;
    }

    // Show income page
    @GetMapping
    public String incomePage(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null)
            return "redirect:/signin";

        // fetch only this user's incomes
        List<Income> incomes = incomeService.getIncomesByUser(loggedUser);
        model.addAttribute("incomes", incomes);

        // fetch only categories of type "income" for this user
        List<Category> categories = categoryService.getCategoriesByUserAndDescription(loggedUser, "income");
        model.addAttribute("categories", categories);

        List<String> labels = incomes.stream()
                .map(i -> i.getCategory().getName())
                .toList();

        List<Double> amounts = incomes.stream()
                .map(Income::getAmount)
                .toList();

        model.addAttribute("labels", labels);
        model.addAttribute("amounts", amounts);

        return "income";
    }

    // Add new income
    @PostMapping("/add")
    public String addIncome(@ModelAttribute Income income, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        income.setUser(loggedUser); // assign income to logged-in user
        incomeService.saveIncome(income, loggedUser);
        return "redirect:/income";
    }

    // Update income
    @PostMapping("/update/{id}")
    public String updateIncome(@PathVariable Long id, @ModelAttribute Income updatedIncome, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/signin";

        Income existing = incomeService.getIncomeById(user.getId()).orElse(null);
        if (existing != null && existing.getUser().equals(user)) {
            existing.setSource(updatedIncome.getSource());
            existing.setAmount(updatedIncome.getAmount());
            existing.setDate(updatedIncome.getDate());
            existing.setCategory(updatedIncome.getCategory());
            incomeService.saveIncome(existing, user);
        }

        return "redirect:/income";
    }

    // Delete income
    @GetMapping("/delete/{id}")
    public String deleteIncome(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedUser");
        if (currentUser != null) {
            incomeService.deleteIncomeByIdAndUser(id, currentUser);
        }
        return "redirect:/income";
    }

    @GetMapping("/pdf")
    public void downloadIncomePdf(HttpServletResponse response, HttpSession session) throws IOException {

        User loggedInUser = (User) session.getAttribute("loggedUser");
        if (loggedInUser == null) {
            response.sendRedirect("/signin");
            return;
        }

        // Fetch all incomes for this user only
        List<Income> incomes = incomeService.getIncomesByUser(loggedInUser);

        // Create PDF document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Title
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 800);
        contentStream.showText("Income Report");
        contentStream.endText();

        // User Info in header
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 770);
        contentStream.showText("User: " + loggedInUser.getName() + ", Email: " + loggedInUser.getEmail());
        contentStream.endText();

        // Table settings
        float margin = 50;
        float yStart = 740;
        float yPosition = yStart;
        float rowHeight = 20;
        float tableBottomY = 100;

        // Define columns
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

        // Draw table rows
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        for (Income income : incomes) {
            if (yPosition <= tableBottomY) {
                // Add new page if space is over
                contentStream.close();
                page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = yStart;
            }

            nextX = margin;
            String[] rowData = {
                    String.valueOf(income.getId()),
                    income.getSource(),
                    String.valueOf(income.getAmount()),
                    income.getDate().toString()
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

        // Set response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=income-report.pdf");

        // Write PDF to response
        document.save(response.getOutputStream());
        document.close();
    }
}
