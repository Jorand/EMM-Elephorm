package com.emm.elephorm;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;

import android.os.Bundle;

import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity implements MediaPlayer.OnCompletionListener {

    private VideoView v;
    private int stopPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);

        v = (VideoView) findViewById(R.id.myvideoview);

        String url = null;
        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString("url");

            if (url != null) {
                v.setMediaController(new MediaController(this));
                v.setOnCompletionListener(this);
                v.setVideoURI(Uri.parse(url));
                v.start();
            }
        }

        if (url == null) {
            throw new IllegalArgumentException("Must set url extra paremeter in intent.");
        }
    }

    @Override
    public void onCompletion(MediaPlayer v) {
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPosition = v.getCurrentPosition();
        v.pause();
    }
    @Override
    public void onResume() {
        super.onResume();
        //v.seekTo(stopPosition);
        //v.start();
        v.resume();
    }

}