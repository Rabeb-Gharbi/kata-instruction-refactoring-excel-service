package com.newlight77.kata.survey.service.impl;

import com.newlight77.kata.survey.Exceptions.SendMailException;
import com.newlight77.kata.survey.config.MailServiceConfig;
import com.newlight77.kata.survey.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
  private JavaMailSender mailSender;
  private MailServiceConfig mailServiceConfig;
  private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

  public MailServiceImpl(final JavaMailSender mailSender, final MailServiceConfig mailServiceConfig) {
    this.mailSender = mailSender;
    this.mailServiceConfig = mailServiceConfig;
  }

  public void send(File attachment) throws SendMailException {
    MimeMessagePreparator messagePreparator = mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
      messageHelper.setFrom(mailServiceConfig.getFrom());
      messageHelper.setTo(mailServiceConfig.getTo());
      messageHelper.setSubject("Campaign Results");
      messageHelper.setText("Hi,\n\nYou will find in the attached file the campaign results.");

      FileSystemResource file = new FileSystemResource(attachment);
      messageHelper.addAttachment(file.getFilename(), file);
    };
    try {
      mailSender.send(messagePreparator);
      logger.info("Email sent successfully");
    } catch (MailException e) {
      throw new SendMailException("An error occurred while sending an email", e);
    }
  }

}
