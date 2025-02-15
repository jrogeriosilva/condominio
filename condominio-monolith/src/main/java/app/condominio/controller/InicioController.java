package app.condominio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import app.condominio.utils.GatewayUtils;

@Controller
public class InicioController {
	@Autowired
	private GatewayUtils gatewayUtils;
	
	@ModelAttribute("ativo")
	public String[] ativo() {
		return new String[] { "inicio", "" };
	}

	@GetMapping({ "/", "", "/home", "/inicio" })
	public ModelAndView inicio() {
		return new ModelAndView("fragmentos/layoutSite", "conteudo", "inicio");
	}

	@GetMapping({ "/entrar", "/login" })
	public ModelAndView preLogin() {
		return new ModelAndView("fragmentos/layoutSite", "conteudo", "login");
	}

	@GetMapping("/autenticado")
	public String posLogin(Authentication authentication) {
		String retorno = "redirect:/login?erro";
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("SINDICO"))) {
			retorno = "redirect:" + gatewayUtils.getUrl() + "/sindico";
		} else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("MORADOR"))) {
			retorno = "redirect:/morador";
		} else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
			retorno = "redirect:/admin";
		}
		return retorno;
	}
}
