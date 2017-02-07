package br.com.luizfp.segredosufsc.segredo;

/**
 * Created by luiz on 3/21/16.
 */
public class SegredoFavoritoUsuario {
    private Long idUsuario;
    private Long idSegredo;
    private boolean addToFavorite;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdSegredo() {
        return idSegredo;
    }

    public void setIdSegredo(Long idSegredo) {
        this.idSegredo = idSegredo;
    }

    public boolean isAddToFavorite() {
        return addToFavorite;
    }

    public void setAddToFavorite(boolean addToFavorite) {
        this.addToFavorite = addToFavorite;
    }
}
