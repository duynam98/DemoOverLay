package com.xiaopo.flying.sticker;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;


public class DialogDrawable extends Drawable {

    private Paint mBorderPaint;
    public Paint mTextPaint;
    private RectF mRectF;
    private int mRadius = 20;
    private int mOffsetY = 30;
    private int mOffsetX = 0;

    private int width = 0;
    private int height = 0;

    private String textDraw;

    private float maxTextSize = 40;

    public DialogDrawable() {
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(Color.parseColor("#00000000"));

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(16);

//        mRectF = new RectF(0, 0, getIntrinsicWidth() - mOffsetX, getIntrinsicHeight() - mOffsetY);\
        textDraw = "double_click";
        resizeText();
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    public int getOffsetY() {
        return mOffsetY;
    }

    public void setOffsetY(int offsetY) {
        mOffsetY = offsetY;
    }

    public int getOffsetX() {
        return mOffsetX;
    }

    public void setOffsetX(int offsetX) {
        mOffsetX = offsetX;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mBorderPaint);

        Path path = new Path();
        path.moveTo(mRectF.right - 2 * mOffsetY - mOffsetX, mRectF.bottom);
        path.lineTo(mRectF.right - (2 * mOffsetY + mRadius) / 2 - mOffsetX, mRectF.bottom + mOffsetY);
        path.lineTo(mRectF.right - mRadius - mOffsetX, mRectF.bottom);

        float x = 0;
        float y = mRectF.centerY();

        float[] characterWidths = new float[textDraw.length()];
        int characterNum = mTextPaint.getTextWidths(textDraw, characterWidths);

        float textWidth = 0f;
        for (int i = 0; i < characterNum; i++) {
            textWidth += characterWidths[i];
        }
        canvas.save();
        canvas.translate(mRectF.width() / 2 - textWidth / 2, 0);
        canvas.drawText(textDraw, x, y, mTextPaint);

        canvas.restore();

        canvas.drawPath(path, mBorderPaint);
    }

    @Override
    public void setAlpha(int i) {
        mBorderPaint.setAlpha(i);
        mTextPaint.setAlpha(i);
    }


    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public int getOpacity() {
        return 0;
    }

    public void setText(String valueText) {
        textDraw = valueText;
        resizeText();
    }

    public void setFontText(Typeface intColor) {
        mTextPaint.setTypeface(intColor);
    }

    public void setColorText(int intColor) {
        mTextPaint.setColor(intColor);
    }

    public int getColor(){
        return mTextPaint.getColor();
    }

    public void setStroke(float width, String color) {
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setStrokeWidth(width);
        //mTextPaint.setColor(Color.parseColor("#" + color));
    }

    public void setLetterSpacing(float space) {
        mTextPaint.setLetterSpacing(space);
        resizeText();
    }

    public void setGradient(String colorTop, String colorBottom) {
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(textDraw, 0, textDraw.length(), bounds);
        int height = bounds.height();

        Shader textShader = new LinearGradient(0, 0, bounds.width(), 0,
                Color.parseColor("#" + colorBottom), Color.parseColor("#" + colorTop)
                , Shader.TileMode.MIRROR);
        mTextPaint.setShader(textShader);
    }

    public void setShadow(float radius, float width, float height, String color) {
        if (mTextPaint != null) {
            mTextPaint.setShadowLayer(radius, width, height, Color.parseColor("#" + color));
        }
    }

    public void setOpacityText(int alpha) {
        mTextPaint.setAlpha(alpha);
    }

    public void deleteGradient() {
        mTextPaint.setShader(null);
    }

    public String getTextDraw() {
        return textDraw;
    }

    private void resizeText() {
        Paint paint = new Paint();
        Rect bounds = new Rect();
        paint.setTextSize(40);
        paint.setLetterSpacing(mTextPaint.getLetterSpacing());
        paint.setTypeface(mTextPaint.getTypeface());
        paint.getTextBounds(textDraw, 0, textDraw.length(), bounds);
        width = bounds.width() + 50;
        height = bounds.height() + 50;
        mRectF = new RectF(0, 25, width, height);
    }

    public int getAlphaText() {
        return mTextPaint.getAlpha();
    }

    public void setTextSize(float size) {
        if (mTextPaint != null) {
            if (size < 40) {
                mTextPaint.setTextSize(size);
            }
        }
    }

}
