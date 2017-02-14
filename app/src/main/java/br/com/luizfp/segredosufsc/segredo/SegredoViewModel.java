package br.com.luizfp.segredosufsc.segredo;

import org.parceler.Parcel;

import java.util.List;

import br.com.luizfp.segredosufsc.Usuario;
import br.com.luizfp.segredosufsc.comentario.ComentarioViewModel;

/**
 * Classe que representa um segredo especificamente usada para a camada da view.
 * Útil para guardar o estado de algumas coisas do segredo, já que o recycler view
 * acaba reciclando os itens, o que pode acabar "mesclando" dados dos segredos. O que,
 * claro, nós não queremos que aconteça.
 *
 */
@Parcel
public class SegredoViewModel {
    public Long id;
    public String segredo;
    public String dataString;
    public Usuario usuario;
    public List<ComentarioViewModel> comentarioList;
    public long totalFavorites;
    public int backgroundColor;
    public boolean favorite;

    public SegredoViewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSegredo() {
        return segredo;
    }

    public void setSegredo(String segredo) {
        this.segredo = segredo;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ComentarioViewModel> getComentarioList() {
        return comentarioList;
    }

    public void setComentarioList(List<ComentarioViewModel> comentarioList) {
        this.comentarioList = comentarioList;
    }

    public long getTotalFavorites() {
        return totalFavorites;
    }

    public void setTotalFavorites(long totalFavorites) {
        this.totalFavorites = totalFavorites;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
