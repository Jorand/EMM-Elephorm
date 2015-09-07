package com.emm.elephorm.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.emm.elephorm.FormationActivity;
import com.emm.elephorm.R;
import com.emm.elephorm.adapters.FormationListAdapter;
import com.emm.elephorm.app.ElephormApp;
import com.emm.elephorm.models.Formation;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    
    private View v;
    private FormationListAdapter listAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout mEmptyViewContainer;
    private List<Formation> formationList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab_home, container, false);

        // SWIPE REFRESH
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        mEmptyViewContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout_emptyView);
        onCreateSwipeToRefresh(swipeRefreshLayout);
        onCreateSwipeToRefresh(mEmptyViewContainer);
        //swipeRefreshLayout.setEnabled(false);
        
        // INIT LISTVIEW
        ListView listView = (ListView) v.findViewById(R.id.homeList);
        listView.setEmptyView(mEmptyViewContainer);
        listAdapter = new FormationListAdapter(getActivity(), formationList);
        listView.setAdapter(listAdapter);

        //ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_header, listView, false);
        //TextView headerTitle = (TextView) header.findViewById(R.id.headerTitle);
        //headerTitle.setText("Nouveautés");
        //listView.addHeaderView(header, null, false);

        // LISTVIEW EVENT
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Formation formation = formationList.get(position);
                Intent intent = new Intent(getActivity(), FormationActivity.class);
                String formationId = formation.getEan();
                intent.putExtra("EXTRA_FORMATION_ID", formationId);
                intent.putExtra("EXTRA_FORMATION_TITLE", formation.getTitle());
                startActivity(intent);
            }
        });

        return v;
    }

    private void onCreateSwipeToRefresh(SwipeRefreshLayout refreshLayout) {
        // INIT SwipeRefreshLayout
        refreshLayout.setColorSchemeResources(R.color.ColorPrimary);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume(){
        // CREATE RESUME UPDATE
        //Log.d("LOG", "t1 onResume");
        super.onResume();
        getListFormations();
    }

    @Override
    public void onRefresh() {
        // SWIPE REFRESH UPDATE
        getListFormations();
    }

    private int current = 0;

    private void updateAdapter(int lengh) {

        current++;

        if (current >= lengh) {

            listAdapter.notifyDataSetChanged();

            swipeRefreshLayout.setRefreshing(false);
            mEmptyViewContainer.setRefreshing(false);

            if (formationList.size() > 0) {
                mEmptyViewContainer.setVisibility(View.GONE);
            } else {
                mEmptyViewContainer.setVisibility(View.VISIBLE);
            }
            current = 0;
        }
    }
    
    private void getListFormations() {
        //Log.d("LOG", "getListFormations");

        formationList.clear();
        swipeRefreshLayout.setRefreshing(true);
        mEmptyViewContainer.setRefreshing(true);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String listString = preferences.getString("recommended_categories", "");
        String[] list = listString.split(";");

        final int listLength = list.length;

        if (list.length > 1) {

            for (String id : list) {

                if (!id.isEmpty()) {

                    mEmptyViewContainer.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(true);
                    mEmptyViewContainer.setRefreshing(true);

                    Formation.getSubcategoryFormations(id, new Formation.getFormationListCallback() {
                        @Override
                        public void onGetFinished(List<Formation> formations) {

                            for (int i = 0; i < formations.size(); i++) {

                                Formation obj = formations.get(i);

                                if (obj.getProgress() <= 0) {
                                    formationList.add(obj);
                                }
                            }
                            updateAdapter(listLength);
                        }

                        @Override
                        public void onGetFail(String error) {

                            updateAdapter(listLength);

                            Toast toast = Toast.makeText(ElephormApp.getInstance().getBaseContext(), error, Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    });

                } else {
                    updateAdapter(listLength);
                }
            }
        }
        else {
            swipeRefreshLayout.setRefreshing(false);
            mEmptyViewContainer.setRefreshing(false);
            listAdapter.notifyDataSetChanged();
        }
    }


}
