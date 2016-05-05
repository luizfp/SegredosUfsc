package br.com.luizfp.segredosufsc.segredo;

import lombok.Data;

/**
 * Created by luiz on 3/21/16.
 */
@Data
public class SegredoFavoritoUsuario {
    private Long idUsuario;
    private Long idSegredo;
    private boolean addToFavorite;
}
