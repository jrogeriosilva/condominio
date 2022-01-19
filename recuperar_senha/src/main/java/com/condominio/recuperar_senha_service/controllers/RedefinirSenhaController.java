package com.condominio.recuperar_senha_service.controllers;

import com.condominio.recuperar_senha_service.exceptions.EmailMicroServicoConnectRefuseException;
import com.condominio.recuperar_senha_service.exceptions.UsuarioNotFoundException;
import com.condominio.recuperar_senha_service.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/conta")
public class RedefinirSenhaController {
    private String gateawy = "http://localhost:8080";
    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute("ativo")
    public String[] ativo() {
        return new String[] { "conta", "" };
    }

    @GetMapping("/redefinir")
    public ModelAndView preRedefinir() {
        return new ModelAndView("fragmentos/layoutSite", "conteudo", "usuarioRedefinirSenha");
    }

    @PostMapping("/redefinir")
    public String postRedefinir(@RequestParam("username") String username) {
        try{
            usuarioService.redefinirSenha(username);
            return "redirect:" +  gateawy + "/conta/redefinir?email&username=" + username;
        }catch (EmailMicroServicoConnectRefuseException ecf) {
            System.out.println("Erro: o mircroservico de enviar email recusou a conecao");
            return "redirect:" +  gateawy + "/conta/redefinir?sendEmailErro";
        }catch (UsuarioNotFoundException e) {
            return "redirect:" +  gateawy + "/conta/redefinir?erro&username=" + username;
        }
    }

    @PutMapping("/redefinir")
    public String putRedefinir(@RequestParam("username") String username, @RequestParam("password") String password,
                               @RequestParam("token") String token) {
        if (usuarioService.redefinirSenha(username, token, password)) {
            return "redirect:" +  gateawy + "/login?redefinido";
        } else {
            return "redirect:" +  gateawy + "/conta/redefinir?invalido";
        }
    }
}
