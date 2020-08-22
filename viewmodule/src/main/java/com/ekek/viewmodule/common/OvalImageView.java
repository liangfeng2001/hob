package com.ekek.viewmodule.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import static android.graphics.Region.Op.INTERSECT;

public class OvalImageView extends AppCompatImageView {

    // Fields
    private Context context;
    private Path path;
    private RectF bounds = new RectF();
    private Region region = new Region();

    // Constructors
    /**
     * Initialize a new instance of the class OvalImageView.
     *
     * @param context context
     * @param attrs   attributes
     */
    public OvalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFields(context);
    }

    // Override functions
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        RectF rectF = new RectF(
                getPaddingLeft(),
                getPaddingTop(),
                w - getPaddingRight(),
                h - getPaddingBottom());
        path.reset();
        path.addOval(rectF, Path.Direction.CW);
        path.close();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(path, INTERSECT);
        super.onDraw(canvas);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        path.computeBounds(bounds, true);
        region.set(
                (int) bounds.left,
                (int) bounds.top,
                (int) bounds.right,
                (int) bounds.bottom);
        region.setPath(path, region);
        boolean ct = region.contains((int) event.getX(), (int) event.getY());
        return ct && super.onTouchEvent(event);
    }

    // Private functions
    /**
     * initialize all the fields of the class
     *
     * @param context context
     */
    private void initFields(Context context) {
        this.context = context;
        this.path = new Path();
    }
}