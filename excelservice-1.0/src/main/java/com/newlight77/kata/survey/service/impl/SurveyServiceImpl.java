package com.newlight77.kata.survey.service.impl;

import com.newlight77.kata.survey.Exceptions.ExportCampaignException;
import com.newlight77.kata.survey.client.CampaignClient;
import com.newlight77.kata.survey.model.AddressStatus;
import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.ExportCampaignService;
import com.newlight77.kata.survey.service.SurveyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class SurveyServiceImpl implements SurveyService{

  private CampaignClient campaignWebService;
  private MailServiceImpl mailService;
  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");



  public SurveyServiceImpl(final CampaignClient campaignWebService, MailServiceImpl mailService) {
    this.campaignWebService = campaignWebService;
    this.mailService = mailService;
  }

  public void creerSurvey(Survey survey) {
    campaignWebService.createSurvey(survey);
  }

  public Survey getSurvey(String id) {
    return campaignWebService.getSurvey(id);
  }


}