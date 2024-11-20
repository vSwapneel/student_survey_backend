package com.example.assignment3.assignment3_backend.repository;

import com.example.assignment3.assignment3_backend.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
