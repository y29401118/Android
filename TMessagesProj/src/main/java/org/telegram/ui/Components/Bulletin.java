package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.DialogsActivity;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public final class Bulletin {

    public static final int DURATION_SHORT = 1500;
    public static final int DURATION_LONG = 2750;

    public static Bulletin make(@NonNull FrameLayout containerLayout, @NonNull Layout contentLayout, int duration) {
        return new Bulletin(containerLayout, contentLayout, duration);
    }

    @SuppressLint("RtlHardcoded")
    public static Bulletin make(@NonNull BaseFragment fragment, @NonNull Layout contentLayout, int duration) {
        if (fragment instanceof ChatActivity) {
            contentLayout.setWideScreenParams(ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
        } else if (fragment instanceof DialogsActivity) {
            contentLayout.setWideScreenParams(ViewGroup.LayoutParams.MATCH_PARENT, Gravity.NO_GRAVITY);
        }
        return new Bulletin(fragment.getLayoutContainer(), contentLayout, duration);
    }

    public static Bulletin find(@NonNull FrameLayout containerLayout) {
        for (int i = 0, size = containerLayout.getChildCount(); i < size; i++) {
            final View view = containerLayout.getChildAt(i);
            if (view instanceof Layout) {
                return ((Layout) view).bulletin;
            }
        }
        return null;
    }

    public static void hide(@NonNull FrameLayout containerLayout) {
        hide(containerLayout, true);
    }

    public static void hide(@NonNull FrameLayout containerLayout, boolean animated) {
        final Bulletin bulletin = find(containerLayout);
        if (bulletin != null) {
            bulletin.hide(animated && isTransitionsEnabled());
        }
    }

    private static final HashMap<FrameLayout, Delegate> delegates = new HashMap<>();

    @SuppressLint("StaticFieldLeak")
    private static Bulletin visibleBulletin;

    private final Layout layout;
    private final ParentLayout parentLayout;
    private final FrameLayout containerLayout;
    private final Runnable hideRunnable = this::hide;
    private final int duration;

    private boolean showing;
    private boolean canHide;
    private int currentBottomOffset;
    private Delegate currentDelegate;
    private Layout.Transition layoutTransition;

    private Bulletin(@NonNull FrameLayout containerLayout, @NonNull Layout layout, int duration) {
        this.layout = layout;
        this.parentLayout = new ParentLayout(layout) {
            @Override
            protected void onPressedStateChanged(boolean pressed) {
                setCanHide(!pressed);
                if (containerLayout.getParent() != null) {
                    containerLayout.getParent().requestDisallowInterceptTouchEvent(pressed);
                }
            }

            @Override
            protected void onHide() {
                hide();
            }
        };
        this.containerLayout = containerLayout;
        this.duration = duration;
    }

    public Bulletin show() {
        if (!showing) {
            showing = true;

            if (layout.getParent() != parentLayout) {
                throw new IllegalStateException("Layout has incorrect parent");
            }

            if (visibleBulletin != null) {
                visibleBulletin.hide();
            }
            visibleBulletin = this;
            layout.onAttach(this);

            layout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    layout.removeOnLayoutChangeListener(this);
                    if (showing) {
                        layout.onShow();
                        currentDelegate = delegates.get(containerLayout);
                        currentBottomOffset = currentDelegate != null ? currentDelegate.getBottomOffset() : 0;
                        if (currentDelegate != null) {
                            currentDelegate.onShow(Bulletin.this);
                        }
                        if (isTransitionsEnabled()) {
                            if (currentBottomOffset != 0) {
                                ViewCompat.setClipBounds(parentLayout, new Rect(left, top - currentBottomOffset, right, bottom - currentBottomOffset));
                            } else {
                                ViewCompat.setClipBounds(parentLayout, null);
                            }
                            ensureLayoutTransitionCreated();
                            layoutTransition.animateEnter(layout, layout::onEnterTransitionStart, () -> {
                                ViewCompat.setClipBounds(parentLayout, null);
                                layout.onEnterTransitionEnd();
                                setCanHide(true);
                            }, offset -> {
                                if (currentDelegate != null) {
                                    currentDelegate.onOffsetChange(layout.getHeight() - offset);
                                }
                            }, currentBottomOffset);
                        } else {
                            if (currentDelegate != null) {
                                currentDelegate.onOffsetChange(layout.getHeight() - currentBottomOffset);
                            }
                            layout.setTranslationY(-currentBottomOffset);
                            layout.onEnterTransitionStart();
                            layout.onEnterTransitionEnd();
                            setCanHide(true);
                        }
                    }
                }
            });

            layout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                }
                @Override
                public void onViewDetachedFromWindow(View v) {
                    layout.removeOnAttachStateChangeListener(this);
                    hide(false);
                }
            });

            containerLayout.addView(parentLayout);
        }
        return this;
    }

    private void setCanHide(boolean canHide) {
        if (this.canHide != canHide) {
            this.canHide = canHide;
            if (canHide) {
                layout.postDelayed(hideRunnable, duration);
            } else {
                layout.removeCallbacks(hideRunnable);
            }
        }
    }

    private void ensureLayoutTransitionCreated() {
        if (layoutTransition == null) {
            layoutTransition = layout.createTransition();
        }
    }

    public void hide() {
        hide(isTransitionsEnabled());
    }

    public void hide(boolean animated) {
        if (showing) {
            showing = false;

            if (visibleBulletin == this) {
                visibleBulletin = null;
            }

            int bottomOffset = currentBottomOffset;
            currentBottomOffset = 0;

            if (ViewCompat.isLaidOut(layout)) {
                layout.removeCallbacks(hideRunnable);
                if (animated) {
                    if (bottomOffset != 0) {
                        ViewCompat.setClipBounds(parentLayout, new Rect(layout.getLeft(), layout.getTop() - bottomOffset, layout.getRight(), layout.getBottom() - bottomOffset));
                    } else {
                        ViewCompat.setClipBounds(parentLayout, null);
                    }
                    ensureLayoutTransitionCreated();
                    layoutTransition.animateExit(layout, layout::onExitTransitionStart, () -> {
                        if (currentDelegate != null) {
                            currentDelegate.onOffsetChange(0);
                            currentDelegate.onHide(this);
                        }
                        ViewCompat.setClipBounds(parentLayout, null);
                        layout.onExitTransitionEnd();
                        layout.onHide();
                        containerLayout.removeView(parentLayout);
                        layout.onDetach();
                    }, offset -> {
                        if (currentDelegate != null) {
                            currentDelegate.onOffsetChange(layout.getHeight() - offset);
                        }
                    }, bottomOffset);
                    return;
                }
            }

            if (currentDelegate != null) {
                currentDelegate.onOffsetChange(0);
                currentDelegate.onHide(this);
            }
            layout.onExitTransitionStart();
            layout.onExitTransitionEnd();
            layout.onHide();
            if (containerLayout != null) {
                containerLayout.removeView(parentLayout);
            }
            layout.onDetach();
        }
    }

    public boolean isShowing() {
        return showing;
    }

    public Layout getLayout() {
        return layout;
    }

    private static boolean isTransitionsEnabled() {
        return MessagesController.getGlobalMainSettings().getBoolean("view_animations", true) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    @Retention(SOURCE)
    @IntDef(value = {ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT})
    private @interface WidthDef {
    }

    @Retention(SOURCE)
    @SuppressLint("RtlHardcoded")
    @IntDef(value = {Gravity.LEFT, Gravity.RIGHT, Gravity.CENTER_HORIZONTAL, Gravity.NO_GRAVITY})
    private @interface GravityDef {
    }

    private static abstract class ParentLayout extends FrameLayout {

        private final Layout layout;
        private final Rect rect = new Rect();
        private final GestureDetector gestureDetector;

        private boolean pressed;
        private float translationX;
        private boolean hideAnimationRunning;
        private boolean needLeftAlphaAnimation;
        private boolean needRightAlphaAnimation;

        public ParentLayout(Layout layout) {
            super(layout.getContext());
            this.layout = layout;
            gestureDetector = new GestureDetector(layout.getContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                    if (!hideAnimationRunning) {
                        needLeftAlphaAnimation = layout.isNeedSwipeAlphaAnimation(true);
                        needRightAlphaAnimation = layout.isNeedSwipeAlphaAnimation(false);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    layout.setTranslationX(translationX -= distanceX);
                    if (translationX == 0 || (translationX < 0f && needLeftAlphaAnimation) || (translationX > 0f && needRightAlphaAnimation)) {
                        layout.setAlpha(1f - Math.abs(translationX) / layout.getWidth());
                    }
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (Math.abs(velocityX) > 2000f) {
                        final boolean needAlphaAnimation = (velocityX < 0f && needLeftAlphaAnimation) || (velocityX > 0f && needRightAlphaAnimation);

                        final SpringAnimation springAnimation = new SpringAnimation(layout, DynamicAnimation.TRANSLATION_X, Math.signum(velocityX) * layout.getWidth() * 2f);
                        if (!needAlphaAnimation) {
                            springAnimation.addEndListener((animation, canceled, value, velocity) -> onHide());
                            springAnimation.addUpdateListener(((animation, value, velocity) -> {
                                if (Math.abs(value) > layout.getWidth()) {
                                    animation.cancel();
                                }
                            }));
                        }
                        springAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
                        springAnimation.getSpring().setStiffness(100f);
                        springAnimation.setStartVelocity(velocityX);
                        springAnimation.start();

                        if (needAlphaAnimation) {
                            final SpringAnimation springAnimation2 = new SpringAnimation(layout, DynamicAnimation.ALPHA, 0f);
                            springAnimation2.addEndListener((animation, canceled, value, velocity) -> onHide());
                            springAnimation2.addUpdateListener(((animation, value, velocity) -> {
                                if (value <= 0f) {
                                    animation.cancel();
                                }
                            }));
                            springAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
                            springAnimation.getSpring().setStiffness(10f);
                            springAnimation.setStartVelocity(velocityX);
                            springAnimation2.start();
                        }

                        hideAnimationRunning = true;
                        return true;
                    }
                    return false;
                }
            });
            gestureDetector.setIsLongpressEnabled(false);
            addView(layout);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (pressed || inLayoutHitRect(event.getX(), event.getY())) {
                gestureDetector.onTouchEvent(event);
                final int actionMasked = event.getActionMasked();
                if (actionMasked == MotionEvent.ACTION_DOWN) {
                    if (!pressed && !hideAnimationRunning) {
                        layout.animate().cancel();
                        translationX = layout.getTranslationX();
                        onPressedStateChanged(pressed = true);
                    }
                } else if (actionMasked == MotionEvent.ACTION_UP || actionMasked == MotionEvent.ACTION_CANCEL) {
                    if (pressed) {
                        if (!hideAnimationRunning) {
                            if (Math.abs(translationX) > layout.getWidth() / 3f) {
                                final float tx = Math.signum(translationX) * layout.getWidth();
                                final boolean needAlphaAnimation = (translationX < 0f && needLeftAlphaAnimation) || (translationX > 0f && needRightAlphaAnimation);
                                layout.animate().translationX(tx).alpha(needAlphaAnimation ? 0f : 1f).setDuration(200).setInterpolator(AndroidUtilities.accelerateInterpolator).withEndAction(() -> {
                                    if (layout.getTranslationX() == tx) {
                                        onHide();
                                    }
                                }).start();
                            } else {
                                layout.animate().translationX(0).alpha(1f).setDuration(200).start();
                            }
                        }
                        onPressedStateChanged(pressed = false);
                    }
                }
                return true;
            }
            return false;
        }

        private boolean inLayoutHitRect(float x, float y) {
            layout.getHitRect(rect);
            return rect.contains((int) x, (int) y);
        }

        protected abstract void onPressedStateChanged(boolean pressed);
        protected abstract void onHide();
    }

    //region Offset Providers
    public static void addDelegate(@NonNull BaseFragment fragment, @NonNull Delegate delegate) {
        final FrameLayout containerLayout = fragment.getLayoutContainer();
        if (containerLayout != null) {
            addDelegate(containerLayout, delegate);
        }
    }

    public static void addDelegate(@NonNull FrameLayout containerLayout, @NonNull Delegate delegate) {
        delegates.put(containerLayout, delegate);
    }

    public static void removeDelegate(@NonNull BaseFragment fragment) {
        final FrameLayout containerLayout = fragment.getLayoutContainer();
        if (containerLayout != null) {
            removeDelegate(containerLayout);
        }
    }

    public static void removeDelegate(@NonNull FrameLayout containerLayout) {
        delegates.remove(containerLayout);
    }

    public interface Delegate {

        default int getBottomOffset() {
            return 0;
        }

        default void onOffsetChange(float offset) {
        }

        default void onShow(Bulletin bulletin) {
        }

        default void onHide(Bulletin bulletin) {
        }
    }
    //endregion

    //region Layouts
    public abstract static class Layout extends FrameLayout {

        private final List<Callback> callbacks = new ArrayList<>();

        protected Bulletin bulletin;

        @WidthDef
        private int wideScreenWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        @GravityDef
        private int wideScreenGravity = Gravity.CENTER_HORIZONTAL;

        public Layout(@NonNull Context context) {
            this(context, Theme.getColor(Theme.key_undo_background));
        }

        public Layout(@NonNull Context context, @ColorInt int backgroundColor) {
            super(context);
            setMinimumHeight(AndroidUtilities.dp(48));
            setBackground(new InsetDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(6), backgroundColor), AndroidUtilities.dp(8)));
            updateSize();
        }

        @Override
        protected void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            updateSize();
        }

        private void updateSize() {
            final boolean isWideScreen = isWideScreen();
            setLayoutParams(LayoutHelper.createFrame(isWideScreen ? wideScreenWidth : LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, isWideScreen ? Gravity.BOTTOM | wideScreenGravity : Gravity.BOTTOM));
        }

        private boolean isWideScreen() {
            return AndroidUtilities.isTablet() || AndroidUtilities.displaySize.x >= AndroidUtilities.displaySize.y;
        }

        private void setWideScreenParams(@WidthDef int width, @GravityDef int gravity) {
            boolean changed = false;

            if (wideScreenWidth != width) {
                wideScreenWidth = width;
                changed = true;
            }

            if (wideScreenGravity != gravity) {
                wideScreenGravity = gravity;
                changed = true;
            }

            if (isWideScreen() && changed) {
                updateSize();
            }
        }

        @SuppressLint("RtlHardcoded")
        private boolean isNeedSwipeAlphaAnimation(boolean swipeLeft) {
            if (!isWideScreen() || wideScreenWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                return false;
            }
            if (wideScreenGravity == Gravity.CENTER_HORIZONTAL) {
                return true;
            }
            if (swipeLeft) {
                return wideScreenGravity == Gravity.RIGHT;
            } else {
                return wideScreenGravity != Gravity.RIGHT;
            }
        }

        public Bulletin getBulletin() {
            return bulletin;
        }

        public boolean isAttachedToBulletin() {
            return bulletin != null;
        }

        @CallSuper
        protected void onAttach(@NonNull Bulletin bulletin) {
            this.bulletin = bulletin;
            for (int i = 0, size = callbacks.size(); i < size; i++) {
                callbacks.get(i).onAttach(this, bulletin);
            }
        }

        @CallSuper
        protected void onDetach() {
            this.bulletin = null;
            for (int i = 0, size = callbacks.size(); i < size; i++) {
                callbacks.get(i).onDetach(this);
            }
        }

        @CallSuper
        protected void onShow() {
            for (int i = 0, size = callbacks.size(); i < size; i++) {
                callbacks.get(i).onShow(this);
            }
        }

        @CallSuper
        protected void onHide() {
            for (int i = 0, size = callbacks.size(); i < size; i++) {
                callbacks.get(i).onHide(this);
            }
        }

        @CallSuper
        protected void onEnterTransitionStart() {
            for (int i = 0, size = callbacks.size(); i < size; i++) {
                callbacks.get(i).onEnterTransitionStart(this);
            }
        }

        @CallSuper
        protected void onEnterTransitionEnd() {
            for (int i = 0, size = callbacks.size(); i < size; i++) {
                callbacks.get(i).onEnterTransitionEnd(this);
            }
        }

        @CallSuper
        protected void onExitTransitionStart() {
            for (int i = 0, size = callbacks.size(); i < size; i++) {
                callbacks.get(i).onExitTransitionStart(this);
            }
        }
        @CallSuper
        protected void onExitTransitionEnd() {
            for (int i = 0, size = callbacks.size(); i < size; i++) {
                callbacks.get(i).onExitTransitionEnd(this);
            }
        }

        //region Callbacks
        public void addCallback(@NonNull Callback callback) {
            callbacks.add(callback);
        }

        public void removeCallback(@NonNull Callback callback) {
            callbacks.remove(callback);
        }

        public interface Callback {

            void onAttach(@NonNull Layout layout, @NonNull Bulletin bulletin);

            void onDetach(@NonNull Layout layout);

            void onShow(@NonNull Layout layout);

            void onHide(@NonNull Layout layout);

            void onEnterTransitionStart(@NonNull Layout layout);

            void onEnterTransitionEnd(@NonNull Layout layout);

            void onExitTransitionStart(@NonNull Layout layout);

            void onExitTransitionEnd(@NonNull Layout layout);
        }
        //endregion

        //region Transitions
        @NonNull
        public Transition createTransition() {
            return new SpringTransition();
        }

        public interface Transition {
            void animateEnter(@NonNull Layout layout, @Nullable Runnable startAction, @Nullable Runnable endAction, @Nullable Consumer<Float> onUpdate, int bottomOffset);
            void animateExit(@NonNull Layout layout, @Nullable Runnable startAction, @Nullable Runnable endAction, @Nullable Consumer<Float> onUpdate, int bottomOffset);
        }

        public static class DefaultTransition implements Transition {

            @Override
            public void animateEnter(@NonNull Layout layout, @Nullable Runnable startAction, @Nullable Runnable endAction, @Nullable Consumer<Float> onUpdate, int bottomOffset) {
                final ObjectAnimator animator = ObjectAnimator.ofFloat(layout, View.TRANSLATION_Y, layout.getHeight(), -bottomOffset);
                animator.setDuration(225);
                animator.setInterpolator(Easings.easeOutQuad);
                if (startAction != null || endAction != null) {
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            if (startAction != null) {
                                startAction.run();
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (endAction != null) {
                                endAction.run();
                            }
                        }
                    });
                }
                if (onUpdate != null) {
                    animator.addUpdateListener(a -> onUpdate.accept((Float) a.getAnimatedValue()));
                }
                animator.start();
            }

            @Override
            public void animateExit(@NonNull Layout layout, @Nullable Runnable startAction, @Nullable Runnable endAction, @Nullable Consumer<Float> onUpdate, int bottomOffset) {
                final ObjectAnimator animator = ObjectAnimator.ofFloat(layout, View.TRANSLATION_Y, layout.getTranslationY(), layout.getHeight());
                animator.setDuration(175);
                animator.setInterpolator(Easings.easeInQuad);
                if (startAction != null || endAction != null) {
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            if (startAction != null) {
                                startAction.run();
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (endAction != null) {
                                endAction.run();
                            }
                        }
                    });
                }
                if (onUpdate != null) {
                    animator.addUpdateListener(a -> onUpdate.accept((Float) a.getAnimatedValue()));
                }
                animator.start();
            }
        }

        public static class SpringTransition implements Transition {

            private static final float DAMPING_RATIO = 0.8f;
            private static final float STIFFNESS = 400f;

            @Override
            public void animateEnter(@NonNull Layout layout, @Nullable Runnable startAction, @Nullable Runnable endAction, @Nullable Consumer<Float> onUpdate, int bottomOffset) {
                final int translationY = layout.getHeight() - bottomOffset;
                layout.setTranslationY(translationY);
                if (onUpdate != null) {
                    onUpdate.accept((float) translationY);
                }
                final SpringAnimation springAnimation = new SpringAnimation(layout, SpringAnimation.TRANSLATION_Y, -bottomOffset);
                springAnimation.getSpring().setDampingRatio(DAMPING_RATIO);
                springAnimation.getSpring().setStiffness(STIFFNESS);
                if (endAction != null) {
                    springAnimation.addEndListener((animation, canceled, value, velocity) -> {
                        if (!canceled) {
                            endAction.run();
                        }
                    });
                }
                if (onUpdate != null) {
                    springAnimation.addUpdateListener((animation, value, velocity) -> onUpdate.accept(value));
                }
                springAnimation.start();
                if (startAction != null) {
                    startAction.run();
                }
            }

            @Override
            public void animateExit(@NonNull Layout layout, @Nullable Runnable startAction, @Nullable Runnable endAction, @Nullable Consumer<Float> onUpdate,int bottomOffset) {
                final SpringAnimation springAnimation = new SpringAnimation(layout, SpringAnimation.TRANSLATION_Y, layout.getHeight() - bottomOffset);
                springAnimation.getSpring().setDampingRatio(DAMPING_RATIO);
                springAnimation.getSpring().setStiffness(STIFFNESS);
                if (endAction != null) {
                    springAnimation.addEndListener((animation, canceled, value, velocity) -> {
                        if (!canceled) {
                            endAction.run();
                        }
                    });
                }
                if (onUpdate != null) {
                    springAnimation.addUpdateListener((animation, value, velocity) -> onUpdate.accept(value));
                }
                springAnimation.start();
                if (startAction != null) {
                    startAction.run();
                }
            }
        }
        //endregion
    }

    @SuppressLint("ViewConstructor")
    public static class ButtonLayout extends Layout {

        private Button button;

        private int childrenMeasuredWidth;

        public ButtonLayout(@NonNull Context context) {
            super(context);
        }

        public ButtonLayout(@NonNull Context context, int backgroundColor) {
            super(context, backgroundColor);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            childrenMeasuredWidth = 0;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (button != null && MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
                setMeasuredDimension(childrenMeasuredWidth + button.getMeasuredWidth(), getMeasuredHeight());
            }
        }

        @Override
        protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            if (button != null && child != button) {
                widthUsed += button.getMeasuredWidth() - AndroidUtilities.dp(12);
            }
            super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
            if (child != button) {
                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                childrenMeasuredWidth = Math.max(childrenMeasuredWidth, lp.leftMargin + lp.rightMargin + child.getMeasuredWidth());
            }
        }

        public Button getButton() {
            return button;
        }

        public void setButton(Button button) {
            if (this.button != null) {
                removeCallback(this.button);
                removeView(this.button);
            }
            this.button = button;
            if (button != null) {
                addCallback(button);
                addView(button, 0, LayoutHelper.createFrameRelatively(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL));
            }
        }
    }

    public static class SimpleLayout extends ButtonLayout {

        public final ImageView imageView;
        public final TextView textView;

        public SimpleLayout(@NonNull Context context) {
            super(context);

            final int undoInfoColor = Theme.getColor(Theme.key_undo_infoColor);

            imageView = new ImageView(context);
            imageView.setColorFilter(new PorterDuffColorFilter(undoInfoColor, PorterDuff.Mode.MULTIPLY));
            addView(imageView, LayoutHelper.createFrameRelatively(24, 24, Gravity.START | Gravity.CENTER_VERTICAL, 16, 12, 16, 12));

            textView = new TextView(context);
            textView.setSingleLine();
            textView.setTextColor(undoInfoColor);
            textView.setTypeface(Typeface.SANS_SERIF);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            addView(textView, LayoutHelper.createFrameRelatively(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 56, 0, 16, 0));
        }
    }

    @SuppressLint("ViewConstructor")
    public static class TwoLineLayout extends ButtonLayout {

        public final BackupImageView imageView;
        public final TextView titleTextView;
        public final TextView subtitleTextView;

        public TwoLineLayout(@NonNull Context context) {
            super(context);

            final int undoInfoColor = Theme.getColor(Theme.key_undo_infoColor);

            addView(imageView = new BackupImageView(context), LayoutHelper.createFrameRelatively(29, 29, Gravity.START | Gravity.CENTER_VERTICAL, 12, 12, 12, 12));

            final LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            addView(linearLayout, LayoutHelper.createFrameRelatively(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 54, 8, 12, 8));

            titleTextView = new TextView(context);
            titleTextView.setSingleLine();
            titleTextView.setTextColor(undoInfoColor);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            linearLayout.addView(titleTextView);

            subtitleTextView = new TextView(context);
            subtitleTextView.setMaxLines(2);
            subtitleTextView.setTextColor(undoInfoColor);
            subtitleTextView.setTypeface(Typeface.SANS_SERIF);
            subtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            linearLayout.addView(subtitleTextView);
        }

    }

    public static class TwoLineLottieLayout extends ButtonLayout {

        public final RLottieImageView imageView;
        public final TextView titleTextView;
        public final TextView subtitleTextView;

        private final int textColor;

        public TwoLineLottieLayout(@NonNull Context context) {
            this(context, Theme.getColor(Theme.key_undo_background), Theme.getColor(Theme.key_undo_infoColor));
        }

        public TwoLineLottieLayout(@NonNull Context context, @ColorInt int backgroundColor, @ColorInt int textColor) {
            super(context, backgroundColor);
            this.textColor = textColor;

            imageView = new RLottieImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            addView(imageView, LayoutHelper.createFrameRelatively(56, 48, Gravity.START | Gravity.CENTER_VERTICAL));

            final int undoInfoColor = Theme.getColor(Theme.key_undo_infoColor);

            final LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            addView(linearLayout, LayoutHelper.createFrameRelatively(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 56, 8, 12, 8));

            titleTextView = new TextView(context);
            titleTextView.setSingleLine();
            titleTextView.setTextColor(undoInfoColor);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            linearLayout.addView(titleTextView);

            subtitleTextView = new TextView(context);
            subtitleTextView.setTextColor(undoInfoColor);
            subtitleTextView.setTypeface(Typeface.SANS_SERIF);
            subtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            linearLayout.addView(subtitleTextView);
        }

        @Override
        protected void onShow() {
            super.onShow();
            imageView.playAnimation();
        }

        public void setAnimation(int resId, String... layers) {
            setAnimation(resId, 32, 32, layers);
        }

        public void setAnimation(int resId, int w, int h, String... layers) {
            imageView.setAnimation(resId, w, h);
            for (String layer : layers) {
                imageView.setLayerColor(layer + ".**", textColor);
            }
        }
    }

    public static class LottieLayout extends ButtonLayout {

        public final RLottieImageView imageView;
        public final TextView textView;

        private final int textColor;

        public LottieLayout(@NonNull Context context) {
            this(context, Theme.getColor(Theme.key_undo_background), Theme.getColor(Theme.key_undo_infoColor));
        }

        public LottieLayout(@NonNull Context context, @ColorInt int backgroundColor, @ColorInt int textColor) {
            super(context, backgroundColor);
            this.textColor = textColor;

            imageView = new RLottieImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            addView(imageView, LayoutHelper.createFrameRelatively(56, 48, Gravity.START | Gravity.CENTER_VERTICAL));

            textView = new TextView(context);
            textView.setSingleLine();
            textView.setTextColor(textColor);
            textView.setTypeface(Typeface.SANS_SERIF);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            addView(textView, LayoutHelper.createFrameRelatively(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL, 56, 0, 16, 0));
        }

        @Override
        protected void onShow() {
            super.onShow();
            imageView.playAnimation();
        }

        public void setAnimation(int resId, String... layers) {
            setAnimation(resId, 32, 32, layers);
        }

        public void setAnimation(int resId, int w, int h, String... layers) {
            imageView.setAnimation(resId, w, h);
            for (String layer : layers) {
                imageView.setLayerColor(layer + ".**", textColor);
            }
        }

        public void setIconPaddingBottom(int paddingBottom) {
            imageView.setLayoutParams(LayoutHelper.createFrameRelatively(56, 48 - paddingBottom, Gravity.START | Gravity.CENTER_VERTICAL, 0, 0, 0, paddingBottom));
        }
    }
    //endregion

    //region Buttons
    @SuppressLint("ViewConstructor")
    public abstract static class Button extends FrameLayout implements Layout.Callback {

        public Button(@NonNull Context context) {
            super(context);
        }

        @Override
        public void onAttach(@NonNull Layout layout, @NonNull Bulletin bulletin) {
        }

        @Override
        public void onDetach(@NonNull Layout layout) {
        }

        @Override
        public void onShow(@NonNull Layout layout) {
        }

        @Override
        public void onHide(@NonNull Layout layout) {
        }

        @Override
        public void onEnterTransitionStart(@NonNull Layout layout) {
        }

        @Override
        public void onEnterTransitionEnd(@NonNull Layout layout) {
        }

        @Override
        public void onExitTransitionStart(@NonNull Layout layout) {
        }

        @Override
        public void onExitTransitionEnd(@NonNull Layout layout) {
        }
    }

    @SuppressLint("ViewConstructor")
    public static final class UndoButton extends Button {

        private Runnable undoAction;
        private Runnable delayedAction;

        private Bulletin bulletin;
        private boolean isUndone;

        public UndoButton(@NonNull Context context, boolean text) {
            super(context);

            final int undoCancelColor = Theme.getColor(Theme.key_undo_cancelColor);

            if (text) {
                TextView undoTextView = new TextView(context);
                undoTextView.setOnClickListener(v -> undo());
                final int leftInset = LocaleController.isRTL ? AndroidUtilities.dp(16) : 0;
                final int rightInset = LocaleController.isRTL ? 0 : AndroidUtilities.dp(16);
                undoTextView.setBackground(Theme.createCircleSelectorDrawable((undoCancelColor & 0x00ffffff) | 0x19000000, leftInset, rightInset));
                undoTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                undoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                undoTextView.setTextColor(undoCancelColor);
                undoTextView.setText(LocaleController.getString("Undo", R.string.Undo));
                undoTextView.setGravity(Gravity.CENTER_VERTICAL);
                ViewHelper.setPaddingRelative(undoTextView, 16, 0, 16, 0);
                addView(undoTextView, LayoutHelper.createFrameRelatively(LayoutHelper.WRAP_CONTENT, 48, Gravity.CENTER_VERTICAL, 8, 0, 0, 0));
            } else {
                final ImageView undoImageView = new ImageView(getContext());
                undoImageView.setOnClickListener(v -> undo());
                undoImageView.setImageResource(R.drawable.chats_undo);
                undoImageView.setColorFilter(new PorterDuffColorFilter(undoCancelColor, PorterDuff.Mode.MULTIPLY));
                undoImageView.setBackground(Theme.createSelectorDrawable((undoCancelColor & 0x00ffffff) | 0x19000000));
                ViewHelper.setPaddingRelative(undoImageView, 0, 12, 0, 12);
                addView(undoImageView, LayoutHelper.createFrameRelatively(56, 48, Gravity.CENTER_VERTICAL));
            }
        }

        public void undo() {
            if (bulletin != null) {
                isUndone = true;
                if (undoAction != null) {
                    undoAction.run();
                }
                bulletin.hide();
            }
        }

        @Override
        public void onAttach(@NonNull Layout layout, @NonNull Bulletin bulletin) {
            this.bulletin = bulletin;
        }

        @Override
        public void onDetach(@NonNull Layout layout) {
            this.bulletin = null;
            if (delayedAction != null && !isUndone) {
                delayedAction.run();
            }
        }

        public UndoButton setUndoAction(Runnable undoAction) {
            this.undoAction = undoAction;
            return this;
        }

        public UndoButton setDelayedAction(Runnable delayedAction) {
            this.delayedAction = delayedAction;
            return this;
        }
    }
    //endregion
}
