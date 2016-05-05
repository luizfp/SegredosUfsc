package br.com.luizfp.segredosufsc.util;

/**
 * Created by luiz on 3/12/16.
 */
public class AlfabetoHelper {

    public static int getIndexForLetter(String[] alfabeto, String letraAtual) {
        if (letraAtual != null) {
            for (int i = 0; i < alfabeto.length; i++) {
                if (alfabeto[i].equalsIgnoreCase(letraAtual)) {
                    return i;
                }
            }
        }
        return 0;
    }
}
