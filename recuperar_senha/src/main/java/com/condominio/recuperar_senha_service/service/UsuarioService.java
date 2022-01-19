package com.condominio.recuperar_senha_service.service;


import com.condominio.recuperar_senha_service.domain.Usuario;
import com.condominio.recuperar_senha_service.exceptions.EmailMicroServicoConnectRefuseException;
import com.condominio.recuperar_senha_service.exceptions.UsuarioNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.TimeZone;

@Service
public class UsuarioService {
    @Autowired
    private EmailService emailService;

    public Usuario getUsuario(String username) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Usuario>  response = restTemplate.exchange("http://localhost:8081/conta/usuario/" + username + "/info/redefinir-senha", HttpMethod.GET, null, Usuario.class);
        System.out.println("usuario recuperado: " + response.getBody().getUserName());

        return response.getBody();
    }

    public boolean redefinirSenha(String username) throws EmailMicroServicoConnectRefuseException, UsuarioNotFoundException {
        try{
            Usuario usuario = getUsuario(username);
            if (usuario != null) {
                String para = usuario.getEmail();
                String assunto = "Condomínio App - Redefinição de Senha";
                String mensagem = "Acesse o endereço abaixo para redefinir sua senha:\n\nhttp://localhost:8080/conta/redefinir?username="
                        + usuario.getUserName() + "&token=" + getToken(usuario.getSenha())
                        + "\n\nCaso não consiga clicar no link acima, copie-o e cole em seu navegador."
                        + "\n\nPor segurança este link só é válido até o final do dia.";

                emailService.enviarEmail(para, assunto, mensagem);

                return true;
            }
        }catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new UsuarioNotFoundException();
            }
        }

        return false;
    }

    public boolean redefinirSenha(String username, String token, String password) {
        // LATER Alterar redefinição de senha para tabela de tokens e expiração
        Usuario usuario = getUsuario(username);
        if (usuario != null && getToken(usuario.getSenha()).equals(token)) {
            usuario.setSenha(password);
            editar(usuario);
            return true;
        } else {
            return false;
        }
    }

    private String getToken(String texto) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));
        String d = "" + calendar.get(Calendar.DAY_OF_YEAR);
        String a = "" + (calendar.get(Calendar.YEAR) - 2000);
        String regex = "\\\\|/|\\?|\\.|&|\\$"; // Regex in java: http://www.regexplanet.com/advanced/java/index.html

        return texto.substring(8).replaceAll(regex, d) + a;
    }

    private void editar(Usuario usuario) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity request = new HttpEntity<Usuario>(usuario);

        ResponseEntity  response = restTemplate.exchange("http://localhost:8081/conta/usuario/password", HttpMethod.PUT,  request, String.class);
    }
}
