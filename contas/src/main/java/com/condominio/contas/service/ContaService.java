package com.condominio.contas.service;

import java.math.BigDecimal;

import com.condominio.contas.domain.Conta;

public interface ContaService extends CrudService<Conta, Long> {

	public BigDecimal saldoAtual();

}
