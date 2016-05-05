package br.com.luizfp.segredosufsc.util;

import android.animation.ObjectAnimator;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import br.com.luizfp.segredosufsc.BuildConfig;

/**
 * Created by luiz on 2/26/16.
 */
public class AnimUtils {
    private static final String TAG = AnimUtils.class.getSimpleName();

    // TODO: Não funciona, getLineCount() não funciona com ellipsize end
    public static void cycleTextViewExpansion(TextView textView, int collapsedMaxLines){
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getMaxLines(): " + TextViewCompat.getMaxLines(textView));
            Log.d(TAG, "getLineCount(): " + textView.getLineCount());
        }
        if (TextViewCompat.getMaxLines(textView) == collapsedMaxLines) {
            // Expand
            ObjectAnimator animation = ObjectAnimator.ofInt(textView, "maxLines", textView.getLineCount());
            animation.setDuration(getConsistentDuration(textView.getLineCount(), collapsedMaxLines)).start();
        } else {
            // Collapse
            // Após colapsar o texto pode setar o ellipsize novamente
            ObjectAnimator animation = ObjectAnimator.ofInt(textView, "maxLines", collapsedMaxLines);
            animation.setDuration(getConsistentDuration(textView.getLineCount(), collapsedMaxLines)).start();
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    private static int getConsistentDuration(int lineCount, int collapsedMaxLines) {
        int duration = (lineCount - collapsedMaxLines) * 10;
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "lineCount: " + lineCount);
            Log.d(TAG, "collapsedMaxLines: " + collapsedMaxLines);
            Log.d(TAG, "duration: " + (duration <= 0 ? 400 : duration));
        }
        return  duration <= 0 ? 400 : duration;
    }
}
