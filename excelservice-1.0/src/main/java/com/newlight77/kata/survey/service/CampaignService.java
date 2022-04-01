package com.newlight77.kata.survey.service;

import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;


public interface CampaignService {

    void createCampaign(Campaign campaign);

    Campaign getCampaign(String id);


}
