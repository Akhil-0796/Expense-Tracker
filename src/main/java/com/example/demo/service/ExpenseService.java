package com.example.demo.service;

import com.example.demo.UserAuthentication.CustomUserDetails;
import com.example.demo.model.Expense;
import com.example.demo.repository.ExpenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    ExpenseRepo expenseRepo;

    public String addExpense(Model model){
        model.addAttribute("expense", new Expense());

        return "expense_form";
    }

    public String saveExpense(@AuthenticationPrincipal CustomUserDetails userDetails, Expense expense){
        String email=userDetails.getUsername();
        Date d=new Date();
        expense.setDate(d);
        expense.setUser(email);
        expenseRepo.save(expense);

        return "redirect:/users";
    }

    public String updateExpenseForm(@PathVariable("id") long id, Model model){
        Expense expense = expenseRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("expense", expense);
        return "update_user";
    }

    public String deleteExpense(@PathVariable("id") long id, Model model){
        Expense expense = expenseRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        expenseRepo.deleteById(id);
        return "redirect:/users";
    }

    public String updateExpense(@PathVariable("id") long id, Expense expense, BindingResult result, Model model){
        if (result.hasErrors()) {
            expense.setId(id);
            return "update_user";
        }
        expense.setDate(new Date());
        expenseRepo.save(expense);
        return "redirect:/users";
    }
    public String sortExpenseList(Model model, @Param("keyword") String keyword){
        if(!keyword.equals("none")) {
            List<Expense> listUsers = expenseRepo.findByCategory(keyword);
            model.addAttribute("listExpenses", listUsers);
            return "users";
        }else{
            List<Expense> listUsers = expenseRepo.findAll();
            model.addAttribute("listExpenses", listUsers);
            return "users";
        }
    }

    public String listUserExpenses(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){
        String userId = userDetails.getUsername();
        System.out.println(userId);


        List<Expense> listUsers = expenseRepo.findByUser(userId);
        model.addAttribute("listExpenses", listUsers);
        return "users";
    }

}
