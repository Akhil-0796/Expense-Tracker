package com.example.demo.service;

import com.example.demo.UserAuthentication.CustomUserDetails;
import com.example.demo.model.Expense;
import com.example.demo.model.User;
import com.example.demo.repository.ExpenseRepo;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserService {
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private ExpenseRepo expenseRepo;

    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());

        return "signup_form";
    }

    public String processRegister(User user){

        String email=user.getEmail();

        if(userRepository.findByEmail(email)!=null){
            return "register_fail";
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        return "register_success";
    }

    public String userDashBoard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){
        String userId = userDetails.getUsername();
        List<Expense> listUsers = expenseRepo.findLast5(userId);

        //List<Expense> listUsers = expenseRepo.findByUser(userId);
        Double sum=0.0;
        for (Expense ex:listUsers
        ) {
            if(ex.getCurrency().equals("INR"))
                sum=sum+ex.getAmount();
            else
                sum=sum+ex.getAmount()*75;
        }
        model.addAttribute("sum", sum);
        List<Expense> l=new ArrayList<>();

        if(listUsers.size()>5){
            for(int i=0;i<5;i++){
                l.add(listUsers.get(i));
            }
        }else{
            l=listUsers;
        }

        model.addAttribute("listExpenses",l);
        return "dash_board";
    }

    public String homePage(){
        return "home";
    }

    public String report(@AuthenticationPrincipal CustomUserDetails userDetails,Model model){
        String userId = userDetails.getUsername();
        List<Expense> listUsers = expenseRepo.findByUser(userId);
        HashMap<String,Double> map=new HashMap<>();

        for (Expense ex:listUsers) {
            if(!map.containsKey(ex.getCategory())){
                if(ex.getCurrency().equals("INR")){
                    map.put(ex.getCategory(),ex.getAmount());
                }else{
                    map.put(ex.getCategory(),ex.getAmount()*75);
                }
            }else{
                if(ex.getCurrency().equals("INR")){
                    Double amt=map.get(ex.getCategory())+ex.getAmount();
                    map.put(ex.getCategory(),amt);
                }else{
                    Double amt=map.get(ex.getCategory())+ex.getAmount()*75;
                    map.put(ex.getCategory(),amt);
                }

            }
        }

        List<Expense> firstList = expenseRepo.findFirst(userId);

        Date date=new Date();
        Format formatter = new SimpleDateFormat("MMMM");
        String s = formatter.format(date);

        TreeMap<String,Double> map1=new TreeMap<>();
        map1.put("week-1",0.0);
        map1.put("week-2",0.0);
        map1.put("week-3",0.0);
        map1.put("week-4",0.0);
        int c1=1;
        int c2=1;
        int c3=1;
        int c4=1;

        for (Expense ex:firstList) {
            int d2=ex.getDate().getDate();
            System.out.println(d2);
            if(Math.abs(d2-c1)/7==0){
                if(ex.getCurrency().equals("INR")){
                    map1.put("week-1",map1.get("week-1")+ex.getAmount());

                }else{
                    map1.put("week-1",map1.get("week-1")+ex.getAmount()*75);

                }
                c1++;
            }else if(Math.abs(d2-c2)/7==1){
                if(ex.getCurrency().equals("INR")){
                    map1.put("week-2",map1.get("week-2")+ex.getAmount());

                }else{
                    map1.put("week-2",map1.get("week-2")+ex.getAmount()*75);

                }
                c2++;
            }else if(Math.abs(d2-c3)/7==2){
                if(ex.getCurrency().equals("INR")){
                    map1.put("week-3",map1.get("week-3")+ex.getAmount());

                }else{
                    map1.put("week-3",map1.get("week-3")+ex.getAmount()*75);

                }
                c3++;
            }else if(Math.abs(d2-c4)/7==3){
                if(ex.getCurrency().equals("INR")){
                    map1.put("week-4",map1.get("week-4")+ex.getAmount());

                }else{
                    map1.put("week-4",map1.get("week-4")+ex.getAmount()*75);

                }
                c4++;
            }
        }
        model.addAttribute("month",s);
        model.addAttribute("weekList",map1);
        System.out.println(map1);

        model.addAttribute("listExpenses",map);
        return "report";
    }
}
