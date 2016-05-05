package br.com.luizfp.segredosufsc.util;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by luiz on 1/3/16.
 */
public class IoUtils {
    private static final String TAG = IoUtils.class.getSimpleName();

    public interface Callback {
        void onFileSaved(File file);
    }

    /**
     * Salva a figura em aruqivo
     *
     * @param file
     * @param bitmap
     */
    public static void writeBitmap(@NonNull File file,@NonNull Bitmap bitmap, Callback callback) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            // true to append false to override
            FileOutputStream out = new FileOutputStream(file, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            if (callback != null) {
                callback.onFileSaved(file);
            }
        } catch (IOException e) {
            Log.e(TAG, "Erro ao salvar bitmap em arquivo", e);
        }
    }

    public static void writeBitmap(@NonNull File file,@NonNull Bitmap bitmap) {
        writeBitmap(file, bitmap, null);
    }

    /**
     * Converte a InputStream para bytes[]
     *
     * @param in
     * @return
     */
    public static byte[] toBytes(InputStream in) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar byte array a partir de InputStream", e);
            return null;
        } finally {
            try {
                bos.close();
                in.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException ao criar byte array a partir de InputStream", e);
            }
        }
    }

    /**
     * Salva o texto em arquivo
     *
     * @param file
     * @param string
     */
    public static void writeString(File file, String string) {
        writeBytes(file, string.getBytes());
    }

    public static void writeBytes(File file, byte[] bytes) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes);
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.e(TAG, "IOException ao escrever bytes para arquivo", e);
        }
    }

    public static String readString(File file) {
        try {
            if (file == null || !file.exists()) {
                return null;
            }
            InputStream in = new FileInputStream(file);
            String s = toString(in, "UTF-8");
            return s;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            Log.e(TAG, "IOException ao ler string para arquivo", e);
            return null;
        }
    }

    /**
     * Converte a InputStream para String utilizando o charset informado
     *
     * @param in
     * @param charset UTF-8 ou ISO-8859-1
     * @return
     * @throws IOException
     */
    public static String toString(InputStream in, String charset) throws IOException {
        byte[] bytes = toBytes(in);
        String texto = new String(bytes, charset);
        return texto;
    }
}
