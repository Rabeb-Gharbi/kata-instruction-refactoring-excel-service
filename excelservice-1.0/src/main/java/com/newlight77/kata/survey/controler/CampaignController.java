package com.newlight77.kata.survey.controler;

import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.impl.CampaignServiceImpl;
import com.newlight77.kata.survey.service.impl.ExportCampaignServiceImpl;
import com.newlight77.kata.survey.service.impl.SurveyServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
public class CampaignController {


    private ExportCampaignServiceImpl exportCampaignService;

    private CampaignServiceImpl campaignService;

    private SurveyServiceImpl surveyService;


    private static final String uriCreateCampaign="/api/survey/campaign";

    private static final String uriGetCampaign="/api/survey/campaign/{id}";

    private static final String uriExportCampaign="/api/survey/campaign/export";


    public CampaignController(final ExportCampaignServiceImpl exportCampaignService, final CampaignServiceImpl campaignService, final SurveyServiceImpl surveyService) {
        this.exportCampaignService = exportCampaignService;
        this.campaignService=campaignService;
        this.surveyService=surveyService;
    }

    @RequestMapping(value = uriCreateCampaign, method = RequestMethod.POST)
    public void createCampaign(@RequestBody Campaign campaign) {
        campaignService.createCampaign(campaign);
    }

    @RequestMapping(value = uriGetCampaign, method = RequestMethod.GET)
    public Campaign getCampaign(@RequestParam @PathVariable("id") String id) {
        return campaignService.getCampaign(id);
    }

    @RequestMapping(value = uriExportCampaign, method = RequestMethod.POST)
    public void exportCampaign(@RequestParam String campaignId) {
        Campaign campaign = campaignService.getCampaign(campaignId);
        Survey survey = surveyService.getSurvey(campaign.getSurveyId());
        exportCampaignService.sendResults(campaign, survey);

    }
}

