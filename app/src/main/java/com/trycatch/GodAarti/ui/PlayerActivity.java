package com.trycatch.GodAarti.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.trycatch.GodAarti.R;
import com.trycatch.GodAarti.adapter.MusicService;

import java.io.IOException;

import static com.trycatch.GodAarti.data.constant.audio;
import static com.trycatch.GodAarti.data.constant.desc;
import static com.trycatch.GodAarti.data.constant.imgs;
import static com.trycatch.GodAarti.data.constant.names;


public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, ServiceConnection {

    ImageButton playpause,Repeat;
    static MediaPlayer mp;
    int abc;
    int mlength;
    ImageView imgview;
    TextView audname;
    TextView auddesc;
    TextView current,maxlength;
    SeekBar seekBar;
    private Handler handler;
    private View view;
    MusicService musicService;
    boolean repeatboolean=false;
    DisplayMetrics displayMetrics ;
    int width, height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.cablayout);

        View view = getSupportActionBar().getCustomView();
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Repeat=findViewById(R.id.repeat);
        playpause = findViewById(R.id.playpause);
        imgview = findViewById(R.id.audiopic);
        audname = findViewById(R.id.audioname);
        auddesc = findViewById(R.id.audiodesc);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        current=findViewById(R.id.currenttime);
        maxlength=findViewById(R.id.auddurattion);
        handler=new Handler();

        Intent newIntent = getIntent();
        abc = newIntent.getIntExtra("Key",0);
        imgview.setImageResource(imgs[abc]);
        audname.setText(names[abc]);
        auddesc.setText(desc[abc]);
        auddesc.setSelected(true);

        Player();

        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    mp.pause();

                    playpause.setImageResource(R.drawable.play);
                }else{
                    mp.start();
                    playpause.setImageResource(R.drawable.pause);
                }
            }
        });


                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                mlength=mp.getDuration();
                seekBar.setMax(mlength);
                seekBar.setProgress(0);
                mp.start();
                changeSeekbar();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mp.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mp!=null){
                    int mcurrent = mp.getCurrentPosition()/1000;
                    seekBar.setProgress(mp.getCurrentPosition());
                    current.setText(formattedTime(mcurrent));
                     mlength =mp.getDuration()/1000;
                    maxlength.setText(formattedMaxTime(mlength));
                }
                handler.postDelayed(this,1000);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.setwallpaper:
                GetScreenWidthHeight();

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),imgs[abc]);
                Bitmap temp = SetBitmapSize(bitmap);

                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

                try {


                    wallpaperManager.setBitmap(temp);
                    Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Wallpaper not Set", Toast.LENGTH_SHORT).show();

                }
                return (true);
            case R.id.share:
                ApplicationInfo api = getApplicationContext().getApplicationInfo();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String sharebody = " God Aarti";
                intent.putExtra(Intent.EXTRA_STREAM, sharebody);
                startActivity(Intent.createChooser(intent, "ShareVia"));
                return (true);
            case R.id.about:
                Intent intents = new Intent(PlayerActivity.this,PopUpActivity.class);
                startActivity(intents);
                return (true);
            case R.id.exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                mp.stop();
                                finishAffinity();
                                System.exit(0);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                return (true);

        }

        return super.onOptionsItemSelected(item);
    }


    private String formattedMaxTime(int mlength) {
        String totalout;
        String totalnew;
        String seconds=String.valueOf(mlength%60);
        String minutes=String.valueOf(mlength/60);
        totalout=minutes + ":" + seconds;
        totalnew=minutes + ":" + "0" + seconds;
        if(seconds.length()==1){
            return totalnew;
        }else{
            return totalout;
        }
    }

    private String formattedTime(int mcurrent) {
        String totalout;
        String totalnew;
        String seconds=String.valueOf(mcurrent%60);
        String minutes=String.valueOf(mcurrent/60);
        totalout=minutes + ":" + seconds;
        totalnew=minutes + ":" + "0" + seconds;
        if(seconds.length()==1){
            return totalnew;
        }else{
           return totalout;
        }

    }

    private void changeSeekbar() {
        seekBar.setProgress(mp.getCurrentPosition());
        if(mp.isPlaying()){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekbar();
                }
            };
            handler.postDelayed(runnable,1000);
        }
    }


    public void next(View view) {
        repeatefunction();
            nextplay();

    }

    public void previous(View view) {
        repeatefunction();
        playprevious();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

        repeatefunction();
        nextplay();
        if (mp != null){
        mp.setOnCompletionListener(this);
    }

    }

    private void nextplay(){
        mp.stop();
        mp.release();
        repeatefunction();
            if (repeatboolean == false) {
                abc = (abc + 1);
                mp = MediaPlayer.create(this, audio[abc]);
                mp.start();
                seekBar.setProgress(0);
                seekBar.setMax(mp.getDuration());
                changeSeekbar();
                playpause.setImageResource(R.drawable.pause);
                imgview.setImageResource(imgs[abc]);
                audname.setText(names[abc]);
                auddesc.setText(desc[abc]);
                auddesc.setSelected(true);
                Intent intent = new Intent(this, MusicService.class);
                bindService(intent, this, BIND_AUTO_CREATE);
                mp.setOnCompletionListener(this);
            } else {
                mp = MediaPlayer.create(this, audio[abc]);
                mp.start();
                seekBar.setProgress(0);
                seekBar.setMax(mp.getDuration());
                changeSeekbar();
                playpause.setImageResource(R.drawable.pause);
                imgview.setImageResource(imgs[abc]);
                audname.setText(names[abc]);
                auddesc.setText(desc[abc]);
                auddesc.setSelected(true);
                Intent intent = new Intent(this, MusicService.class);
                bindService(intent, this, BIND_AUTO_CREATE);
                mp.setOnCompletionListener(this);
            }


    }

    private  void playprevious(){
        mp.stop();
        mp.release();
        repeatefunction();
        if(repeatboolean==false) {
            abc = (abc - 1);
            mp = MediaPlayer.create(this, audio[abc]);
            mp.start();
            seekBar.setProgress(0);
            seekBar.setMax(mp.getDuration());
            changeSeekbar();
            playpause.setImageResource(R.drawable.pause);
            imgview.setImageResource(imgs[abc]);
            audname.setText(names[abc]);
            auddesc.setText(desc[abc]);
            auddesc.setSelected(true);
            Intent intent = new Intent(this,MusicService.class);
            bindService(intent,this,BIND_AUTO_CREATE);
            mp.setOnCompletionListener(this);
        }else{
            mp = MediaPlayer.create(this,audio[abc]);
            mp.start();
            seekBar.setProgress(0);
            seekBar.setMax(mp.getDuration());
            changeSeekbar();
            playpause.setImageResource(R.drawable.pause);
            imgview.setImageResource(imgs[abc]);
            audname.setText(names[abc]);
            auddesc.setText(desc[abc]);
            auddesc.setSelected(true);
            Intent intent = new Intent(this,MusicService.class);
            bindService(intent,this,BIND_AUTO_CREATE);
            mp.setOnCompletionListener(this);
        }


    }

    public void settings(View view) {
        Intent intent = new Intent(PlayerActivity.this,PopUpActivity.class);
        intent.putExtra("posi",abc);
        startActivity(intent);
    }

    public void repeat(View view) {
        repeatefunction();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder=(MusicService.MyBinder) service;
        musicService = myBinder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    mp = null;
    }


    public  void repeatefunction()
    {
        if(repeatboolean==false){
            repeatboolean=true;
            Repeat.setImageResource(R.drawable.repeatone);
        }else{
            repeatboolean=false;
            Repeat.setImageResource(R.drawable.repeat);
        }
    }


    public void Player(){
        if(mp!=null){
            if(mp.isPlaying()) {
                mp = MediaPlayer.create(this,audio[abc]);
                mp.start();
                changeSeekbar();
                playpause.setImageResource(R.drawable.pause);
                mp.setOnCompletionListener(this);
                Intent intent = new Intent(this, MusicService.class);
                bindService(intent, this, BIND_AUTO_CREATE);
            }
                 }else {
                mp = MediaPlayer.create(this,audio[abc]);
                mp.start();
                changeSeekbar();
                playpause.setImageResource(R.drawable.pause);
                mp.setOnCompletionListener(this);
                Intent intent = new Intent(this,MusicService.class);
                bindService(intent,this,BIND_AUTO_CREATE);
        }
    }

    public void GetScreenWidthHeight(){

        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;

        height = displayMetrics.heightPixels;

    }

    public Bitmap SetBitmapSize(Bitmap bitmap1){

        return Bitmap.createScaledBitmap(bitmap1, width, height, false);

    }



    @Override
    public void onBackPressed() {
        mp.stop();
        PlayerActivity.super.onBackPressed();

    }


}