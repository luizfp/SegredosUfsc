package br.com.luizfp.segredosufsc.segredo;

import org.parceler.Parcel;

import java.util.List;

import br.com.luizfp.segredosufsc.Usuario;
import br.com.luizfp.segredosufsc.comentario.ComentarioViewModel;
import lombok.Data;

/**
 * Classe que representa um segredo especificamente usada para a camada da view.
 * Útil para guardar o estado de algumas coisas do segredo, já que o recycler view
 * acaba reciclando os itens, o que pode acabar "mesclando" dados dos segredos. O que,
 * claro, nós não queremos que aconteça.
 *
 */
@Data
@Parcel
public class SegredoViewModel {
    private Long id;
    private String segredo;
    private String dataString;
    private Usuario usuario;
    private List<ComentarioViewModel> comentarioList;
    private long totalFavorites;
    private int backgroundColor;
    private boolean favorite;
}
