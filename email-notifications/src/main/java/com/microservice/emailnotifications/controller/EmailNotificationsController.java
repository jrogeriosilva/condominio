package com.microservice.emailnotifications.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.emailnotifications.model.EmailModel;
import com.microservice.emailnotifications.service.EmailService;

@RestController
public class EmailNotificationsController {
	
	@Autowired
	private EmailService emailService;
	
	@PostMapping(value="/send-email",consumes = "application/json", produces = "application/json")
	public String sendEmail(@RequestBody EmailModel emailModel) {
		try {
			emailService.enviarEmail(emailModel.getRecipient(), emailModel.getSubject(), emailModel.getMessage());
			return "E-mail Enviado Com Sucesso";
		}catch(Exception ex) {
			return "Erro ao Enviar E-mail: " + ex;
		}
	}
		
}
