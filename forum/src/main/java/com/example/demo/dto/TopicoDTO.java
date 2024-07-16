package com.example.demo.dto;


import jakarta.validation.constraints.NotBlank;

public class TopicoDTO {

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotBlank(message = "Mensagem é obrigatória")
    private String mensagem;

    @NotBlank(message = "Autor é obrigatório")
    private String autor;

    @NotBlank(message = "Curso é obrigatório")
    private String curso;

    public  String getTitulo() {
        return titulo;
    }

    public void setTitulo( String titulo) {
        this.titulo = titulo;
    }

    public  String getMensagem() {
        return mensagem;
    }

    public void setMensagem( String mensagem) {
        this.mensagem = mensagem;
    }

    public  String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
