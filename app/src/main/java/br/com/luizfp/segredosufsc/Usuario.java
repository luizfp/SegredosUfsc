package br.com.luizfp.segredosufsc;

import org.parceler.Parcel;

/**
 * Created by luiz on 2/23/16.
 */
@Parcel
public class Usuario {
    private Long id;
    private char inicial;
    private Curso curso;

    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public char getInicial() {
        return inicial;
    }

    public void setInicial(char inicial) {
        this.inicial = inicial;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
