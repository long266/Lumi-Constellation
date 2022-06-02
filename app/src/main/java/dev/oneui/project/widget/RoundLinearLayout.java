package dev.oneuiproject.oneui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.util.SeslRoundedCorner;

import com.nhlong.lumi.constellation.R;

/**
 * LinearLayout with rounded corners.
 */
public class RoundLinearLayout extends LinearLayout {
    private Context mContext;
    private SeslRoundedCorner mRoundedCorner;

    public RoundLinearLayout(@NonNull Context context) {
        this(context, null);
    }

    public RoundLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs,
                            int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RoundLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs,
                            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mContext = context;

        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.RoundLinearLayout);
        final int roundedCorners = a.getInt(R.styleable.RoundLinearLayout_roundedCorners,
                SeslRoundedCorner.ROUNDED_CORNER_ALL);
        a.recycle();

        mRoundedCorner = new SeslRoundedCorner(mContext);
        mRoundedCorner.setRoundedCorners(roundedCorners);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mRoundedCorner.drawRoundedCorner(canvas);
    }

    public void setRoundedCorners(int roundedCorners) {
        mRoundedCorner.setRoundedCorners(roundedCorners);
        invalidate();
    }

    public void setRoundedCornerColor(int roundedCorners, @ColorInt int color) {
        mRoundedCorner.setRoundedCornerColor(roundedCorners, color);
        invalidate();
    }
}