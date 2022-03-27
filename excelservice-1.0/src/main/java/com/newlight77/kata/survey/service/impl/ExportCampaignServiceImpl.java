package com.newlight77.kata.survey.service.impl;

import com.newlight77.kata.survey.Exceptions.ExportCampaignException;
import com.newlight77.kata.survey.client.CampaignClient;
import com.newlight77.kata.survey.model.AddressStatus;
import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.ExportCampaignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class ExportCampaignServiceImpl implements ExportCampaignService {

  private CampaignClient campaignWebService;
  private MailServiceImpl mailService;
  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  
  public ExportCampaignServiceImpl(final CampaignClient campaignWebService, MailServiceImpl mailService) {
    this.campaignWebService = campaignWebService;
    this.mailService = mailService;
  }

  public void creerSurvey(Survey survey) {
    campaignWebService.createSurvey(survey);
  }

  public Survey getSurvey(String id) {
    return campaignWebService.getSurvey(id);
  }

  public void createCampaign(Campaign campaign) {
    campaignWebService.createCampaign(campaign);
  }

  public Campaign getCampaign(String id) {
    return campaignWebService.getCampaign(id);
  }

  public void sendResults(Campaign campaign, Survey survey) {
    Workbook workbook = new XSSFWorkbook();

    Sheet sheet = workbook.createSheet("Survey");
    sheet.setColumnWidth(0, 10500);
    for (int i = 1; i <= 18; i++) {
      sheet.setColumnWidth(i, 6000);
    }

    // 1ere ligne =  l'entête
    Row header = sheet.createRow(0);

    CellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    XSSFFont font = ((XSSFWorkbook) workbook).createFont();
    font.setFontName("Arial");
    font.setFontHeightInPoints((short) 14);
    font.setBold(true);
    headerStyle.setFont(font);
    headerStyle.setWrapText(false);

    Cell headerCell = header.createCell(0);
    headerCell.setCellValue("Survey");
    headerCell.setCellStyle(headerStyle);

    CellStyle titleStyle = workbook.createCellStyle();
    titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
    titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    XSSFFont titleFont = ((XSSFWorkbook) workbook).createFont();
    titleFont.setFontName("Arial");
    titleFont.setFontHeightInPoints((short) 12);
    titleFont.setUnderline(FontUnderline.SINGLE);
    titleStyle.setFont(titleFont);

    CellStyle style = workbook.createCellStyle();
    style.setWrapText(true);

    // section client
    Row row = sheet.createRow(2);
    Cell cell = row.createCell(0);
    cell.setCellValue("Client");
    cell.setCellStyle(titleStyle);

    Row clientRow = sheet.createRow(3);
    Cell nomClientRowLabel = clientRow.createCell(0);
    nomClientRowLabel.setCellValue(survey.getClient());
    nomClientRowLabel.setCellStyle(style);

    String clientAddress = survey.getClientAddress().getStreetNumber() + " "
            + survey.getClientAddress().getStreetName() + survey.getClientAddress().getPostalCode() + " "
            + survey.getClientAddress().getCity();

    Row clientAddressLabelRow = sheet.createRow(4);
    Cell clientAddressCell = clientAddressLabelRow.createCell(0);
    clientAddressCell.setCellValue(clientAddress);
    clientAddressCell.setCellStyle(style);

    row = sheet.createRow(6);
    cell = row.createCell(0);
    cell.setCellValue("Number of surveys");
    cell = row.createCell(1);
    cell.setCellValue(campaign.getAddressStatuses().size());

    Row surveyLabelRow = sheet.createRow(8);
    Cell surveyLabelCell = surveyLabelRow.createCell(0);
    surveyLabelCell.setCellValue("N° street");
    surveyLabelCell.setCellStyle(style);

    surveyLabelCell = surveyLabelRow.createCell(1);
    surveyLabelCell.setCellValue("streee");
    surveyLabelCell.setCellStyle(style);

    surveyLabelCell = surveyLabelRow.createCell(2);
    surveyLabelCell.setCellValue("Postal code");
    surveyLabelCell.setCellStyle(style);

    surveyLabelCell = surveyLabelRow.createCell(3);
    surveyLabelCell.setCellValue("City");
    surveyLabelCell.setCellStyle(style);

    surveyLabelCell = surveyLabelRow.createCell(4);
    surveyLabelCell.setCellValue("Status");
    surveyLabelCell.setCellStyle(style);

    int startIndex = 9;
    int currentIndex = 0;

    for (AddressStatus addressStatus : campaign.getAddressStatuses()) {

      Row surveyRow = sheet.createRow(startIndex + currentIndex);
      Cell surveyRowCell = surveyRow.createCell(0);
      surveyRowCell.setCellValue(addressStatus.getAddress().getStreetNumber());
      surveyRowCell.setCellStyle(style);

      surveyRowCell = surveyRow.createCell(1);
      surveyRowCell.setCellValue(addressStatus.getAddress().getStreetName());
      surveyRowCell.setCellStyle(style);

      surveyRowCell = surveyRow.createCell(2);
      surveyRowCell.setCellValue(addressStatus.getAddress().getPostalCode());
      surveyRowCell.setCellStyle(style);

      surveyRowCell = surveyRow.createCell(3);
      surveyRowCell.setCellValue(addressStatus.getAddress().getCity());
      surveyRowCell.setCellStyle(style);

      surveyRowCell = surveyRow.createCell(4);
      surveyRowCell.setCellValue(addressStatus.getStatus().toString());
      surveyRowCell.setCellStyle(style);

      currentIndex++;

    }

    try {
      writeFileAndSend(survey, workbook);
    } catch (IOException e) {
    log.info("Error while writing File : ", e);
  } catch (ExportCampaignException e) {
    log.info("Error while trying to send email : ", e);
  }

  }

  public void writeFileAndSend(Survey survey, Workbook workbook) throws ExportCampaignException, IOException {
    try {
      File resultFile = new File(System.getProperty("java.io.tmpdir"), "survey-" + survey.getId() + "-" + dateTimeFormatter.format(LocalDate.now()) + ".xlsx");
      FileOutputStream outputStream = new FileOutputStream(resultFile);
      workbook.write(outputStream);

      mailService.send(resultFile);
      resultFile.deleteOnExit();
    } catch(Exception ex) {
      throw new ExportCampaignException("Error while trying to send email", ex);
    } finally {
      try {
        workbook.close();
      } catch(Exception e) {
        // CANT HAPPEN
      }
    }
  }

}
