package com.casper.testdrivendevelopment.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import com.casper.testdrivendevelopment.R;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private  SurfaceHolder surfaceHolder;
    private  DrawThread drawThread;
    private ArrayList<Sprite> sprites=new ArrayList<>();

    double xTouch=-1,yTouch=-1;
    int count=0;
    int size=20;
    int millis=100;

    @SuppressLint("ClickableViewAccessibility")
    public GameView(Context context) {
        super(context);
        surfaceHolder=this.getHolder();
        surfaceHolder.addCallback(this);

        for(int i=0;i<size;i++) {
            sprites.add(new Sprite(R.drawable.star));
        }

       this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xTouch=event.getX();
                yTouch=event.getY();
                return false;
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(null==drawThread){
            drawThread=new DrawThread();
            drawThread.start();

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(null!=drawThread){
            drawThread.stopThread();
            drawThread=null;
        }
    }

    private class DrawThread extends Thread{
        private Boolean Alive=false;

        public void stopThread() {
            Alive = false;
            while (true) {
                try {
                    this.join();//保证run方法执行完毕
                    break;
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void run(){
            Alive = true;
            while (Alive) {
                Canvas canvas = null;
                try {
                    synchronized (surfaceHolder) {
                        canvas = surfaceHolder.lockCanvas();
                        drawBackground(canvas);
                        //在20，40的地方画一个文本
                        Paint p = new Paint();
                        p.setTextSize(50);
                        p.setColor(Color.WHITE);
                        canvas.drawText("共击中了"+count+"个",20,50,p);

                        for (int i = 0; i < sprites.size(); i++) sprites.get(i).move();
                        for (int i = 0; i < sprites.size(); i++) sprites.get(i).draw(canvas);

                        for(int i=0;i<sprites.size();i++){
                            if(xTouch <= sprites.get(i).getDrawableRect().right && xTouch >= sprites.get(i).getDrawableRect().left
                                    && yTouch <= sprites.get(i).getDrawableRect().bottom && yTouch >= sprites.get(i).getDrawableRect().top){
                                count++;
                                xTouch=-1;yTouch=-1;
                                sprites.remove(i);
                                if(sprites.size()==0){
                                    Paint p1 = new Paint();
                                    p1.setTextSize(80);
                                    p1.setColor(Color.WHITE);
                                    if(size==0){
                                        canvas.drawText("恭喜通关",260,1200,p1);
                                    }
                                    else{
                                        canvas.drawText("恭喜进入下一关",260,1200,p1);
                                    }
                                    size=size-5;
                                    millis=millis-20;

                                    for(int j=0;j<size;j++){
                                        sprites.add(new Sprite(R.drawable.star));
                                    }
                                    count=0;

                                }
                            }
                        }

                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (null != canvas) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(millis);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void drawBackground(Canvas canvas) {
        Drawable drawable= getContext().getResources().getDrawable(R.drawable.background);
        Rect drawableRect=new Rect(0,0,canvas.getWidth(),canvas.getHeight());
        drawable.setBounds(drawableRect);
        drawable.draw(canvas);
    }

    private class Sprite {
        private int resouceId;
        private int x,y;
        private double direction;
        private Rect drawableRect;
        private Drawable drawable;

        public Sprite(int resouceId) {
            this.resouceId = resouceId;
            x = (int) (Math.random() * getWidth());
            y = (int) (Math.random() * getHeight());
            direction = Math.random() * 2 * Math.PI;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }


        public Rect getDrawableRect() {
            return drawableRect;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

        public void move() {
            x += 15 * Math.cos(direction);
            if (x < 0)
                x = getWidth();
            else if (x > getWidth())
                x = 0;
            y += 15 * Math.sin(direction);
            if (y < 0)
                y = getHeight();
            else if (y > getHeight())
                y = 0;
            if (Math.random() < 0.05)
                direction = Math.random() * 2 * Math.PI;
        }

        public void draw(Canvas canvas){
            drawable= getContext().getResources().getDrawable(R.drawable.star);
            drawableRect=new Rect(x,y,x+drawable.getIntrinsicWidth(),y+drawable.getIntrinsicHeight());
            drawable.setBounds(drawableRect);
            drawable.draw(canvas);
        }


    }

}
