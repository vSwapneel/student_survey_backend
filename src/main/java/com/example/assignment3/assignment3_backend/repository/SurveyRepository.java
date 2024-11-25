package com.example.assignment3.assignment3_backend.repository;

import com.example.assignment3.assignment3_backend.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

//    Swapneel Suhas Vaidya
//    Sanjana Sujith Kumar Arekal

// Provides CRUD operations and database interaction methods for the Survey entity

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
