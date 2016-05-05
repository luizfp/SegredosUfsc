package br.com.luizfp.segredosufsc.util;

import java.util.regex.Pattern;

/**
 * Created by luiz on 3/7/16.
 */
public class PatternUtils {

    private static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+");

    public static boolean isThisEmailValid(String email) {
        return EMAIL_ADDRESS.matcher(email).matches();
    }
}
