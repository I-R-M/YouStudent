package com.example.mycolorapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

public class CustomButton extends View
{
    private byte r,g,b;
    public CustomButton(Context context) {
        super(context);
        setRandColor();
    }

    public CustomButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setRandColor();
    }

    public CustomButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRandColor();
    }

    public CustomButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setRandColor();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Paint paint = new Paint();
//        for (int i = 0; i < getWidth() ; i++) {
//            for (int j = 0; j < getHeight(); j++) {
//                paint.setColor(getColorReleation(i,j));
//                canvas.drawPoint(i,j,paint);
//            }
//        }
    }
    public void setRandColor()
    {
        Random random = new Random();
        r = (byte)(random.nextInt());
        g = (byte)(random.nextInt());
        b = (byte)(random.nextInt());
    }
    private int getColor()
    {
        return (r | (g>>8) | (b>>16));
    }
    private int distance(int i, int j)
    {
        Point middle = new Point(getWidth()/2,getHeight()/2);
        return (int)Math.sqrt(Math.pow(i-middle.x,2) + Math.pow(j-middle.y,2));
    }
    private int getColorReleation(int i , int j)
    {
        byte r1,g1,b1;
        byte d = (byte)distance(i,j);
        r1 = (byte)(r - d);
        g1 = (byte)(g - d);
        b1 = (byte)(b - d);
        return (r1 | (g1 << 8) | (b1 << 16));
    }
}
