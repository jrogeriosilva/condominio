package com.condominio.recuperar_senha_service.domain;

public class Email {
    private String recipient;
    private String subject;
    private String message;

    public Email() {}

    public  Email(String recipient, String subject, String message) {
        this.recipient = recipient;
        this.message = message;
        this.subject = subject;
    }

    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
