package br.com.luizfp.segredosufsc.util;

import android.content.Context;

import br.com.luizfp.segredosufsc.network.Credentials;

/**
 * Created by luiz on 3/15/16.
 */
public class TokenUtils {

    public static Credentials getCredentials(Context context) {
        Long idUsuario = Prefs.getLong(context, Prefs.ID_USER_KEY);
        String token = Prefs.getString(context, Prefs.TOKEN_KEY);
        Credentials credentials = new Credentials();
        credentials.setIdUser(idUsuario);
        credentials.setAccessToken(token);
        credentials.setTokenType(Credentials.TOKEN_TYPE.BEARER);
        return credentials;
    }
}
