package com.ekek.viewmodule.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ekek.viewmodule.R;

import static android.graphics.Region.Op.INTERSECT;

public class RoundedImageView extends AppCompatImageView {

    // Constants
    private static final float DEFAULT_X_RADIUS = 1.0f;
    private static final float DEFAULT_Y_RADIUS = 1.0f;
    private static final float DEFAULT_STROKE_WIDTH = 0.0f;
    private static final int DEFAULT_STROKE_COLOR = Color.WHITE;

    // Fields
    private Context context;
    private Path path;
    private Paint paint;
    private RectF bounds = new RectF();
    private Region region = new Region();
    private RectF rectF;
    private float radiusX;
    private float radiusY;
    private float strokeWidth;
    private int strokeColor;

    // Constructors
    /**
     * Initialize a new instance of the class RoundedImageView.
     * @param context context
     * @param attrs attributes
     */
    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initFields(context, attrs);
    }

    // Override functions
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        rectF = new RectF(
                getPaddingLeft(),
                getPaddingTop(),
                w - getPaddingRight(),
                h - getPaddingBottom());
        path.reset();
        path.addRoundRect(rectF, radiusX, radiusY, Path.Direction.CW);
        path.close();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(path, INTERSECT);
        super.onDraw(canvas);
        if (strokeWidth > 0) {
            canvas.drawRoundRect(rectF, radiusX, radiusY, paint);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        path.computeBounds(bounds, true);
        region.set(
                (int)bounds.left,
                (int)bounds.top,
                (int)bounds.right,
                (int)bounds.bottom);
        region.setPath(path, region);
        boolean ct =  region.contains((int)event.getX(), (int)event.getY());
        return ct && super.onTouchEvent(event);
    }


    // Private functions

    /**
     * initialize all the fields of the class
     * @param context context
     * @param attrs attrs
     */
    private void initFields(Context context, AttributeSet attrs) {
        this.context = context;
        this.paint = new Paint();
        this.path = new Path();
        TypedArray typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.viewmodule_RoundedImageView);
        radiusX = typedArray.getFloat(R.styleable.viewmodule_RoundedImageView_viewmodule_x_radius, DEFAULT_X_RADIUS);
        radiusY = typedArray.getFloat(R.styleable.viewmodule_RoundedImageView_viewmodule_y_radius, DEFAULT_Y_RADIUS);
        strokeWidth = typedArray.getFloat(R.styleable.viewmodule_RoundedImageView_viewmodule_stroke_width, DEFAULT_STROKE_WIDTH);
        strokeColor = typedArray.getColor(R.styleable.viewmodule_RoundedImageView_viewmodule_stroke_color, DEFAULT_STROKE_COLOR);
        paint.setColor(strokeColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        typedArray.recycle();
    }

    // Properties
    public void setRadiusX(float radiusX) {
        this.radiusX = radiusX;
    }
    public void setRadiusY(float radiusY) {
        this.radiusY = radiusY;
    }
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        paint.setStrokeWidth(strokeWidth);
    }
    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        paint.setColor(strokeColor);
    }
}