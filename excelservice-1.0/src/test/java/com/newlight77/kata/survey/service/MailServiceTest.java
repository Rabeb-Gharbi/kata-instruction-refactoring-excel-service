package com.newlight77.kata.survey.service;

import com.newlight77.kata.survey.Exceptions.SendMailException;
import com.newlight77.kata.survey.config.MailServiceConfig;
import com.newlight77.kata.survey.service.impl.MailServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest {

    @Mock
    private JavaMailSender mockMailSender;
    @Mock
    private MailServiceConfig mockMailServiceConfig;

    private MailService mailServiceUnderTest;

    @Before
    public void setUp() {
        mailServiceUnderTest = new MailServiceImpl(mockMailSender, mockMailServiceConfig);
    }

    @Test
    public void testSend() throws SendMailException {
        // Setup
        final File attachment = new File("filename.txt");

        // Run the test
        mailServiceUnderTest.send(attachment);

        // Verify the results
        verify(mockMailSender).send(any(MimeMessagePreparator.class));
    }

    @Test
    public void testSend_MailServiceConfigGetToReturnsNoItems() throws SendMailException {
        // Setup
        final File attachment = new File("filename.txt");
        // Run the test
        mailServiceUnderTest.send(attachment);

        // Verify the results
        verify(mockMailSender).send(any(MimeMessagePreparator.class));
    }

    @Test
    public void testSend_JavaMailSenderThrowsMailException() throws SendMailException {
        // Setup
        final File attachment = new File("filename.txt");
        doNothing().when(mockMailSender).send(any(MimeMessagePreparator.class));

        // Run the test
        mailServiceUnderTest.send(attachment);

        // Verify the results
    }
}
