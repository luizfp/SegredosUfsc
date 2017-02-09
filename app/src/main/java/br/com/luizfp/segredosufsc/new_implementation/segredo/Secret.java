package br.com.luizfp.segredosufsc.new_implementation.segredo;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

import br.com.luizfp.segredosufsc.Usuario;
import br.com.luizfp.segredosufsc.comentario.Comentario;

/**
 * Created by luiz on 2/23/16.
 */
@Parcel
public class Secret {
    public Long id;
    public String segredo;
    public Date data;
    public Usuario usuario;
    public List<Comentario> comentarioList;
    public long totalFavorites;
    public boolean favorite;

    public Secret() {
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Comentario> getComentarioList() {
        return comentarioList;
    }

    public void setComentarioList(List<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
    }

    public long getTotalFavorites() {
        return totalFavorites;
    }

    public void setTotalFavorites(long totalFavorites) {
        this.totalFavorites = totalFavorites;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
