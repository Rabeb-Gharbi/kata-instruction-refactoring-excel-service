package com.newlight77.kata.survey.service;

import com.newlight77.kata.survey.Exceptions.ExportCampaignException;
import com.newlight77.kata.survey.Exceptions.SendMailException;
import com.newlight77.kata.survey.client.impl.CampaignClientImpl;
import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.impl.ExportCampaignServiceImpl;
import com.newlight77.kata.survey.service.impl.MailServiceImpl;
import com.newlight77.kata.survey.util.JsonUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.io.File;
import java.io.IOException;

public class ExportCampaignServiceTest {

    private ExportCampaignServiceImpl service;

    @Mock
    private CampaignClientImpl campaignClientMock;

    @Mock
    private MailServiceImpl mailServiceMock;

    @Captor
    private ArgumentCaptor<Survey> surveyCaptor;

    @Captor
    private ArgumentCaptor<Workbook> workBookCaptor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        service = new ExportCampaignServiceImpl(campaignClientMock, mailServiceMock);
        service = Mockito.spy(service);
    }

    @Test
    public void test() throws IOException, ExportCampaignException, SendMailException {

        // Arrange
        String surveyJson = "{\n" +
                "    \"id\" : \"surveyId\",\n" +
                "    \"sommary\" : \"sommary\",\n" +
                "    \"client\" : \"client's name\",\n" +
                "    \"clientAddress\" : {\n" +
                "        \"id\" : \"addressId1\",\n" +
                "        \"streetNumber\" : \"10\",\n" +
                "        \"streetName\" : \"rue de Rivoli\",\n" +
                "        \"postalCode\" : \"75001\",\n" +
                "        \"city\" : \"Paris\"\n" +
                "    },\n" +
                "    \"questions\" : [{\n" +
                "        \"id\" : \"questionId1\",\n" +
                "        \"surveyQuestion\" : \"question1\"\n" +
                "    }, \n" +
                "    {\n" +
                "       \"id\" : \"questionId2\",\n" +
                "       \"surveyQuestion\" : \"question2\"\n" +
                "   }] \n" +
                "}";

        Survey survey = JsonUtil.instance().fromJson(surveyJson, Survey.class);

        String campaignJson = "{\n" +
                "    \"id\" : \"campaignId\",\n" +
                "    \"surveyId\" : \"surveyId\",\n" +
                "    \"addressStatuses\" : [ {\n" +
                "        \"id\" : \"addressStatusesId1\",\n" +
                "        \"address\" : {\n" +
                "          \"id\" : \"addressId1\",\n" +
                "          \"streetNumber\" : \"10\",\n" +
                "          \"streetName\" : \"rue de Rivoli\",\n" +
                "          \"postalCode\" : \"75001\",\n" +
                "          \"city\" : \"Paris\"\n" +
                "        },\n" +
                "        \"status\" : \"DONE\"\n" +
                "    }, {\n" +
                "        \"id\" : \"addressStatusesId2\",\n" +
                "        \"address\" : {\n" +
                "          \"id\" : \"addressId2\",\n" +
                "          \"streetNumber\" : \"40\",\n" +
                "          \"streetName\" : \"rue de Louvre\",\n" +
                "          \"postalCode\" : \"75001\",\n" +
                "          \"city\" : \"Paris\"\n" +
                "        },\n" +
                "        \"status\" : \"TODO\"\n" +
                "    }] \n" +
                "}";
        Campaign campaign = JsonUtil.instance().fromJson(campaignJson, Campaign.class);

        Mockito.doReturn(campaign).when(campaignClientMock).getCampaign(Mockito.anyString());
        Mockito.doNothing().when(mailServiceMock).send(Mockito.any(File.class));

        // Act
        service.sendResults(campaign, survey);

        // Assert
        Mockito.verify(service).writeFileAndSend(surveyCaptor.capture(), workBookCaptor.capture());

        Workbook workbook = workBookCaptor.getValue();

        Assertions.assertThat(workbook).isNotNull();

        Sheet sheet = workbook.getSheet("Survey");
        Assertions.assertThat(sheet).isNotNull();

        Row headerRow = sheet.getRow(0);
        Assertions.assertThat(headerRow).isNotNull();
        Assertions.assertThat(headerRow.getCell(0)).isNotNull();
        Assertions.assertThat(headerRow.getCell(0).getStringCellValue()).isEqualTo("Survey");

        Row clientRow = sheet.getRow(2);
        Assertions.assertThat(clientRow).isNotNull();
        Assertions.assertThat(clientRow.getCell(0)).isNotNull();
        Assertions.assertThat(clientRow.getCell(0).getStringCellValue()).isEqualTo("Client");

        Row clientNameRow = sheet.getRow(3);
        Assertions.assertThat(clientNameRow).isNotNull();
        Assertions.assertThat(clientNameRow.getCell(0)).isNotNull();
        Assertions.assertThat(clientNameRow.getCell(0).getStringCellValue()).isEqualTo(survey.getClient());

        Row clientAddressRow = sheet.getRow(4);
        Assertions.assertThat(clientAddressRow).isNotNull();
        Assertions.assertThat(clientAddressRow.getCell(0)).isNotNull();

        Row SurveyNumberRow = sheet.getRow(6);
        Assertions.assertThat(SurveyNumberRow).isNotNull();
        Assertions.assertThat(SurveyNumberRow.getCell(0)).isNotNull();
        Assertions.assertThat(SurveyNumberRow.getCell(0).getStringCellValue()).isEqualTo("Number of surveys");
        Assertions.assertThat(SurveyNumberRow.getCell(1)).isNotNull();
        Assertions.assertThat(SurveyNumberRow.getCell(1).getNumericCellValue()).isEqualTo(campaign.getAddressStatuses().size());

        Row surveysHeaderRow = sheet.getRow(8);
        Assertions.assertThat(surveysHeaderRow).isNotNull();

    }
}
