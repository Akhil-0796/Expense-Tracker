package com.example.demo.controller;


import com.example.demo.UserAuthentication.CustomUserDetails;
import com.example.demo.model.Expense;
import com.example.demo.repository.ExpenseRepo;
import com.example.demo.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

@Controller
public class ExpenseController {


    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private ExpenseService expenseService;


    @GetMapping("/addExpense")
    public String addExpense(Model model) {
       return expenseService.addExpense(model);
    }

    @PostMapping("/process_expense")
    public String processExpense(@AuthenticationPrincipal CustomUserDetails userDetails, Expense expense) {

       return expenseService.saveExpense(userDetails,expense);
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
       return expenseService.updateExpenseForm(id,model);
    }

    @GetMapping("/delete/{id}")
    public String DeleteExpense(@PathVariable("id") long id, Model model) {
        return expenseService.deleteExpense(id,model);
    }

    @PostMapping("/update/{id}")
    public String updateExpense(@PathVariable("id") long id, Expense expense,
                             BindingResult result, Model model) {
      return expenseService.updateExpense(id,expense,result,model);
    }


    @GetMapping("/sorting")
    public String sortExpenses(Model model, @Param("keyword") String keyword) {
       return expenseService.sortExpenseList(model,keyword);
    }


    @GetMapping("/usersEx")
    public String listUserExpense(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
    return expenseService.listUserExpenses(userDetails,model);

    }

}
