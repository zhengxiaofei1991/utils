package util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import pl.droidsonroids.gif.GifImageButton;

/**
 * @desc: 类作用
 * @author: zhengxf
 * @created: 2019/4/17.
 */

public class FloatingButton extends GifImageButton {
    private static final int TRANSLATE_DURATION_MILLIS = 200;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_NORMAL, TYPE_MINI})
    public @interface TYPE {
    }

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_MINI = 1;

    private boolean mVisible;

    private int mColorNormal;
    private int mColorPressed;
    private int mColorRipple;
    private int mColorDisabled;
    private boolean mShadow;
    private int mType;

    private int mShadowSize;

    private int mScrollThreshold;

    private boolean mMarginsSet;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    public FloatingButton(Context context) {
        super(context);
    }

    public FloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FloatingButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = getDimension(
                mType == TYPE_NORMAL ? com.melnykov.fab.R.dimen.fab_size_normal : com.melnykov.fab.R.dimen.fab_size_mini);
        if (mShadow && !hasLollipopApi()) {
            size += mShadowSize * 2;
            setMarginsWithoutShadow();
        }
        setMeasuredDimension(size, size);
    }

    private void init(Context context, AttributeSet attributeSet) {
        mVisible = true;
        mColorNormal = getColor(com.melnykov.fab.R.color.material_blue_500);
        mColorPressed = darkenColor(mColorNormal);
        mColorRipple = lightenColor(mColorNormal);
        mColorDisabled = getColor(android.R.color.darker_gray);
        mType = TYPE_NORMAL;
        mShadow = true;
        mScrollThreshold = getResources().getDimensionPixelOffset(com.melnykov.fab.R.dimen.fab_scroll_threshold);
        mShadowSize = getDimension(com.melnykov.fab.R.dimen.fab_shadow_size);
        if (attributeSet != null) {
            initAttributes(context, attributeSet);
        }
        updateBackground();
    }

    private int getDimension(@DimenRes int id) {
        return getResources().getDimensionPixelSize(id);
    }

    private boolean hasLollipopApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private boolean hasJellyBeanApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    private boolean hasHoneycombApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private void setMarginsWithoutShadow() {
        if (!mMarginsSet) {
            if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                int leftMargin = layoutParams.leftMargin - mShadowSize;
                int topMargin = layoutParams.topMargin - mShadowSize;
                int rightMargin = layoutParams.rightMargin - mShadowSize;
                int bottomMargin = layoutParams.bottomMargin - mShadowSize;
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

                requestLayout();
                mMarginsSet = true;
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void setBackgroundCompat(Drawable drawable) {
        if (hasLollipopApi()) {
            float elevation;
            if (mShadow) {
                elevation = getElevation() > 0.0f ? getElevation()
                        : getDimension(com.melnykov.fab.R.dimen.fab_elevation_lollipop);
            } else {
                elevation = 0.0f;
            }
            setElevation(elevation);
            RippleDrawable rippleDrawable = new RippleDrawable(new ColorStateList(new int[][]{{}},
                    new int[]{mColorRipple}), drawable, null);
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int size = getDimension(mType == TYPE_NORMAL ? com.melnykov.fab.R.dimen.fab_size_normal
                            : com.melnykov.fab.R.dimen.fab_size_mini);
                    outline.setOval(0, 0, size, size);
                }
            });
            setClipToOutline(true);
            setBackground(rippleDrawable);
        } else if (hasJellyBeanApi()) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray attr = getTypedArray(context, attributeSet, com.melnykov.fab.R.styleable.FloatingActionButton);
        if (attr != null) {
            try {
                mColorNormal = attr.getColor(com.melnykov.fab.R.styleable.FloatingActionButton_fab_colorNormal,
                        getColor(com.melnykov.fab.R.color.material_blue_500));
                mColorPressed = attr.getColor(com.melnykov.fab.R.styleable.FloatingActionButton_fab_colorPressed,
                        darkenColor(mColorNormal));
                mColorRipple = attr.getColor(com.melnykov.fab.R.styleable.FloatingActionButton_fab_colorRipple,
                        lightenColor(mColorNormal));
                mColorDisabled = attr.getColor(com.melnykov.fab.R.styleable.FloatingActionButton_fab_colorDisabled,
                        mColorDisabled);
                mShadow = attr.getBoolean(com.melnykov.fab.R.styleable.FloatingActionButton_fab_shadow, true);
                mType = attr.getInt(com.melnykov.fab.R.styleable.FloatingActionButton_fab_type, TYPE_NORMAL);
            } finally {
                attr.recycle();
            }
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private int getMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

    public void setShadow(boolean shadow) {
        if (shadow != mShadow) {
            mShadow = shadow;
            updateBackground();
        }
    }

    public boolean hasShadow() {
        return mShadow;
    }

    public void setType(@FloatingActionButton.TYPE int type) {
        if (type != mType) {
            mType = type;
            updateBackground();
        }
    }

    @FloatingActionButton.TYPE
    public int getType() {
        return mType;
    }

    public boolean isVisible() {
        return mVisible;
    }

    private void updateBackground() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, createDrawable(mColorPressed));
        drawable.addState(new int[]{-android.R.attr.state_enabled}, createDrawable(mColorDisabled));
        drawable.addState(new int[]{}, createDrawable(mColorNormal));
        setBackgroundCompat(drawable);
    }

    private Drawable createDrawable(int color) {
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(color);

        if (mShadow && !hasLollipopApi()) {
            Drawable shadowDrawable = getResources().getDrawable(mType == TYPE_NORMAL ? com.melnykov.fab.R.drawable.fab_shadow
                    : com.melnykov.fab.R.drawable.fab_shadow_mini);
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shadowDrawable, shapeDrawable});
            layerDrawable.setLayerInset(1, mShadowSize, mShadowSize, mShadowSize, mShadowSize);
            return layerDrawable;
        } else {
            return shapeDrawable;
        }
    }

    public void show() {
        show(true);
    }

    public void hide() {
        hide(true);
    }

    public void show(boolean animate) {
        toggle(true, animate, false);
    }

    public void hide(boolean animate) {
        toggle(false, animate, false);
    }

    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height + getMarginBottom();
            if (animate) {
                ViewPropertyAnimator.animate(this).setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            } else {
                ViewHelper.setTranslationY(this, translationY);
            }

            // On pre-Honeycomb a translated view is still clickable, so we need to disable clicks manually
            if (!hasHoneycombApi()) {
                setClickable(visible);
            }
        }
    }

    private int getColor(@ColorRes int id) {
        return getResources().getColor(id);
    }

    private static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f;
        return Color.HSVToColor(hsv);
    }

    private static int lightenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1.1f;
        return Color.HSVToColor(hsv);
    }

}
