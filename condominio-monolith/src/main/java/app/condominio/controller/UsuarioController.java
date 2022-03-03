package app.condominio.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import app.condominio.domain.Condominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import app.condominio.domain.Usuario;
import app.condominio.dto.UsuarioDTO;
import app.condominio.service.UsuarioService;

@Controller
@RequestMapping("conta")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private HttpServletRequest request;

	@ModelAttribute("ativo")
	public String[] ativo() {
		return new String[] { "conta", "" };
	}

	@GetMapping("/cadastrar")
	public ModelAndView getCadastrarSindico(@ModelAttribute("sindico") Usuario sindico) {
		return new ModelAndView("fragmentos/layoutSite", "conteudo", "sindicoCadastro");
	}

	@PostMapping("/cadastrar")
	public ModelAndView postCadastrarSindico(@Valid @ModelAttribute("sindico") Usuario sindico,
			BindingResult validacao) {
		usuarioService.validar(sindico, validacao);
		if (validacao.hasErrors()) {
			sindico.setId(null);
			return new ModelAndView("fragmentos/layoutSite", "conteudo", "sindicoCadastro");
		}
		usuarioService.salvarSindico(sindico);
		return new ModelAndView("redirect:/login?novo");
	}

	@GetMapping("/cadastro")
	public ModelAndView getCadastroSindico(ModelMap model) {
		model.addAttribute("sindico", usuarioService.lerLogado());
		model.addAttribute("conteudo", "sindicoCadastro");
		return new ModelAndView("fragmentos/layoutSite", model);
	}

	@PutMapping("/cadastro")
	public ModelAndView putCadastroSindico(@Valid @ModelAttribute("sindico") Usuario sindico, BindingResult validacao) {
		usuarioService.validar(sindico, validacao);
		if (validacao.hasErrors()) {
			return new ModelAndView("fragmentos/layoutSite", "conteudo", "sindicoCadastro");
		}
		usuarioService.editar(sindico);
		SecurityContextHolder.clearContext();
		return new ModelAndView("redirect:/entrar?alterado");
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
			return "redirect:/login?redefinido";
		} else {
			return "redirect:/conta/redefinir?invalido";
		}
	}

	@GetMapping("/usuario/logado")
	public ResponseEntity<UsuarioDTO> getUsuarioLogado() {
		UsuarioDTO usuarioDTO =  new UsuarioDTO();
		Usuario usuario = usuarioService.lerLogado();
		
		if(usuario != null) {
			
			usuarioDTO.setUserName(usuario.getUsername());
			usuarioDTO.setNome(usuario.getNome());
			usuarioDTO.setSobrenome(usuario.getSobrenome());
			usuarioDTO.setEmail(usuario.getEmail());
			
			if(usuario.getCondominio() != null) 
				usuarioDTO.setCondominio(usuario.getCondominio().getIdCondominio());
			else 
				usuarioDTO.setCondominio(null);
			
			return ResponseEntity.ok().body(usuarioDTO);
			
		}else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/usuario/{userName}/info/redefinir-senha")
	public ResponseEntity<UsuarioDTO> getUsuarioByUserName(@PathVariable String userName) {
		UsuarioDTO usuarioDTO =  new UsuarioDTO();
		Usuario usuario = usuarioService.ler(userName);
		CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
		
		if(usuario != null) {
			
			usuarioDTO.setUserName(usuario.getUsername());
			usuarioDTO.setEmail(usuario.getEmail());
			usuarioDTO.setSenha(usuario.getPassword());
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set(csrfToken.getHeaderName(), csrfToken.getToken());
			
			return ResponseEntity.ok().headers(responseHeaders).body(usuarioDTO);
			
		}else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/usuario/password")
	public ResponseEntity atualizarSenha(@RequestBody UsuarioDTO usuarioDTO) {
		Usuario usuario = usuarioService.ler(usuarioDTO.getUserName());
		usuario.setPassword(usuarioDTO.getSenha());
		usuarioService.editar(usuario);
		
		return ResponseEntity.ok().build();
	}

}


