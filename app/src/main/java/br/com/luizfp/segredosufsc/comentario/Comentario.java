package br.com.luizfp.segredosufsc.comentario;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by luiz on 2/23/16.
 */
@Parcel
public class Comentario {
    public Long id;
    public Date data;
    public String comentario;
    public br.com.luizfp.segredosufsc.Usuario Usuario;
    public Long idSegredo;

    public Comentario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public br.com.luizfp.segredosufsc.Usuario getUsuario() {
        return Usuario;
    }

    public void setUsuario(br.com.luizfp.segredosufsc.Usuario usuario) {
        Usuario = usuario;
    }

    public Long getIdSegredo() {
        return idSegredo;
    }

    public void setIdSegredo(Long idSegredo) {
        this.idSegredo = idSegredo;
    }
}
