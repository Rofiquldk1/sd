package com.agamilabs.smartshop.ui.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

public class RoundRectCornerImageView extends AppCompatImageView {

    private float radius = 18.0f;
    private Path path;
    private RectF rect;

    public RoundRectCornerImageView(Context context) {
        super(context);
        init();
    }

    public RoundRectCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundRectCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}