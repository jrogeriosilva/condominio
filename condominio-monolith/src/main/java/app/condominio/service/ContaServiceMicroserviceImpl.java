package app.condominio.service;


import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import app.condominio.dao.ContaDao;
import app.condominio.domain.Condominio;
import app.condominio.domain.Conta;

@Service
@Transactional
public class ContaServiceMicroserviceImpl implements ContaService {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private HttpServletRequest request;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	private String contasService = "http://localhost:8082/sindico/contas";

	@Override
	public void salvar(Conta entidade) {

	}

	@Override
	public Conta ler(Long id) {
		return null;
	}

	@Override
	public List<Conta> listar() {			
        HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(null, buildHeadersRequest());
        ResponseEntity<ContasResponse.ListaContas>  response = restTemplate.exchange(
        												contasService + "/listar", 
        												HttpMethod.GET, formEntity, ContasResponse.ListaContas.class);
       
        
        //TODO: lidar como o caso em que o servico nao responde      
		return response.getBody().getContas();
	}

	@Override
	public Page<Conta> listarPagina(Pageable pagina) {
		return null;
	}

	@Override
	public void editar(Conta entidade) {

	}

	@Override
	public void excluir(Conta entidade) {

	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void validar(Conta entidade, BindingResult validacao) {

	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void padronizar(Conta entidade) {

	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public BigDecimal saldoAtual() {
		Condominio condominio = usuarioService.lerLogado().getCondominio();
		if (condominio == null) {
			return BigDecimal.ZERO.setScale(2);
		} else {
			HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(null, buildHeadersRequest());
            ResponseEntity<ContasResponse.SaldoAtual>  response = restTemplate.exchange(
            												contasService + "/condominio/saldo/atual", 
            												HttpMethod.GET, formEntity, ContasResponse.SaldoAtual.class);
           
            
            //TODO: lidar como o caso em que o servico nao responde
			return response.getBody().getSaldoAtual();
		}
	}
	
	private HttpHeaders buildHeadersRequest() {
		String coockieName = "JSESSIONID";
		Cookie jsessionid = WebUtils.getCookie(request, "JSESSIONID");
		HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", coockieName + "=" + jsessionid.getValue());
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        
		return headers;
	}

}

class ContasResponse {
	
	public static class SaldoAtual {
		private Long condominio;
		private BigDecimal saldoAtual;
		
		
		public Long getCondominio() {
			return condominio;
		}
		
		public void setCondominio(Long condominio) {
			this.condominio = condominio;
		}
		
		public BigDecimal getSaldoAtual() {
			return saldoAtual;
		}
		
		public void setSaldoAtual(BigDecimal saldoAtual) {
			this.saldoAtual = saldoAtual;
		}
	}
	
	public static class ListaContas {
		private List<Conta> contas;

		public List<Conta> getContas() {
			return contas;
		}

		public void setContas(List<Conta> contas) {
			this.contas = contas;
		}
		
	}
}

