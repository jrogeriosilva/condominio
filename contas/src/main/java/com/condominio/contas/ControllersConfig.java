package com.condominio.contas;

import com.condominio.contas.domain.Usuario;
import com.condominio.contas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;



@ControllerAdvice
public class ControllersConfig {

	@Autowired
	UsuarioService usuarioService;

	@ModelAttribute("haCondominio")
	public boolean haCondominio() {
		Usuario usuario = usuarioService.lerLogado();
		if (usuario == null) {
			return false;
		}
		return usuario.getCondominio() != null;
	}

	@ModelAttribute("gateway")
	public String gateway() {
		return "http://localhost:8080";
	}

}
