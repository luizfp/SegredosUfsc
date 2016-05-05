package br.com.luizfp.segredosufsc.comentario;

import org.parceler.Parcel;

import java.util.Date;

import lombok.Data;

/**
 * Created by luiz on 2/23/16.
 */
@Data
@Parcel
public class Comentario {
    private Long id;
    private Date data;
    private String comentario;
    private br.com.luizfp.segredosufsc.Usuario Usuario;
    private Long idSegredo;
}
