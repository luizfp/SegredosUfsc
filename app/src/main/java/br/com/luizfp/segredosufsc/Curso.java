package br.com.luizfp.segredosufsc;


import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by luiz on 2/23/16.
 */
@Getter
@Setter
@Parcel
public class Curso  {
    private Long id;
    private String nome;

    /**
     * Dado override no toString() para retornar apenas o nome, pois assim o spinner dos cursos
     * já mostrará apenas o nome deles. Assim pode-se passar um list de cursos diretamente para o
     * arrayAdapter e ele tratará de chamar o toString() para cada objeto do array.
     *
     * @return o nome do curso
     */
    @Override
    public String toString() {
        return nome;
    }
}
