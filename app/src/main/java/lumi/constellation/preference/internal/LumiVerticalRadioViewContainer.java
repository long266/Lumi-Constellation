package lumi.constellation.preference.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.nhlong.lumi.constellation.R;

public class LumiVerticalRadioViewContainer extends LinearLayout {
    private boolean mIsDividerEnabled;

    public LumiVerticalRadioViewContainer(Context context) {
        super(context);
    }

    public LumiVerticalRadioViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LumiVerticalRadioViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mIsDividerEnabled) {
            Drawable drawable = AppCompatResources.getDrawable(getContext(), R.drawable.sesl_divider_vertical);
            int marginTop = Math.round(getContext().getResources().getDimension(R.dimen.widget_multi_btn_preference_divider_margin_top));
            int height = (getHeight() - marginTop) - Math.round(getContext().getResources().getDimension(R.dimen.widget_multi_btn_preference_divider_margin_bottom));
            int width = Math.round(getContext().getResources().getDimension(R.dimen.sesl_list_divider_height));

            for (int i = 0; i < getChildCount() - 1; i++) {
                drawable.setBounds(0, 0, width, height);
                canvas.save();
                canvas.translate((float) Math.round((((float) getWidth()) / ((float) getChildCount())) * ((float) i + 1)), (float) marginTop);
                drawable.draw(canvas);
                canvas.restore();
            }
        }
    }

    public void setDividerEnabled(boolean enabled) {
        mIsDividerEnabled = enabled;
    }
}