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
import com.emm.elephorm.adapters.CustomListAdapter;
import com.emm.elephorm.app.ElephormApp;
import com.emm.elephorm.models.Formation;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment3 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View v;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout mEmptyViewContainer;
    private CustomListAdapter listAdapter;
    private List<Formation> formationList = new ArrayList<>();

    private List<Formation> currentFormationList = new ArrayList<>();
    private List<Formation> finishedFormationList = new ArrayList<>();
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab_fragment3, container, false);

        // SWIPE REFRESH
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        mEmptyViewContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout_emptyView);
        onCreateSwipeToRefresh(swipeRefreshLayout);
        onCreateSwipeToRefresh(mEmptyViewContainer);
        //swipeRefreshLayout.setEnabled(false);

        // INIT LISTVIEW
        listView = (ListView) v.findViewById(R.id.list);
        listView.setEmptyView(mEmptyViewContainer);
        listAdapter = new CustomListAdapter(getActivity(), formationList);
        listView.setAdapter(listAdapter);

        // EVENT LISTVIEW
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
        //Log.d("LOG", "t3 onResume");
        super.onResume();
        updateList();
    }

    @Override
    public void onRefresh() {
        // SWIPE REFRESH UPDATE
        updateList();
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

    private void updateList() {
        //Log.d("LOG", "updateList");

        formationList.clear();

        getFormations("current_formations");
        getFormations("finished_formations");
    }

    private void getFormations(String key) {
        //Log.d("LOG", "getFormations "+key);

        final String listType = key;

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String listString = preferences.getString(key, "");
        String[] list = listString.split(";");

        final int listLength = list.length;

        if (list.length > 1) {

            currentFormationList.clear();
            finishedFormationList.clear();

            for (String FormationId : list) {

                if (!FormationId.isEmpty()) {

                    mEmptyViewContainer.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(true);
                    mEmptyViewContainer.setRefreshing(true);

                    Formation.getFormation(FormationId, new Formation.getFormationCallback() {
                        @Override
                        public void onGetFinished(Formation formation) {

                            switch (listType) {
                                case "current_formations":
                                    currentFormationList.add(formation);
                                    break;
                                case "finished_formations":
                                    finishedFormationList.add(formation);
                                    break;
                                default:
                                    formationList.add(formation);
                                    break;
                            }

                            formationList.add(formation);

                            updateAdapter(listLength);
                        }

                        @Override
                        public void onGetFail(String error) {
                            updateAdapter(listLength);
                            Toast toast = Toast.makeText(ElephormApp.getInstance().getBaseContext(), error, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
                else {
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
