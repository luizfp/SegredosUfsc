package br.com.luizfp.segredosufsc.event;

import lombok.Data;

/**
 * Created by luiz on 3/19/16.
 */
public class SecretRemovedFromFavoritesEvent {

    public static class NotifyDataSedChanged{}

    @Data
    public static class RemoveItemFromList{
        private Long idSegredo;
    }
}
