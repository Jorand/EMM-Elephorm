package com.emm.elephorm;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.emm.elephorm.adapters.CustomListAdapter;
import com.emm.elephorm.models.Formation;

import java.util.ArrayList;
import java.util.List;

public class FormationActivity extends AppCompatActivity {

    private String FormationId;

    private Formation myFormation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();
        FormationId = intent.getStringExtra("EXTRA_FORMATION_ID");

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setTitle("Formation");
        }

        getFormation();
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

        //if (id == R.id.home) {
        //    onBackPressed();
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    private void getFormation() {

        Formation.getFormation(FormationId, new Formation.getFormationCallback() {
            @Override
            public void onGetFinished(Formation formation) {

                myFormation = formation;

                TextView title = (TextView) findViewById(R.id.TitleFormation);

               /* Log.d("TAG", formation.getTitle());*/

                title.setText(formation.getTitle());

                //formationAdapter.notifyDataSetChanged();

            }

            @Override
            public void onGetFail(String error) {

            }
        });

    }
}
