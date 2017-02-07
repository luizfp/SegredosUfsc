package br.com.luizfp.segredosufsc.event;

/**
 * Created by luiz on 3/19/16.
 */
public class SecretRemovedFromFavoritesEvent {

    public static class NotifyDataSedChanged{}

    public static class RemoveItemFromList {
        private Long idSegredo;

        public Long getIdSegredo() {
            return idSegredo;
        }

        public void setIdSegredo(Long idSegredo) {
            this.idSegredo = idSegredo;
        }
    }
}
