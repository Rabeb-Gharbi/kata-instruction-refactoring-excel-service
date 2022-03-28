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
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class ExportCampaignServiceImpl implements ExportCampaignService{

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

    public void sendResults(Campaign campaign, Survey survey){
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Survey");
        sheet.setColumnWidth(0, 10500);
        for (int i = 1; i <= 18; i++) {
            sheet.setColumnWidth(i, 6000);
        }

        // 1ere ligne =  l'entête
        Row header = sheet.createRow(0);
        CellStyle headerStyle = getCellStyle(workbook, IndexedColors.LIGHT_BLUE.getIndex(),FillPatternType.SOLID_FOREGROUND,"Arial", (short) 14, true,false);

        Cell headerCell=createCell(0,"Survey",header,headerStyle);

        CellStyle titleStyle = getCellStyle(workbook, IndexedColors.LIGHT_GREEN.getIndex(),FillPatternType.SOLID_FOREGROUND,"Arial", (short) 12, true,true);

        CellStyle style = workbook.createCellStyle();

        // section client

        Row row = sheet.createRow(2);
        Cell cell=createCell(0,"Client",row,titleStyle);

        Row clientRow = sheet.createRow(3);
        Cell nomClientRowLabel=createCell(0,survey.getClient(),clientRow,style);

        StringBuilder clientAddressValue = new StringBuilder(survey.getClientAddress().getStreetNumber()).append(survey.getClientAddress().getStreetName())
                .append(survey.getClientAddress().getPostalCode()).append(survey.getClientAddress().getCity());
        String clientAddress = clientAddressValue.toString();

        Row clientAddressLabelRow = sheet.createRow(4);
        Cell clientAddressCell = createCell(0,clientAddress,clientAddressLabelRow,style);

        row = sheet.createRow(6);
        cell = row.createCell(0);
        cell.setCellValue("Number of surveys");
        cell = row.createCell(1);
        cell.setCellValue(campaign.getAddressStatuses().size());

        Row surveyLabelRow = sheet.createRow(8);
        createCell(0,"N° street",surveyLabelRow,style);

        createCell(1,"streee",surveyLabelRow,style);

        createCell(2,"Postal code",surveyLabelRow,style);

        createCell(3,"City",surveyLabelRow,style);

        createCell(4,"Status",surveyLabelRow,style);

        int startIndex = 9;
        int currentIndex = 0;

        for (AddressStatus addressStatus : campaign.getAddressStatuses()) {

            Row surveyRow = sheet.createRow(startIndex + currentIndex);
            createCell(0,addressStatus.getAddress().getStreetNumber(),surveyRow,style);
            createCell(1,addressStatus.getAddress().getStreetName(),surveyRow,style);
            createCell(2,addressStatus.getAddress().getPostalCode(),surveyRow,style);
            createCell(3,addressStatus.getAddress().getCity(),surveyRow,style);
            createCell(3,addressStatus.getStatus().toString(),surveyRow,style);

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

    private CellStyle getCellStyle(Workbook workbook, short foregroundColor,FillPatternType pattern,String fontName, short fontHeight, boolean bolt,boolean wrapText ) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(foregroundColor);
        headerStyle.setFillPattern(pattern);
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontHeight);
        font.setBold(bolt);
        headerStyle.setFont(font);
        headerStyle.setWrapText(wrapText);
        return headerStyle;
    }

    private Cell createCell(int indexCell,String cellValue,Row row,CellStyle cellStyle) {
        Cell cell = row.createCell(indexCell);
        cell.setCellValue(cellValue);
        cell.setCellStyle(cellStyle);
        return cell;

    }

    public void writeFileAndSend(Survey survey, Workbook workbook) throws ExportCampaignException,IOException {
        try {
            File resultFile = new File(System.getProperty("java.io.tmpdir"), "survey-" + survey.getId() + "-" + dateTimeFormatter.format(LocalDate.now()) + ".xlsx");
            FileOutputStream outputStream = new FileOutputStream(resultFile);
            workbook.write(outputStream);

            mailService.send(resultFile);
            resultFile.deleteOnExit();
        } catch(Exception ex) {
            throw new ExportCampaignException("Error while trying to send email", ex);
        } finally {
            workbook.close();
        }
    }

}
