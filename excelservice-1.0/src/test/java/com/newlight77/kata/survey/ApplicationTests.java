package com.newlight77.kata.survey;

import com.newlight77.kata.survey.Exceptions.SendMailException;
import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.ExportCampaignService;
import com.newlight77.kata.survey.service.MailService;
import com.newlight77.kata.survey.service.impl.MailServiceImpl;
import com.newlight77.kata.survey.util.JsonUtil;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTests {

	@Autowired
	private ExportCampaignService exportCampaignService;

	@MockBean
	private MailServiceImpl mailService;

	private String surveyJson;

	private String campaignJson;

	private Survey survey;

	private Campaign campaign;

	@Before
	public void setUp() {
		surveyJson = "{\n" +
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
		campaignJson = "{\n" +
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

		survey = JsonUtil.instance().fromJson(surveyJson, Survey.class);
		campaign = JsonUtil.instance().fromJson(campaignJson, Campaign.class);


	}


	@Test
	public void exportcampaign() throws SendMailException {

		System.out.println("surveyJson =>" +surveyJson);
		// WHEN
		Mockito.doNothing().when(mailService)
				.send(any(File.class));
		exportCampaignService.creerSurvey(survey);
		exportCampaignService.createCampaign(campaign);

		exportCampaignService.sendResults(campaign,survey);
		// THEN
		Mockito.verify(mailService, Mockito.times(1)).send(any(File.class));

	}


}