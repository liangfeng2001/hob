package com.ekek.viewmodule.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ekek.viewmodule.R;

import static android.graphics.Region.Op.INTERSECT;

public class ArcuateTextView extends View {

    // Constants
    private static final float DEFAULT_ATTR_FONT_SIZE = 60.0f;
    private static final float DEFAULT_EXTRA_SPACE = 10.0f;
    private static final float DEFAULT_TEXT_OFFSET_H = 0.0f;
    private static final float DEFAULT_TEXT_OFFSET_V = -18.0f;

    // Fields
    private Context context;
    private Path path;
    private Path pathOuterArc;
    private Path pathInnerArc;
    private Paint paint;
    private float fontSize = DEFAULT_ATTR_FONT_SIZE;
    private int fontColor;
    private String text;

    // Constructors
    public ArcuateTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs);
    }

    // Override functions
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(getSuggestedMinimumWidth(), widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                width = getSuggestedMinimumWidth();
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(getSuggestedMinimumHeight(), heightSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                height = getSuggestedMinimumWidth();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        RectF rectOut = new RectF(
                getPaddingLeft(),
                getPaddingTop(),
                w - getPaddingRight(),
                h - getPaddingBottom());
        RectF rectIn = new RectF(
                getPaddingLeft() + fontSize + DEFAULT_EXTRA_SPACE,
                getPaddingTop() + fontSize + DEFAULT_EXTRA_SPACE,
                w - getPaddingRight() - fontSize - DEFAULT_EXTRA_SPACE,
                h - getPaddingBottom() - fontSize - DEFAULT_EXTRA_SPACE);

        float[] pointStart = new float[] {0.0f, 0.0f};
        float[] pointStart2 = new float[] {0.0f, 0.0f};
        PathMeasure pathMeasure;

        path.reset();

        pathInnerArc.reset();
        pathInnerArc.addArc(rectIn, 90.0f, 90.0f);
        pathMeasure = new PathMeasure(pathInnerArc, false);
        pathMeasure.getPosTan(0, pointStart, null);
        path.moveTo(pointStart[0], pointStart[1]);
        path.addPath(pathInnerArc);

        pathOuterArc.reset();
        pathOuterArc.addArc(rectOut, 180.0f, -90.0f);
        pathMeasure = new PathMeasure(pathOuterArc, false);
        pathMeasure.getPosTan(0, pointStart2, null);
        path.lineTo(pointStart2[0], pointStart2[1]);
        path.addPath(pathOuterArc);

        path.lineTo(pointStart[0], pointStart[1]);
        path.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(path, INTERSECT);
        super.onDraw(canvas);

        canvas.drawTextOnPath(text, pathOuterArc, DEFAULT_TEXT_OFFSET_H, DEFAULT_TEXT_OFFSET_V, paint);
    }

    // Public functions
    public void setTypeface(Typeface typeface) {
        paint.setTypeface(typeface);
        invalidate();
    }
    public void setText(int value) {
        if (value != -1) {
            text = context.getResources().getString(value);
        } else {
            text = "";
        }
        invalidate();
    }

    // Private functions
    private void initialize(Context context, @Nullable AttributeSet attrs) {
        this.context = context;

        readAttrs(attrs);

        path = new Path();
        pathOuterArc = new Path();
        pathInnerArc = new Path();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(fontColor);
        paint.setTextSize(fontSize);
        paint.setTextAlign(Paint.Align.CENTER);
    }
    private void readAttrs(AttributeSet attrs) {
        @SuppressLint("CustomViewStyleable")
        TypedArray typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.viewmodule_ArcuateTextView);
        fontSize = typedArray.getDimension(R.styleable.viewmodule_ArcuateTextView_viewmodule_font_size, DEFAULT_ATTR_FONT_SIZE);
        fontColor = typedArray.getColor(R.styleable.viewmodule_ArcuateTextView_viewmodule_font_color, Color.WHITE);
        int r = typedArray.getResourceId(R.styleable.viewmodule_ArcuateTextView_viewmodule_text, -1);
        if (r != -1) {
            text = context.getResources().getString(r);
        } else {
            text = "";
        }
        typedArray.recycle();
    }
}
