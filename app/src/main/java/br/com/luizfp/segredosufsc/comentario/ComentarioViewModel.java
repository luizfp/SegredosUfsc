package br.com.luizfp.segredosufsc.comentario;

import org.parceler.Parcel;

import lombok.Data;

/**
 * Classe que representa um comentário especificamente usada para a camada da view.
 * Útil para guardar o estado da cor de fundo (onde fica a inicial do usuário) de um comentario.
 * Já que o recycler view acaba reciclando os itens, o que pode ocasionar em trocas de cores ao
 * dar o scroll.
 *
 */
@Data
@Parcel
public class ComentarioViewModel {
    private String comentario;
    private String nomeCursoUsuario;
    private String dataString;
    private char inicialNomeUsuario;
    private int backgroundColorInicialUsuario;
}
