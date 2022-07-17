package com.example.flyingbird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.flyingbird.Gameview.screenRatiox;
import static com.example.flyingbird.Gameview.screenRatioy;

public class Bird {
    public int speed=20;
    public boolean wasShot=true;
    int x=0,y,width,height,birdcounter;
    Bitmap bird1,bird2;
    Bird(Resources res)
    {
        bird1= BitmapFactory.decodeResource(res,R.drawable.bird1);
        bird2= BitmapFactory.decodeResource(res,R.drawable.bird2);
        width=bird1.getWidth();
        height=bird1.getHeight();
        width/=6;
        height/=6;
        width=(int)(width*screenRatiox);
        height=(int)(height*screenRatioy);
        bird1=Bitmap.createScaledBitmap(bird1,width,height,false);
        bird2=Bitmap.createScaledBitmap(bird2,width,height,false);
       y=-height;



    }
    Bitmap getBird()
    {
        if(birdcounter==0)
        {
            birdcounter++;
            return bird1;
        }
        birdcounter--;
            return bird2;
        }

  Rect getCollisonshape()
  {
      return new Rect(x,y,x+width,y+height);
  }

}
