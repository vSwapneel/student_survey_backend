package com.example.assignment3.assignment3_backend.controller;

import com.example.assignment3.assignment3_backend.model.Survey;
import com.example.assignment3.assignment3_backend.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@CrossOrigin(origins = "*")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @GetMapping
    public List<Survey> getAllSurveys() {
        return surveyService.getAllSurveys();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable Long id) {
        return surveyService.getSurveyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Survey createSurvey(@RequestBody Survey survey) {
        return surveyService.saveSurvey(survey);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Survey> updateSurvey(@PathVariable Long id, @RequestBody Survey surveyDetails) {
        return surveyService.getSurveyById(id)
                .map(survey -> {
                    survey.setFirstName(surveyDetails.getFirstName());
                    survey.setLastName(surveyDetails.getLastName());
                    survey.setStreetAddress(surveyDetails.getStreetAddress());
                    survey.setCity(surveyDetails.getCity());
                    survey.setState(surveyDetails.getState());
                    survey.setZip(surveyDetails.getZip());
                    survey.setTelephone(surveyDetails.getTelephone());
                    survey.setEmail(surveyDetails.getEmail());
                    survey.setSurveyDate(surveyDetails.getSurveyDate());
                    survey.setLikedMost(surveyDetails.getLikedMost());
                    survey.setInterestSource(surveyDetails.getInterestSource());
                    survey.setRecommendation(surveyDetails.getRecommendation());
                    survey.setComments(surveyDetails.getComments());
                    return ResponseEntity.ok(surveyService.saveSurvey(survey));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        if (surveyService.getSurveyById(id).isPresent()) {
            surveyService.deleteSurvey(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
