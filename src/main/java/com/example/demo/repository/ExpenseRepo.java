package com.example.demo.repository;

import com.example.demo.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepo extends JpaRepository<Expense,Long> {

    public List<Expense> findByCategory(String category);


    public List<Expense> findByUser(String userId);

    @Query("select c from Expense c where c.user like %?1 order by date desc")
    List<Expense> findLast5(String user);

    @Query("select c from Expense c where c.user like %?1 order by date asc")
    List<Expense> findFirst(String user);


    // Expense findByEmail(String email);
}
