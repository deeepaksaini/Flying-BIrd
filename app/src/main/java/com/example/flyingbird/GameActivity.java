package com.example.flyingbird;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {
   private Gameview gameview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAGS_CHANGED,WindowManager.LayoutParams.FLAGS_CHANGED);
        Point point=new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        gameview=new Gameview(this,point.x,point.y);

        setContentView(gameview);
    }
    protected void onPause(){
        super.onPause();
        gameview.pause();
    }
    protected void onResume()
    {
        super.onResume();
        gameview.resume();
    }
}