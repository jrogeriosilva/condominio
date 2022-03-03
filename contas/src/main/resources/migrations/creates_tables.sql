create database contas;

USE contas;

START TRANSACTION;

DROP TABLE IF EXISTS
	contas,contasbancarias;

CREATE TABLE contas (
  idConta BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  sigla VARCHAR(2) NULL,
  descricao VARCHAR(30) NULL,
  saldoInicial DECIMAL(9,2) NULL,
  saldoAtual DECIMAL(9,2) NULL,
  id_condominio BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY(idConta)
);

CREATE TABLE contasbancarias (
  idConta BIGINT UNSIGNED NOT NULL,
  tipo CHAR NULL,
  banco VARCHAR(3) NULL,
  agencia VARCHAR(5) NULL,
  agenciaDv CHAR NULL,
  conta VARCHAR(20) NULL,
  contaDv CHAR NULL,
  PRIMARY KEY(idConta),
  FOREIGN KEY(idConta)
    REFERENCES contas(idConta)
      ON DELETE CASCADE
      ON UPDATE CASCADE
);

COMMIT;
