package com.emm.elephorm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.emm.elephorm.adapters.CustomListAdapter;
import com.emm.elephorm.models.Formation;

import java.util.ArrayList;
import java.util.List;


public class FormationsListActivity extends AppActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private CustomListAdapter listAdapter;
    private String SubcategoryId;

    private List<Formation> formationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formations_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Ftoolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();
        SubcategoryId = intent.getStringExtra("EXTRA_SUBCATEGORY_ID");
        String SubcategoryTitle = intent.getStringExtra("EXTRA_SUBCATEGORY_NAME");

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setTitle(SubcategoryTitle);
        }

        listView = (ListView) findViewById(R.id.formations_list);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.ColorPrimary);

        View headerView = getLayoutInflater().inflate(R.layout.list_header, null, false);

        //listView.addHeaderView(headerView);

        listAdapter = new CustomListAdapter(this, formationList);
        listView.setAdapter(listAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setEnabled(false);

        swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);

                    getListFormations();
                }
            }
        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Formation formation = formationList.get(position);
                Intent intent = new Intent(FormationsListActivity.this, FormationActivity.class);
                String formationId = formation.getEan();
                intent.putExtra("EXTRA_FORMATION_ID", formationId);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formations_list, menu);
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

       // if (id == R.id.home) {
        //    onBackPressed();
         //   return true;
       // }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        getListFormations();
    }

    private void getListFormations() {

        Formation.getSubcategoryFormations(SubcategoryId, new Formation.getFormationListCallback() {
            @Override
            public void onGetFinished(List<Formation> formations) {

                for (int i = 0; i < formations.size(); i++) {

                    Formation obj = formations.get(i);

                    formationList.add(obj);

                    listAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);

                }

            }

            @Override
            public void onGetFail(String error) {

                swipeRefreshLayout.setRefreshing(false);
                Toast toast = Toast.makeText(getApplicationContext(), "ERREUR", Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }
}
