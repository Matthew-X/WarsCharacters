package com.glasswellapps.iact.character_screen.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PulsatingImageView extends View implements Runnable{
    Thread thread;
    Bitmap image;
    boolean scaled = false;
    public PulsatingImageView(@NonNull Context context) {
        super(context);
        init();
    }

    public PulsatingImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PulsatingImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        thread = new Thread(this::run);
        thread.start();
    }


    public void setImageBitmap(Bitmap bitmap){
        image = bitmap;
        scaled = false;
    }


    float time = 0;
    Paint paint = new Paint();
    int offsetX = 0;
    int offsetY = 0;

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(canvas==null || image==null){
            return;
        }
        if(!scaled) {
            if (image != null) {
                image = Bitmap.createScaledBitmap(image,
                        canvas.getHeight()*image.getWidth()/image.getHeight(),
                        canvas.getHeight(),
                        false);
                offsetX = (image.getWidth()-canvas.getWidth())/2;

            }
            scaled = true;

        }

        canvas.drawBitmap(image,-offsetX,0, paint);

    }


    void update(int deltaTime){
        if(scaled) {
            int alpha = (int) (100+155 * (1 + Math.sin(time / 1000 *4))/2);
            paint.setAlpha(alpha);
        }
    }

    int fixedDeltaTime = 1000/60;
    boolean isRunning = true;
    @Override
    public void run() {
        while(isRunning) {
            time+=fixedDeltaTime;
            //System.out.println("tick");
            update(fixedDeltaTime);
            float dt = System.currentTimeMillis();
            postInvalidate();
            dt = System.currentTimeMillis()-dt;
            int lag = (int)(fixedDeltaTime-dt);
            if(lag > 0){
                try{
                    thread.sleep(lag);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            while(lag<0){
                lag+=fixedDeltaTime;
                update(fixedDeltaTime);
            }

            try{
                thread.sleep(fixedDeltaTime);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void onStop() throws InterruptedException {
        recycleBitmap(image);
        image = null;
        isRunning = false;
        thread.join();
    }
    private void recycleBitmap(Bitmap bitmap){
        if(bitmap!=null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }
}
