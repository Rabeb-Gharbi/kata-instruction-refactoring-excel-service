package com.newlight77.kata.survey.service;

import com.newlight77.kata.survey.Exceptions.SendMailException;

import java.io.File;


public interface MailService {

  void send(File attachment) throws SendMailException;

}
