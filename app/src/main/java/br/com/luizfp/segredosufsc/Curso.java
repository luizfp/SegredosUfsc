package br.com.luizfp.segredosufsc;


import org.parceler.Parcel;

/**
 * Created by luiz on 2/23/16.
 */
@Parcel
public class Curso  {
    public Long id;
    public String nome;

    public Curso() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

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
