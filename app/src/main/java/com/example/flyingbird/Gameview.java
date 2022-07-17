package com.example.flyingbird;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gameview extends SurfaceView implements Runnable {
    private Thread thread;
    private SoundPool soundPool;
    private int sound;
    private boolean isplaying,isGameOver=false;
    private int screenx, screeny,score=0;
    public static float screenRatiox, screenRatioy;
    private Background background1, background2;
    private Plane plane;
    private Bird[] birds;
    private SharedPreferences prefs;
    private Random random;
    private Paint paint;
    private List<Bullet> bullets;
    private GameActivity activity;

    public Gameview(GameActivity activity, int screenx, int screeny) {
        super(activity);

        this.screenx = screenx;
        this.screeny = screeny;
        screenRatiox = 1920f / screenx;
        screenRatioy = 1080f / screeny;
        background1 = new Background(screenx, screeny, getResources());
        background2 = new Background(screenx, screeny, getResources());
        background2.x = screenx;
        paint = new Paint();
       if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes= new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME).build();
                    soundPool=new SoundPool.Builder()
                            .setAudioAttributes(audioAttributes)
                            .build();

        }
        else
            soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        prefs=activity.getSharedPreferences("game",Context.MODE_PRIVATE );
        sound=soundPool.load(activity,R.raw.shoot,1);
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);
        plane=new Plane(this,screeny,getResources());
        bullets=new ArrayList<>();
        birds=new Bird[4];
        for(int i=0;i<4;i++)
        {
            Bird bird=new Bird(getResources());
            birds[i]= bird;
        }
        random=new Random();

    }
    public void run() {

        while (isplaying) {

            update ();
            draw ();
            sleep ();

        }

    }

    private void update () {

        background1.x -= 10 * screenRatiox;
        background2.x -= 10 * screenRatiox;

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenx;
        }

        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenx;
        }
        if(plane.isGoingup)
        {
            plane.y-=30*screenRatioy;
        }
        else
        plane.y+=30*screenRatioy;
        if(plane.y<0)
        {
            plane.y=0;
        }
        if(plane.y>screeny-plane.height)
        {
            plane.y=screeny-plane.height;
        }
        List<Bullet> trash = new ArrayList<>();

        for (Bullet bullet : bullets) {

            if (bullet.x > screenx)
                trash.add(bullet);

            bullet.x += 50 * screenRatiox;
            for(Bird bird:birds)
            {
                if(Rect.intersects(bird.getCollisonshape(),bullet.getCollisionShape()))
                {
                    score++;
                    bird.x=-500;
                    bullet.x=screenx+500;
                    bird.wasShot=true;
                }
            }
                }
        for (Bullet bullet : trash) {
            bullets.remove(bullet);
        }
     for(Bird bird:birds)
     {
         bird.x-=bird.speed;
         if(bird.x+bird.width<0)
         {
             if(!bird.wasShot)
             {
                 isGameOver=true;
                 return;
             }
             int bound= (int) (30*screenRatiox);
             bird.speed=random.nextInt(bound);
         if(bird.speed<10*screenRatiox)
         {
             bird.speed= (int) (10*screenRatiox);
         }
         bird.x=screenx;
         bird.y=random.nextInt(screeny-bird.height);
         bird.wasShot=false;
         }
        if(Rect.intersects(bird.getCollisonshape(),plane.getCollisonshape()))
        {
             isGameOver = true;
             return;
        }
     }
    }

    private void sleep () {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void draw () {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);
            for(Bird bird:birds)
                canvas.drawBitmap(bird.getBird(),bird.x,bird.y,paint);
            canvas.drawText(score+"",screenx/2f,164,paint);

            if(isGameOver)
            {
                isplaying=false;
                canvas.drawBitmap(plane.getDead(),plane.x,plane.y,paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting();

                return;
            }

            canvas.drawBitmap(plane.getPlane(),plane.x,plane.y,paint);
            for(Bullet bullet:bullets)
            {
                canvas.drawBitmap(bullet.bullet,bullet.x,bullet.y,paint);
            }

            getHolder().unlockCanvasAndPost(canvas);


        }

    }

    private void waitBeforeExiting() {
        try{
            thread.sleep(3000);
            activity.startActivity(new Intent(activity,MainActivity.class));
            activity.finish();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveIfHighScore() {
        if(prefs.getInt("highscore",0)<score) {
            SharedPreferences.Editor editor=prefs.edit();
            editor.putInt("highscore",score);
            editor.apply();
        }
        }

    public void resume()
    { isplaying=true;
       thread=new Thread(this);
       thread.start();
    }
    public void pause()
    {
        try {
            isplaying=false;
            thread.join();
        }catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
  public boolean  onTouchEvent(MotionEvent event)
    {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
            if (event.getX()<screenx/2){
                plane.isGoingup=true;
            }
            break;
            case MotionEvent.ACTION_UP:
            plane.isGoingup=false;
            if(event.getX()>screenx/2)
                plane.toshoot++;
            break;
        }
        return true;

    }

    public void newbullet() {
        if(!prefs.getBoolean("isMute",false))
            soundPool.play(sound,1,1,0,0,1);

        Bullet bullet = new Bullet(getResources());
        bullet.x = plane.x + plane.width;
        bullet.y = plane.y + (plane.height / 2);
        bullets.add(bullet);

    }


}





