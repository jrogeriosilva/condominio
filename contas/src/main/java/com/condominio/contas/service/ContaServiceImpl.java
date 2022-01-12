package com.condominio.contas.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.condominio.contas.dao.ContaDao;
import com.condominio.contas.domain.Conta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional
public class ContaServiceImpl implements ContaService {

	@Autowired
	private ContaDao contaDao;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public void salvar(Conta entidade) {
		if (entidade.getIdConta() == null) {
			padronizar(entidade);
			// LATER fazer esta alteração com trigger
			entidade.setSaldoAtual(entidade.getSaldoInicial());
			contaDao.save(entidade);
		}
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Conta ler(Long id) {
		return contaDao.findById(id).get();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Conta> listar() {
		Long condominio = usuarioService.lerLogado().getCondominio();
		if (condominio == null) {
			return new ArrayList<>();
		}
		return getContasCondominio(condominio);
	}

	@Override
	public Page<Conta> listarPagina(Pageable pagina) {
		Long condominio = usuarioService.lerLogado().getCondominio();

		if (condominio == null) {
			return Page.empty(pagina);
		}
		return contaDao.findAllByIdCondominioOrderBySiglaAsc(condominio, pagina);
	}

	@Override
	public void editar(Conta entidade) {
		padronizar(entidade);
		// LATER fazer esta alteração com trigger
		Conta antiga = ler(entidade.getIdConta());
		entidade.setSaldoAtual(
				antiga.getSaldoAtual().subtract(antiga.getSaldoInicial()).add(entidade.getSaldoInicial()));
		contaDao.save(entidade);
	}

	@Override
	public void excluir(Conta entidade) {
		contaDao.delete(entidade);

	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void validar(Conta entidade, BindingResult validacao) {
		// VALIDAÇÕES NA INCLUSÃO
		if (entidade.getIdConta() == null) {
			// Sigla não pode repetir
			if (contaDao.existsBySiglaAndIdCondominio(entidade.getSigla(), usuarioService.lerLogado().getCondominio())) {
				validacao.rejectValue("sigla", "Unique");
			}
		}
		// VALIDAÇÕES NA ALTERAÇÃO
		else {
			// Sigla não pode repetir
			if (contaDao.existsBySiglaAndIdCondominioAndIdContaNot(entidade.getSigla(),
					usuarioService.lerLogado().getCondominio(), entidade.getIdConta())) {
				validacao.rejectValue("sigla", "Unique");
			}
		}
		// VALIDAÇÕES EM AMBOS

	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void padronizar(Conta entidade) {
		if (entidade.getIdCondominio() == null) {
			entidade.setIdCondominio(usuarioService.lerLogado().getCondominio());
		}
		if (entidade.getSaldoInicial() == null) {
			entidade.setSaldoInicial(BigDecimal.ZERO);
		}
		if (entidade.getSaldoAtual() == null) {
			entidade.setSaldoAtual(BigDecimal.ZERO);
		}
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public BigDecimal saldoAtual() {
		Long condominio = usuarioService.lerLogado().getCondominio();

		if(condominio != null) {
			BigDecimal saldo = contaDao.sumSaldoAtualByCondominio(condominio);
			return saldo == null ? BigDecimal.ZERO.setScale(2) : saldo;
		}else {
			return BigDecimal.ZERO.setScale(2);
		}
	}

	public List<Conta>  getContasCondominio(Long condominio) {
		return contaDao.findByIdCondominio(condominio);
	}

}
