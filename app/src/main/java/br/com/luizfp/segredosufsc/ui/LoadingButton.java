package br.com.luizfp.segredosufsc.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.luizfp.segredosufsc.R;

/**
 * From: https://github.com/snadjafi/LoadingButton
 */
public class LoadingButton extends RelativeLayout {

    //region Variables
    static final int DEFAULT_COLOR = android.R.color.white;
    public static final int IN_FROM_RIGHT = 0;
    public static final int IN_FROM_LEFT = 1;

    private int mDefaultTextSize;
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private String mLoadingMessage;
    private String mButtonText;
    private float mTextSize;
    private int mTextColor;
    private boolean mIsLoadingShowing;
    private Typeface mTypeface;
    //private Animation inRight;
    //private Animation inLeft;
    //private int mCurrentInDirection;
    //private boolean mTextSwitcherReady;
    //endregion

    //region Constructors
    public LoadingButton(Context context) {
        super(context);
        init(context, null);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    //endregion

    public float getTextSize() {
        return mTextSize;
    }

    public Typeface getTypeface() {
        return mTypeface;
    }

    public void setProgressColor(int colorRes) {
        mProgressBar.getIndeterminateDrawable()
                .mutate()
                .setColorFilter(colorRes, PorterDuff.Mode.SRC_ATOP);
    }

    public void setTypeface(@NonNull Typeface typeface) {
        this.mTypeface = typeface;
    }

//    public void setAnimationInDirection(int inDirection) {
//        mCurrentInDirection = inDirection;
//    }

    public void setText(String text) {
        if (text != null) {
            mButtonText = text;
            mTextView.setText(mButtonText);
            // if (mTextSwitcherReady) {
            //mTextView.setInAnimation(mCurrentInDirection == IN_FROM_LEFT ? inLeft : inRight);
            // }
        }
    }

    public void setLoadingText(String text) {
        if (text != null) {
            mLoadingMessage = text;
        }
    }

//    public void setTextFactory(@NonNull ViewSwitcherFactory factory) {
//        mTextView.removeAllViewsInLayout();
//        mTextView.setFactory(factory);
//        mTextView.setText(mButtonText);
//    }

    public void showLoading() {
        if (!mIsLoadingShowing) {
            mProgressBar.setVisibility(View.VISIBLE);
            mTextView.setText(mLoadingMessage);
            mIsLoadingShowing = true;
            setEnabled(false);
        }
    }

    public void stopLoading() {
        if (mIsLoadingShowing) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mTextView.setText(mButtonText);
            mIsLoadingShowing = false;
            setEnabled(true);
        }
    }

    public void showSuccess(String successMessage) {
        if (mIsLoadingShowing) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mTextView.setText(successMessage);
        }
    }


    public boolean isLoadingShowing() {
        return mIsLoadingShowing;
    }

    private void init(Context context, AttributeSet attrs) {
        mDefaultTextSize = getResources().getDimensionPixelSize(R.dimen.text_default_size);
        mIsLoadingShowing = false;
        LayoutInflater.from(getContext()).inflate(R.layout.partial_loading_button, this, true);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
        mTextView = (TextView) findViewById(R.id.pb_text);


        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.LoadingButton,
                    0, 0);
            try {
                float textSize = a.getDimensionPixelSize(R.styleable.LoadingButton_pbTextSize, mDefaultTextSize);
                setTextSize(textSize);

                String text = a.getString(R.styleable.LoadingButton_pbText);
                setText(text);

                mLoadingMessage = a.getString(R.styleable.LoadingButton_pbLoadingText);

                if (mLoadingMessage == null) {
                    mLoadingMessage = getContext().getString(R.string.default_loading);
                }
                int defaultColor = ContextCompat.getColor(context, DEFAULT_COLOR);
                int progressColor = a.getColor(R.styleable.LoadingButton_pbProgressColor, defaultColor);
                setProgressColor(progressColor);

                int textColor = a.getColor(R.styleable.LoadingButton_pbTextColor, defaultColor);
                setTextColor(textColor);

            } finally {
                a.recycle();
            }
        } else {
            int white = ContextCompat.getColor(context, DEFAULT_COLOR);
            mLoadingMessage = getContext().getString(R.string.default_loading);
            setProgressColor(white);
            setTextColor(white);
            setTextSize(mDefaultTextSize);
        }
        //setupTextSwitcher();
    }

    private void setTextSize(float size) {
        mTextView.setTextSize(size);
    }

    private void setTextColor(int textColor) {
        mTextView.setTextColor(textColor);
    }

//    private void setupTextSwitcher() {
//        ViewSwitcherFactory factory = new ViewSwitcherFactory(getContext(), mTextColor, mTextSize, mTypeface);
//        mTextView.setFactory(factory);
//
//        inRight = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
//        inLeft = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
//        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
//        mTextView.setOutAnimation(out);
//        mTextView.setInAnimation(inRight);
//
//        mTextView.setText(mButtonText);
//        mTextSwitcherReady = true;
//    }

//    public static class ViewSwitcherFactory implements ViewSwitcher.ViewFactory {
//
//        //region Variables
//        private final int textColor;
//        private final float textSize;
//        private final Typeface typeFace;
//        private final Context context;
//        //endregion
//
//        //region Constructor
//        public ViewSwitcherFactory(Context context, int textColor, float textSize, Typeface typeface) {
//            this.context = context;
//            this.textColor = textColor;
//            this.textSize = textSize;
//            this.typeFace = typeface;
//        }
//        //endregion
//
//        @Override
//        public View makeView() {
//            TextView tv = new TextView(context);
//            tv.setTextColor(textColor);
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//            tv.setGravity(Gravity.CENTER);
//            tv.setTypeface(typeFace);
//
//            return tv;
//        }
//    }
}