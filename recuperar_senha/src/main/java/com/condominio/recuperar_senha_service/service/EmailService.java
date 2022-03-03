package com.condominio.recuperar_senha_service.service;

import com.condominio.recuperar_senha_service.domain.Email;
import com.condominio.recuperar_senha_service.exceptions.EmailMicroServicoConnectRefuseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;

@Service
public class EmailService {
    public void enviarEmail(String para, String assunto, String mensagem) throws EmailMicroServicoConnectRefuseException {
        System.out.println("Para: " + para);
        System.out.println("Assunto: " + assunto);
        System.out.println("Mensagem: " + mensagem);

        enviarEmailProMicroservicoChamandoDireto(new Email(para, assunto, mensagem));
    }

    private void enviarEmailProMicroservicoChamandoDireto(Email email) throws EmailMicroServicoConnectRefuseException {
        String url = "http://localhost:8083/send-email";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Email> request = new HttpEntity<>(email, requestHeaders);

        try{
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        }catch (RestClientException rce) {
            if(rce.getCause() instanceof ConnectException)
                throw new EmailMicroServicoConnectRefuseException();
        }
    }
}
