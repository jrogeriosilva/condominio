# Microserviço de Email
Recebe um POST na Rota:
*localhost:8081/send-email*

**Formato do JSON**
```json
{
    "recipient" : "email@gmail.com",
    "subject" : "Email de Teste",
    "message" : "Isso e um Teste"
}

```

Lembrar de definir as Variáveis de Ambiente:
${MAIL_USERNAME}
${MAIL_PASSWORD}

O Email do Gmail precisa ser configurado como app menos Seguro, Já que o Aplicativo não é oficial:https://myaccount.google.com/u/3/lesssecureapps?gar=1