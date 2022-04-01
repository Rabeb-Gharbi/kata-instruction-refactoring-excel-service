package com.newlight77.kata.survey.service;

import com.newlight77.kata.survey.model.Survey;


public interface SurveyService {

    void creerSurvey(Survey survey);

    Survey getSurvey(String id);


}
