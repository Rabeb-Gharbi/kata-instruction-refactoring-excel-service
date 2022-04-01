package com.newlight77.kata.survey.controler;

import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.impl.ExportCampaignServiceImpl;
import com.newlight77.kata.survey.service.impl.SurveyServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SurveyController {

    private SurveyServiceImpl surveyService;


    private static final String uriCreateSurvey="/api/survey";

    private static final String uriGetSurvey="/api/survey/{id}";


    public SurveyController(final SurveyServiceImpl surveyService) {
        this.surveyService = surveyService;
    }

    @RequestMapping(value = uriCreateSurvey,  method = RequestMethod.POST)
    public ResponseEntity<List<String>> createSurvey(@RequestBody Survey survey) {
        surveyService.creerSurvey(survey);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = uriGetSurvey, method = RequestMethod.GET)
    public ResponseEntity<Survey> getSurvey(@RequestParam  @PathVariable("id") String id) {
        Survey survey=surveyService.getSurvey(id);
        return new ResponseEntity<>(survey,HttpStatus.OK);

    }

}

