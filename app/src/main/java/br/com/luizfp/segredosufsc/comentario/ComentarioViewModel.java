package br.com.luizfp.segredosufsc.comentario;

import org.parceler.Parcel;

/**
 * Classe que representa um comentário especificamente usada para a camada da view.
 * Útil para guardar o estado da cor de fundo (onde fica a inicial do usuário) de um comentario.
 * Já que o recycler view acaba reciclando os itens, o que pode ocasionar em trocas de cores ao
 * dar o scroll.
 *
 */
@Parcel
public class ComentarioViewModel {
    private String comentario;
    private String nomeCursoUsuario;
    private String dataString;
    private char inicialNomeUsuario;
    private int backgroundColorInicialUsuario;

    public ComentarioViewModel() {
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getNomeCursoUsuario() {
        return nomeCursoUsuario;
    }

    public void setNomeCursoUsuario(String nomeCursoUsuario) {
        this.nomeCursoUsuario = nomeCursoUsuario;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public char getInicialNomeUsuario() {
        return inicialNomeUsuario;
    }

    public void setInicialNomeUsuario(char inicialNomeUsuario) {
        this.inicialNomeUsuario = inicialNomeUsuario;
    }

    public int getBackgroundColorInicialUsuario() {
        return backgroundColorInicialUsuario;
    }

    public void setBackgroundColorInicialUsuario(int backgroundColorInicialUsuario) {
        this.backgroundColorInicialUsuario = backgroundColorInicialUsuario;
    }
}
