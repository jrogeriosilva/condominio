package com.condominio.recuperar_senha_service.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void enviarEmail(String para, String assunto, String mensagem) {
        System.out.println("Para: " + para);
        System.out.println("Assunto: " + assunto);
        System.out.println("Mensagem: " + mensagem);
        //TODO: Usar o microservice de enviar email
    }
}
