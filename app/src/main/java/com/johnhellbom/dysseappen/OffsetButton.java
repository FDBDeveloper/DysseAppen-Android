package com.johnhellbom.dysseappen;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.Button;
import android.content.res.Resources;

/**
 * Created by John Hellbom on 2016-02-28.
 */
public class OffsetButton extends Button {

    private static final int OFFSET_IN_DP = 1;
    private int offset_in_px;
    private boolean wasPressed = false;
    private Integer[] defaultPaddings;

    public OffsetButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public OffsetButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public OffsetButton(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        offset_in_px = (int) convertDpToPixel(OFFSET_IN_DP, getContext());
    }

    @Override
    public void setPressed(boolean pressed) {
        if (pressed && !wasPressed) {
            changePaddings();
        }
        if (!pressed && wasPressed) {
            resetPaddings();
        }
        super.setPressed(pressed);
    }

    private void changePaddings() {
        defaultPaddings = new Integer[]{getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom()};
        setPadding(getPaddingLeft(), getPaddingTop() + offset_in_px, getPaddingRight(), getPaddingBottom() - offset_in_px);
        wasPressed = true;
    }

    private void resetPaddings() {
        setPadding(defaultPaddings[0], defaultPaddings[1], defaultPaddings[2], defaultPaddings[3]);
        wasPressed = false;
    }

    @Override
    public boolean performClick() {
        resetPaddings();
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled())
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !wasPressed) {
                changePaddings();
            } else if (event.getAction() == MotionEvent.ACTION_UP && wasPressed) {
                resetPaddings();
            }
        }
        return super.onTouchEvent(event);
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
