package com.emm.elephorm;

import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.emm.elephorm.adapters.CustomListAdapter;
import com.emm.elephorm.app.ElephormApp;
import com.emm.elephorm.models.Formation;
import com.emm.elephorm.models.Lesson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FormationActivity extends AppActivity {

    VideoView video_player_view;
    DisplayMetrics dm;
    MediaController media_Controller;

    private String FormationId;
    private Formation myFormation;

    ImageLoader imageLoader = ElephormApp.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setTitle("Formation");
        }

        Intent intent = getIntent();
        FormationId = intent.getStringExtra("EXTRA_FORMATION_ID");

        getFormation();

        //getInit();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getInit() {
        //video_player_view = (VideoView) findViewById(R.id.video_player_view);
        media_Controller = new MediaController(this);
        dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        video_player_view.setMinimumWidth(width);
        //video_player_view.setMinimumHeight(height);
        video_player_view.setMediaController(media_Controller);
        video_player_view.setVideoPath("http://eas.elephorm.com/videos/tuto-cubase-7-les-nouveautes/presentation-de-cubase-7");
        video_player_view.start();
    }

    private void getFormation() {

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

                List<Lesson> items = myFormation.getItems();

                for (int i = 0; i < items.size(); i++) {

                    final Lesson obj = items.get(i);

                    TextView item = new TextView(getApplicationContext());

                    if (obj.getType().equals("video")) {

                        item.setText("Video - "+obj.getTitle());
                    }
                    else {
                        item.setText(obj.getTitle());
                    }

                    item.setText(String.valueOf(obj.getFloor()));

                    int indent = 10 * obj.getFloor();

                    item.setPadding(10, 10, 10, indent);
                    item.setTextColor(getResources().getColor(R.color.primary_dark_material_dark));

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
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //intent.putExtra("url", url);
                                //startActivity(intent);
                            }
                            else {

                                Intent intent = new Intent(FormationActivity.this, LessonsActivity.class);
                                String id = obj.getId();
                                intent.putExtra("EXTRA_LESSON_ID", id);
                                startActivity(intent);
                            }

                        }

                    });

                }


            }

            @Override
            public void onGetFail(String error) {

                Toast toast = Toast.makeText(getApplicationContext(), "ERREUR", Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }
}
