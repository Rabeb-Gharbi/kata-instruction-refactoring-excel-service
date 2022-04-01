package com.newlight77.kata.survey.client;

import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import org.springframework.stereotype.Service;

@Service
public interface CampaignClient{


    void createSurvey(Survey survey);

    Survey getSurvey(String id) ;

    void createCampaign(Campaign campaign) ;

    Campaign getCampaign(String id) ;
}
