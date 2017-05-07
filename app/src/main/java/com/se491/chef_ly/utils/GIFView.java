package com.se491.chef_ly.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import com.se491.chef_ly.R;

import java.io.InputStream;

/**
 * Created by Don on 5/7/2017.
 */
public class GIFView extends View {
    public Movie mMovie;
    public long movieStart;

    public GIFView(Context context) {
        super(context);
        initializeView();
    }

    public GIFView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public GIFView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView();
    }

    private void initializeView() {
//R.drawable.loader - our animated GIF

        InputStream is = getContext().getResources().openRawResource(+ R.drawable.chefly_intro1);

        mMovie = Movie.decodeStream(is);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        long now = android.os.SystemClock.uptimeMillis();
        if (movieStart == 0) {
            movieStart = now;
        }
        if (mMovie != null) {
            int relTime = (int) ((now - movieStart) % mMovie.duration());
            mMovie.setTime(relTime);
            mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight() - mMovie.height());
            this.invalidate();
        }
    }
    private int gifId;

    public void setGIFResource(int resId) {
        this.gifId = resId;
        initializeView();
    }

    public int getGIFResource() {
        return this.gifId;
    }
}