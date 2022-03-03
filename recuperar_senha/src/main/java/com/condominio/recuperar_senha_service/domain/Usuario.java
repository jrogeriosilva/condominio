package com.condominio.recuperar_senha_service.domain;


public class Usuario {
    private String userName;
    private String nome;
    private String sobrenome;
    private String email;
    private Long condominio;
    private String senha;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCondominio() {
        return condominio;
    }

    public void setCondominio(Long condominio) {
        this.condominio = condominio;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

