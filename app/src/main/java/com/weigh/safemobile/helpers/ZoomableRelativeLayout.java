package com.weigh.safemobile.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

public class ZoomableRelativeLayout extends RelativeLayout implements ScaleGestureDetector.OnScaleGestureListener{

    float mScaleFactor = 1;
    float mPivotX;
    float mPivotY;
    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 5.0f;

    float startingSpan;
    float endSpan;
    float startFocusX;
    float startFocusY;
    //float scale = 1.0f;

    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }
    private Mode mode = Mode.NONE;
    // Where the finger first  touches the screen
    private float startX = 0f;
    private float startY = 0f;

    // How much to translate the canvas
    private float dx = 0f;
    private float dy = 0f;
    private float prevDx = 0f;
    private float prevDy = 0f;
    private float leftDx = 0f;
    private float rightDx = 0f;
    private float topDy = 0f;
    private float bottomDy = 0f;

    public ZoomableRelativeLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public ZoomableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public ZoomableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:

                        if (mScaleFactor > MIN_ZOOM) {
                            mode = Mode.DRAG;
                            startX = motionEvent.getX() - prevDx;
                            startY = motionEvent.getY() - prevDy;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == Mode.DRAG) {

                            dx = motionEvent.getX() - startX;
                            dy = motionEvent.getY() - startY;
                            if (dx>0) {
                                if(dx>leftDx) {
                                    dx = leftDx;
                                }
                            } else if (dx < 0) {
                                if(dx<rightDx) {
                                    dx = rightDx;
                                }
                            }
                            if (dy>0) {
                                if(dy>topDy) {
                                    dy = topDy;
                                }
                            } else if (dy < 0) {
                                if(dy<bottomDy) {
                                    dy = bottomDy;
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mode = Mode.ZOOM;
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = Mode.NONE;
                        break;
                    case MotionEvent.ACTION_UP:
                        mode = Mode.NONE;
                        prevDx = dx;
                        prevDy = dy;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);

                if ((mode == Mode.DRAG && mScaleFactor >= MIN_ZOOM)|| mode == Mode.ZOOM) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    applyScaleAndTranslation();

                }
                return true;
            }
        });
    }

    private void applyScaleAndTranslation() {

        child().setTranslationX(dx);
        child().setTranslationY(dy);
        if (mScaleFactor == 1) {
            child().setTranslationY(0);
            child().setTranslationX(0);
        }

    }

    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor, mPivotX, mPivotY);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    public void scale(float scaleFactor, float pivotX, float pivotY) {
        mScaleFactor *= scaleFactor;
        mScaleFactor = Math.max(MIN_ZOOM, Math.min(mScaleFactor, MAX_ZOOM));

        mPivotX = pivotX;
        mPivotY = pivotY;
        this.invalidate();
    }

    public void restore() {
        mScaleFactor = 1;
        this.invalidate();
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float s = detector.getScaleFactor();//detector.getCurrentSpan()/startingSpan
        scale(s, startFocusX, startFocusY);

        leftDx = (1-1/mScaleFactor)* startFocusX;
        rightDx = (1-mScaleFactor)*(child().getWidth()-startFocusX)/mScaleFactor;

        topDy = (1-1/mScaleFactor)* startFocusY;
        bottomDy = (1-mScaleFactor)*(child().getHeight()-startFocusY)/mScaleFactor;
        System.out.println(child().getHeight());

        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        startingSpan = detector.getCurrentSpan();
        startFocusX = detector.getFocusX();
        startFocusY = detector.getFocusY();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        //restore();
    }

    private View child() {
        return getChildAt(0);
    }
}
