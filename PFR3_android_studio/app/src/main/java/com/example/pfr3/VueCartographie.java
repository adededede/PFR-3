package com.example.pfr3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class VueCartographie extends View {
    protected final Rect contour;
    protected Paint peinture;

    public VueCartographie(Context context) {
        super(context);

        //On dessine le contour de la vue de cartographie
        contour = new Rect(10,10,100,100);
        peinture = new Paint();
        peinture.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas c){
        c.drawColor(Color.WHITE);
        c.drawRect(contour,peinture);
    }
}
