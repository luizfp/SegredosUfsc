package br.com.luizfp.segredosufsc.new_implementation;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.luizfp.segredosufsc.R;


/**
 * Created by luiz on 8/6/16.
 */
public class ErrorView extends LinearLayout implements View.OnClickListener {
    public static final int DEFAULT_IMAGE = R.drawable.error_view_default_image;
    public static final int DEFAULT_MESSAGE = R.string.error_view_default_message;
    public static final int DEFAULT_BUTTON_TEXT = R.string.error_view_default_button_text;
    private ImageView mImgError;
    private Button mBtnRetry;
    private TextView mTxtErrorMessage;
    private OnButtonRetryClickListener mListener;

    public interface OnButtonRetryClickListener {
        void onClickButtonRetry();
    }

    public ErrorView(Context context) {
        super(context);
        init(context, null);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        View view = inflate(context, R.layout.error_view, this);
        mImgError = (ImageView) view.findViewById(R.id.img_error);
        mBtnRetry = (Button) view.findViewById(R.id.btn_retry);
        mTxtErrorMessage = (TextView) view.findViewById(R.id.txt_errorMessage);
        mBtnRetry.setOnClickListener(this);

        if (attrs == null)
            return;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ErrorView);

        Drawable drawable = a.getDrawable(R.styleable.ErrorView_errorImage);
        if (drawable != null)
            mImgError.setImageDrawable(drawable);
        else
            mImgError.setImageResource(DEFAULT_IMAGE);

        String buttonText = a.getString(R.styleable.ErrorView_errorButtonText);
        if (buttonText != null)
            mBtnRetry.setText(buttonText);
        else
            mBtnRetry.setText(DEFAULT_BUTTON_TEXT);

        String errorMessage = a.getString(R.styleable.ErrorView_errorMessage);
        if (errorMessage != null)
            mTxtErrorMessage.setText(errorMessage);
        else
            mTxtErrorMessage.setText(DEFAULT_MESSAGE);

        a.recycle();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.mImgErrorId = mImgError.getTag() != null ? (int) mImgError.getTag() : -1;
        savedState.mErrorMessage = mTxtErrorMessage.getText().toString();
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setErrorImage(savedState.mImgErrorId);
        setErrorMessage(savedState.mErrorMessage);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null)
            mListener.onClickButtonRetry();
    }

    public void setErrorImage(@DrawableRes int res) {
        mImgError.setTag(res != -1 ? res : null);
        mImgError.setImageResource(res != -1 ? res : DEFAULT_IMAGE);
    }

    public void setErrorMessage(String errorMessage) {
        mTxtErrorMessage.setText(errorMessage);
    }

    public void setErrorMessage(@StringRes int res) {
        mTxtErrorMessage.setText(res);
    }

    public void setButtonText(String buttonText) {
        mBtnRetry.setText(buttonText);
    }

    public void setButtonText(@StringRes int res) {
        mBtnRetry.setText(res);
    }

    public void setOnButtonRetryClickListener(@Nullable OnButtonRetryClickListener listener) {
        mListener = listener;
    }

    private static class SavedState extends BaseSavedState {
        int mImgErrorId;
        String mErrorMessage;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(android.os.Parcel in) {
            super(in);
            this.mImgErrorId = in.readInt();
            this.mErrorMessage = in.readString();
        }

        @Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.mImgErrorId);
            out.writeString(this.mErrorMessage);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(android.os.Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}