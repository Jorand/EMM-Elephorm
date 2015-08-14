package com.emm.elephorm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.emm.elephorm.adapters.CustomListAdapter;
import com.emm.elephorm.models.Category;
import com.emm.elephorm.models.Formation;
import com.emm.elephorm.models.Subcategory;

import java.util.ArrayList;
import java.util.List;


public class FormationsActivity extends AppCompatActivity {

    private ListView listView;
    private CustomListAdapter listAdapter;
    private String SubcategoryId;

    private List<Formation> formationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formations);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Ftoolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();
        SubcategoryId = intent.getStringExtra("EXTRA_SUBCATEGORY_ID");

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setTitle(SubcategoryId);
        }

        listView = (ListView) findViewById(R.id.formations_list);

        listAdapter = new CustomListAdapter(this, formationList);
        listView.setAdapter(listAdapter);

        getListFormations();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formations, menu);
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

        if (id == R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected List<Category> categories = new ArrayList<>();

    private void getListFormations() {

        // TODO get formations

    }
}
