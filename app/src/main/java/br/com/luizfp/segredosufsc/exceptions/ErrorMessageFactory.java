package br.com.luizfp.segredosufsc.exceptions;

import android.content.Context;

import br.com.luizfp.segredosufsc.R;


/**
 * Factory usada para criar uma mensagem de erro dada uma exceção e uma condição
 */
public class ErrorMessageFactory {
    private static final int UNAUTHORIZED = 401;

    private ErrorMessageFactory() {
        //empty
    }

    /**
     * Creates a String representing an error message.
     *
     * @param context Context needed to retrieve string resources.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return {@link String} an error message.
     */
    public static String create(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_generic);

        if (exception instanceof NoNetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_internet_connection);
        } else if (exception instanceof MalformedEmailException) {
            message = context.getString(R.string.exception_message_malformed_email);
        } else if (exception instanceof NotAnUfscEmailException) {
            message = context.getString(R.string.exception_message_not_an_usfc_email);
        } else if (exception instanceof DefaultMessageException) {
            message = exception.getMessage() != null ? exception.getMessage() : "Erro";
        }
//        } else if (exception instanceof HttpException) {
//            if (((HttpException) exception).code() == UNAUTHORIZED) {
//                message = context.getString(R.string.exception_message_unauthorized);
//            }
//        }

        return message;
    }
}
