package com.ekek.commonmodule.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class Marquee {

    // Field
    private String text;
    private Rect idealRect;
    private RectF limitRect;
    private PointF position;
    private int speed;
    private int textOffset;

    // Constructors
    public Marquee(String text, RectF limitRect, PointF position, int speed) {
        this.text = text;
        this.limitRect = limitRect;
        this.position = position;
        this.speed = speed;
        if (this.speed < 1) {
            this.speed = 1;
        }
    }

    // Public functions
    public void marquee(Canvas canvas, Paint paint) {
        if (idealRect == null) {
            idealRect = getStringRect(paint, text);
        }

        canvas.save();
        canvas.clipRect(limitRect);
        canvas.translate(-1.0f * textOffset, 0);
        canvas.drawText(text, position.x, position.y, paint);
        canvas.restore();

        textOffset += speed;
        if (textOffset > idealRect.width()) {
            textOffset = -1 * (int)limitRect.width();
        }
    }

    /**
     * 重置当前显示的位置
     * @param offset 字符起始位置，0代表最左侧，<0 的值代表字符起始位置相对于最左侧的距离
     */
    public void reset(int offset) {
        textOffset = offset;
    }

    // Private functions
    private Rect getStringRect(Paint paint, String value) {
        Rect rect = new Rect();
        paint.getTextBounds(value, 0, value.length(), rect);
        return rect;
    }
}
