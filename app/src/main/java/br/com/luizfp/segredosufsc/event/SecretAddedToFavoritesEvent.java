package br.com.luizfp.segredosufsc.event;

import br.com.luizfp.segredosufsc.segredo.SegredoViewModel;

/**
 * Created by luiz on 3/19/16.
 */
public class SecretAddedToFavoritesEvent {
    private SegredoViewModel segredo;

    public SegredoViewModel getSegredo() {
        return segredo;
    }

    public void setSegredo(SegredoViewModel segredo) {
        this.segredo = segredo;
    }
}