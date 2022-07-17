package com.example.flyingbird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.flyingbird.Gameview.screenRatiox;
import static com.example.flyingbird.Gameview.screenRatioy;

public class Plane{
    public boolean isGoingup=false;
    public int toshoot=0;
    int x,y,width,height,wingCounter=0,shootcounter=1;
    Bitmap plane1,plane2,shoot1,shoot2,shoot3,shoot4,shoot5,dead;
    private Gameview gameview;
    Plane( Gameview gameview,int screeny, Resources res)
    {
        this.gameview=gameview;
        plane1= BitmapFactory.decodeResource(res,R.drawable.fly1);
        plane2=  BitmapFactory.decodeResource(res,R.drawable.fly2);
        shoot1= BitmapFactory.decodeResource(res,R.drawable.shoot1);
        shoot2= BitmapFactory.decodeResource(res,R.drawable.shoot2);
        shoot3= BitmapFactory.decodeResource(res,R.drawable.shoot3);
        shoot4= BitmapFactory.decodeResource(res,R.drawable.shoot4);
        shoot5= BitmapFactory.decodeResource(res,R.drawable.shoot5);
        width=plane1.getWidth();
        height=plane1.getHeight();
        width/=4;
        height/=4;
        width=(int)(width*screenRatiox);
        height=(int)(height*screenRatioy);
        plane1=Bitmap.createScaledBitmap(plane1,width,height,false);
        plane2=Bitmap.createScaledBitmap(plane2,width,height,false);
        shoot1=Bitmap.createScaledBitmap(shoot1,width,height,false);
        shoot2=Bitmap.createScaledBitmap(shoot2,width,height,false);
        shoot3=Bitmap.createScaledBitmap(shoot3,width,height,false);
        shoot4=Bitmap.createScaledBitmap(shoot4,width,height,false);
        shoot5=Bitmap.createScaledBitmap(shoot5,width,height,false);
        dead=BitmapFactory.decodeResource(res,R.drawable.dead);
        dead=Bitmap.createScaledBitmap(dead,width,height,false);
        y=screeny/2;
        x= (int) (64*screenRatiox);
    }
    Bitmap getPlane()
    {
        if(toshoot!=0)
        {
            if(shootcounter==1)
            {
                shootcounter++;
                return shoot1;
            }
            if(shootcounter==2)
            {
                shootcounter++;
                return shoot2;
            }if(shootcounter==3)
        {
            shootcounter++;
            return shoot3;
        }if(shootcounter==4)
        {
            shootcounter++;
            return shoot4;
        }
            shootcounter=1;
            toshoot--;
            gameview.newbullet();
            return shoot5;
        }

        if(wingCounter==0){
            wingCounter++;
            return plane1;
        }
        wingCounter--;
        return plane2;
    }
    Rect getCollisonshape()
    {
        return new Rect(x,y,x+width,y+height);
    }
    Bitmap getDead()
    {
        return dead;
    }
}
