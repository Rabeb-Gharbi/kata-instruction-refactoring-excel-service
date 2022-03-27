package com.newlight77.kata.survey.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailServiceConfig {

  @Value("${service.mail.from}")
  private String from;

  @Value("${service.mail.to}")
  private String[] to;

  @Value("${service.mail.text}")
  private String text;

  @Value("${service.mail.subject}")
  private String subject;


  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String[] getTo() {
    return to;
  }

  public void setTo(String[] to) {
    this.to = to;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }
}
