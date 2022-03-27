package com.newlight77.kata.survey.service;

import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;


public interface ExportCampaignService {

    void creerSurvey(Survey survey);

    Survey getSurvey(String id);

    void createCampaign(Campaign campaign);

    Campaign getCampaign(String id);

    void sendResults(Campaign campaign, Survey survey);


}
