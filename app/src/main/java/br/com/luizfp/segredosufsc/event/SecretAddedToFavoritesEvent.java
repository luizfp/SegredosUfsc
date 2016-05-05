package br.com.luizfp.segredosufsc.event;

import br.com.luizfp.segredosufsc.segredo.SegredoViewModel;
import lombok.Data;

/**
 * Created by luiz on 3/19/16.
 */
@Data
public class SecretAddedToFavoritesEvent {
    private SegredoViewModel segredo;
}