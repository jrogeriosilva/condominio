package com.condominio.recuperar_senha_service.controllers;

import com.condominio.recuperar_senha_service.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/conta")
public class RedefinirSenhaController {
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
        if (usuarioService.redefinirSenha(username)) {
            return "redirect:/conta/redefinir?email&username=" + username;
        } else {
            return "redirect:/conta/redefinir?erro&username=" + username;
        }
    }

    @PutMapping("/redefinir")
    public String putRedefinir(@RequestParam("username") String username, @RequestParam("password") String password,
                               @RequestParam("token") String token) {
        if (usuarioService.redefinirSenha(username, token, password)) {
            return "redirect:http://localhost:8080/login?redefinido";
        } else {
            return "redirect:/conta/redefinir?invalido";
        }
    }
}
