package com.condominio.contas;

import com.condominio.contas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieFilter extends GenericFilterBean {
    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String cookieName = "JSESSIONID";
        HttpServletRequest req = (HttpServletRequest) request;
        Optional<String> requestCookie = Optional.empty();

        if(req.getCookies() != null) {
            requestCookie = Arrays.stream(req.getCookies())
                    .filter(cookie -> cookieName.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findAny();
        }

        if(requestCookie.isPresent()) {
            //System.out.println(requestCookie.get());
            usuarioService.setCookie(cookieName + "=" + requestCookie.get());
        }

        chain.doFilter(request, response);
    }
}
