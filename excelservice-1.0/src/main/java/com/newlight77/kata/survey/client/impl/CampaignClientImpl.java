package com.newlight77.kata.survey.client.impl;

import com.newlight77.kata.survey.client.CampaignClient;
import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CampaignClientImpl implements CampaignClient {

    private WebClient webClient;

    private static final String uriSurvey="/surveys";
    private static final String uriCampaign="/campaigns";



    public CampaignClientImpl(@Value("${external.url}") String externalUrl) {
        webClient = WebClient.builder()
                .baseUrl(externalUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    public void createSurvey(Survey survey) {
        webClient.post()
                .uri(uriSurvey)
                .syncBody(survey)
                .retrieve();
    }

    public Survey getSurvey(String id) {
        return webClient.get()
                .uri(uriSurvey + id)
                .retrieve()
                .bodyToMono(Survey.class).block();
    }

    public void createCampaign(Campaign campaign) {
        webClient.post()
                .uri(uriCampaign)
                .syncBody(campaign);
    }

    public Campaign getCampaign(String id) {
        return webClient.get()
                .uri(uriCampaign+ id)
                .retrieve()
                .bodyToMono(Campaign.class).block();
    }
}
