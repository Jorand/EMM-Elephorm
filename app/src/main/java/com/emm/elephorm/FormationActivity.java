package com.emm.elephorm;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.emm.elephorm.app.ElephormApp;
import com.emm.elephorm.models.Formation;
import com.emm.elephorm.models.Lesson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FormationActivity extends AppActivity {

    VideoView video_player_view;
    DisplayMetrics dm;
    MediaController media_Controller;

    private String FormationId;
    private Formation myFormation;

    ImageLoader imageLoader = ElephormApp.getInstance().getImageLoader();
    private ProgressBar progress;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);

        Intent intent = getIntent();
        FormationId = intent.getStringExtra("EXTRA_FORMATION_ID");
        String FormationTitle = intent.getStringExtra("EXTRA_FORMATION_TITLE");

        if (FormationTitle.isEmpty())
            FormationTitle = "Formation";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setTitle(FormationTitle);
        }

        progress = (ProgressBar) findViewById(R.id.progress);
        Drawable draw = getDrawable(R.drawable.custom_progressbar);
        progress.setProgressDrawable(draw);

        getFormation();

        NetworkImageView poster = (NetworkImageView) findViewById(R.id.poster);
        poster.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                String url = myFormation.getTeaser();
                intent.putExtra("url", "http://videos.elephorm.com/"+url+"/video");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume(){
        Log.d("LOG", "formation onResume");
        getFormation();
        super.onResume();

    }

    private void getFormation() {

        myFormation = null;

        Formation.getFormation(FormationId, new Formation.getFormationCallback() {
            @Override
            public void onGetFinished(Formation formation) {

                myFormation = formation;

                if (imageLoader == null)
                    imageLoader = ElephormApp.getInstance().getImageLoader();
                NetworkImageView poster = (NetworkImageView) findViewById(R.id.poster);

                poster.setImageUrl(myFormation.getPoster(), imageLoader);

                TextView titre = (TextView) findViewById(R.id.title);
                titre.setText(myFormation.getTitle());

                TextView subTitle = (TextView) findViewById(R.id.subtitle);
                subTitle.setText(myFormation.getSubtitle());

                TextView description = (TextView) findViewById(R.id.description);
                description.setText(Html.fromHtml(myFormation.getDescription()));

                TextView price = (TextView) findViewById(R.id.price);
                price.setText(myFormation.getPrice());

                TextView duration = (TextView) findViewById(R.id.duration);
                duration.setText(myFormation.getDuration());

                List<Lesson> items = myFormation.getLessonList(0, myFormation.getItems());

                TextView countLessons = (TextView) findViewById(R.id.count_items);
                countLessons.setText(items.size() +" leçons");

                int viewedVideo = 0;
                int video = 0;

                for (int i = 0; i < items.size(); i++) {

                    final Lesson obj = items.get(i);

                    TextView item = new TextView(getApplicationContext());

                    //Log.d("LOG", String.valueOf(obj.getFloor()));

                    //int indent = 10 * obj.getFloor();

                    if (obj.getType().equals("chapter")) {
                        item.setPadding(0, 30, 0, 10);
                        item.setTypeface(null, Typeface.BOLD);
                        item.setTextSize(14);
                    }
                    else {
                        item.setPadding(30, 16, 0, 16);
                    }

                    item.setTextColor(getResources().getColor(R.color.ColorTxt));

                    if (obj.getType().equals("video")) {

                        item.setText(obj.getTitle());
                        item.setClickable(true);
                        //item.setFocusableInTouchMode(true);
                        item.setBackgroundResource(R.drawable.label_bg);

                        if (obj.isViewed()) {
                            item.setTextColor(getResources().getColor(R.color.ColorGey));

                            viewedVideo++;
                        }

                        video++;

                    }
                    else {
                        item.setText(obj.getTitle());
                    }

                    LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
                    layout.addView(item);

                    item.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (obj.getType().equals("video")) {

                                Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                                try {
                                    JSONObject video = new JSONObject(obj.getVideo());
                                    String url = video.getString("filepath");
                                    intent.putExtra("url", url);
                                    obj.setViewed();
                                    myFormation.updateProgress();
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {

                                //Intent intent = new Intent(FormationActivity.this, LessonsActivity.class);
                                //String id = obj.getId();
                                //intent.putExtra("EXTRA_LESSON_ID", id);
                                //startActivity(intent);
                            }

                        }

                    });

                }

                progress.setProgress(Math.round(myFormation.getProgress()));
                TextView progressText = (TextView) findViewById(R.id.progress_text);

                progressText.setText(viewedVideo+"/"+video+" ("+Math.round(myFormation.getProgress())+"%)");



            }

            @Override
            public void onGetFail(String error) {

                Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }
}
