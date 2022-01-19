package app.condominio.controller;

import java.math.BigDecimal;
import java.net.ConnectException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.ModelAndView;

import app.condominio.service.RelatorioService;
import app.condominio.utils.GatewayUtils;

@Controller
@RequestMapping("/sindico")
public class PainelController {
	@Autowired
	private GatewayUtils gatewayUtils;
	
	@ModelAttribute("ativo")
	public String[] ativo() {
		return new String[] { "painel", "" };
	}
	
	@ModelAttribute("contaService")
	public String contaService() {
		return gatewayUtils.getUrl() + "/sindico/contas";
//		return  "/sindico/contas";
	}

	@Autowired
	RelatorioService relatorioService;

	@GetMapping({ "/", "", "/painel", "/dashboard" })
	public ModelAndView sindico(ModelMap model) {
		
		try {
			model.addAttribute("saldoAtual", relatorioService.saldoAtualTodasContas());
			model.addAttribute("receitaDespesaMes", relatorioService.receitaDespesaMesAtual());
			model.addAttribute("receitaDespesaRealizada", relatorioService.receitaDespesaRealizadaPeriodoAtual());
			model.addAttribute("contaServiceConnectErro", false);
		}catch(RestClientException e) {
			if(e.getCause() instanceof ConnectException) {
				model.addAttribute("saldoAtual", BigDecimal.ZERO.setScale(2));
				model.addAttribute("receitaDespesaMes", BigDecimal.ZERO.setScale(2));
				model.addAttribute("receitaDespesaRealizada", BigDecimal.ZERO.setScale(2));
				model.addAttribute("contaServiceConnectErro", true);
			}
		}
		
		
		model.addAttribute("inadimplencia", relatorioService.inadimplenciaAtual());		
		model.addAttribute("receitaDespesaOrcada", relatorioService.receitaDespesaOrcadaPeriodoAtual());

		model.addAttribute("conteudo", "painel");
		return new ModelAndView("fragmentos/layoutSindico", model);
	}

}

