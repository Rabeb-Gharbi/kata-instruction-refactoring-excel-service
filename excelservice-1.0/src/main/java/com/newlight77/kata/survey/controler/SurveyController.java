package com.newlight77.kata.survey.controler;

import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.impl.ExportCampaignServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
public class SurveyController {

    private ExportCampaignServiceImpl exportCampaignService;

    private static final String uriCreateSurvey="/api/survey";

    private static final String uriGetSurvey="/api/survey/{id}";

    private static final String uriCreateCampaign="/api/survey/campaign";

    private static final String uriGetCampaign="/api/survey/campaign/{id}";

    private static final String uriExportCampaign="/api/survey/campaign/export";


    public SurveyController(final ExportCampaignServiceImpl exportCampaignService) {
        this.exportCampaignService = exportCampaignService;
    }

    @RequestMapping(value = uriCreateSurvey,  method = RequestMethod.POST)
    public void createSurvey(@RequestBody Survey survey) {
        exportCampaignService.creerSurvey(survey);
    }

    @RequestMapping(value = uriGetSurvey, method = RequestMethod.GET)
    public Survey getSurvey(@RequestParam  @PathVariable("id") String id) {
        return exportCampaignService.getSurvey(id);
    }

    @RequestMapping(value = uriCreateCampaign, method = RequestMethod.POST)
    public void createCampaign(@RequestBody Campaign campaign) {
        exportCampaignService.createCampaign(campaign);
    }

    @RequestMapping(value = uriGetCampaign, method = RequestMethod.GET)
    public Campaign getCampaign(@RequestParam @PathVariable("id") String id) {
        return exportCampaignService.getCampaign(id);
    }

    @RequestMapping(value = uriExportCampaign, method = RequestMethod.POST)
    public void exportCampaign(@RequestParam String campaignId) {
        Campaign campaign = exportCampaignService.getCampaign(campaignId);
        Survey survey = exportCampaignService.getSurvey(campaign.getSurveyId());
        exportCampaignService.sendResults(campaign, survey);

    }
}

