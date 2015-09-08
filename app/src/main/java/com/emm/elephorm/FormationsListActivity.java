package com.emm.elephorm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.emm.elephorm.adapters.FormationListAdapter;
import com.emm.elephorm.models.Formation;

import java.util.ArrayList;
import java.util.List;


public class FormationsListActivity extends AppActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private FormationListAdapter listAdapter;
    private String subcategoryId;
    private List<Formation> formationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formations_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Ftoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();
        subcategoryId = intent.getStringExtra("EXTRA_SUBCATEGORY_ID");
        String subcategoryTitle = intent.getStringExtra("EXTRA_SUBCATEGORY_NAME");

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(subcategoryTitle);
        }

        // Gestion de la listview
        listView = (ListView) findViewById(R.id.formations_list);

        listAdapter = new FormationListAdapter(this, formationList);
        listView.setAdapter(listAdapter);

        // Gestion du clic sur un élément
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Formation formation = formationList.get(position);
                Intent intent = new Intent(FormationsListActivity.this, FormationActivity.class);
                String formationId = formation.getEan();
                intent.putExtra("EXTRA_FORMATION_ID", formationId);
                intent.putExtra("EXTRA_FORMATION_TITLE", formation.getTitle());
                startActivity(intent);
            }
        });

        // refresh en swipe
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.ColorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onResume(){
        super.onResume();
        getListFormations();
    }

    @Override
    public void onRefresh() {
        getListFormations();
    }

    /**
     * Récupère la liste des formations et l'affiche
     */
    private void getListFormations() {
        formationList.clear();

        Formation.getSubcategoryFormations(subcategoryId, new Formation.getFormationListCallback() {
            @Override
            public void onGetFinished(List<Formation> formations) {

                formationList.clear();

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
                Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }
}
