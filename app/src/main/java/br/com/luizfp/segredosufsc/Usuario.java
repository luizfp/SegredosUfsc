package br.com.luizfp.segredosufsc;

import org.parceler.Parcel;

import lombok.Data;

/**
 * Created by luiz on 2/23/16.
 */
@Data
@Parcel
public class Usuario {
    private Long id;
    private char inicial;
    private Curso curso;
}
