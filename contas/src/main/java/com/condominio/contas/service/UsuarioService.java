package com.condominio.contas.service;

import com.condominio.contas.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class UsuarioService {
    @Autowired
    private RestTemplate restTemplate;
    private Usuario usuarioLogado;
    private String cookie;

    public Usuario lerLogado() {
        return getUsuarioLogado();
    }

    public Usuario getUsuarioLogado() {
        if(cookie != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", cookie);
            HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(null, headers);
            ResponseEntity<Usuario>  response = restTemplate.exchange("http://localhost:8081/conta/usuario/logado", HttpMethod.GET, formEntity, Usuario.class);

            System.out.println("usuario logado: " + response.getBody().getUserName());

            usuarioLogado = response.getBody();
            return usuarioLogado;
        }else {
            throw new RuntimeException("Cookie não definido");
        }
    }

    public String getCoockieTest() {
        String url = "http://localhost:8081/entrar";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); //Optional in case server sends back JSON data

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("username", "sindico_teste");
        requestBody.add("password", "1234");

        HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, formEntity, String.class);

        HttpHeaders responseHeaders = response.getHeaders();
        String set_cookie = responseHeaders.getFirst(HttpHeaders.SET_COOKIE);
        System.out.println("cookie: " + set_cookie.split(" ")[0]);

        String tempCookie = set_cookie.split(" ")[0];
        cookie = tempCookie.substring(0, tempCookie.length());

        return this.cookie;
    }

    public String getCookie() {
        return this.cookie;
    }

    public void setCookie(String cookie) {
        System.out.println("cookie: " + cookie);
        this.cookie = cookie;
    }
}
