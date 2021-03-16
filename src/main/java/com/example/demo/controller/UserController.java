package com.example.demo.controller;

import com.example.demo.UserAuthentication.CustomUserDetails;
import com.example.demo.model.Expense;
import com.example.demo.model.User;
import com.example.demo.repository.ExpenseRepo;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String viewHomePage() {
        //ModelAndView modelAndView = new ModelAndView("redirect:/index");
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        return userService.showRegistrationForm(model);
    }



    @PostMapping("/process_register")
    public String processRegister(User user) {

        return userService.processRegister(user);
    }


    @GetMapping("/users")
    public String listUsers(Model model) {
        List<Expense> listUsers = expenseRepo.findAll();
        model.addAttribute("listExpenses", listUsers);

        return "users";
    }

    @GetMapping("/dash")
    public String DashBoard(@AuthenticationPrincipal CustomUserDetails userDetails,Model model) {

        return  userService.userDashBoard(userDetails,model);

    }

    @GetMapping("/home")
    public String homePage(Model model) {
      return userService.homePage();
    }

    @GetMapping("/add")
    public String add(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }


    @GetMapping("/report")
    public String Reports(@AuthenticationPrincipal CustomUserDetails userDetails,Model model) {
       return userService.report(userDetails,model);
    }

}