package br.com.luizfp.segredosufsc.segredo;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

import br.com.luizfp.segredosufsc.comentario.Comentario;
import br.com.luizfp.segredosufsc.Usuario;
import lombok.Data;

/**
 * Created by luiz on 2/23/16.
 */
@Data
@Parcel
public class Segredo {
    private Long id;
    private String segredo;
    private Date data;
    private Usuario usuario;
    private List<Comentario> comentarioList;
    private long totalFavorites;
    private boolean favorite;
}
