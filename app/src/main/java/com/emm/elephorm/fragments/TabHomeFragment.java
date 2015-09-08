package com.emm.elephorm.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.emm.elephorm.FormationActivity;
import com.emm.elephorm.FormationsListActivity;
import com.emm.elephorm.R;
import com.emm.elephorm.adapters.CategoryExpandableListAdapter;
import com.emm.elephorm.adapters.FormationExpandableListAdapter;
import com.emm.elephorm.adapters.FormationListAdapter;
import com.emm.elephorm.app.ElephormApp;
import com.emm.elephorm.models.Category;
import com.emm.elephorm.models.Formation;
import com.emm.elephorm.models.Subcategory;
import com.emm.elephorm.models.TitleList;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    
    private View v;
    private FormationListAdapter listAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout mEmptyViewContainer;
    private List<Formation> formationList = new ArrayList<>();

    private FormationExpandableListAdapter titleListAdapter;
    private ArrayList<TitleList> titleLists = new ArrayList<>();

    ArrayList<Formation> firstFormations = new ArrayList<>();
    ArrayList<Formation> newFormations = new ArrayList<>();
    ArrayList<Formation> newFirstFormations = new ArrayList<>();

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
        /*

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
        */

        // INIT EXPENDABLELIST
        ExpandableListView expListView = (ExpandableListView) v.findViewById(R.id.homeExpandableFormationsList);
        //expListView.setDivider(null);

        titleListAdapter = new FormationExpandableListAdapter(v.getContext(), titleLists);
        expListView.setAdapter(titleListAdapter);

        // EXPENDABLELIST EVENT
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Formation formation = titleLists.get(groupPosition).getFormations().get(childPosition);
                Intent intent = new Intent(getActivity(), FormationActivity.class);
                String formationId = formation.getEan();
                intent.putExtra("EXTRA_FORMATION_ID", formationId);
                intent.putExtra("EXTRA_FORMATION_TITLE", formation.getTitle());
                startActivity(intent);

                return false;
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
        //Log.d("LOG", "t1 onRefresh");
        getListFormations();
    }

    private int current = 0;

    private void updateAdapter(int lengh) {

        current++;

        if (current >= lengh) {

            newFirstFormations.clear();

            titleLists.clear();

            if (newFormations.size() > 0) {

                newFirstFormations.add(newFormations.get(1));
                newFirstFormations.add(newFormations.get(2));
                newFirstFormations.add(newFormations.get(3));

                TitleList newsList = new TitleList("Nouveautés", newFirstFormations);
                titleLists.add(newsList);
            }

            firstFormations.clear();

            if (formationList.size() > 0) {

                Random r;
                int index;
                ArrayList<Integer> ids = new ArrayList<>();

                while (firstFormations.size() < 3 && firstFormations.size() < formationList.size()) {

                    r = new Random();
                    index = r.nextInt(formationList.size());

                    if (!ids.contains(index)) {
                        firstFormations.add(formationList.get(index));
                        ids.add(index);
                    }
                }

                TitleList recommendedList = new TitleList("Recommandé pour vous", firstFormations);
                titleLists.add(recommendedList);
            }

            titleListAdapter.notifyDataSetChanged();

            swipeRefreshLayout.setRefreshing(false);
            mEmptyViewContainer.setRefreshing(false);

            if (formationList.size() > 0 || newFormations.size() > 0) {
                mEmptyViewContainer.setVisibility(View.GONE);
            } else {
                mEmptyViewContainer.setVisibility(View.VISIBLE);
            }
            current = 0;
        }
    }
    
    private void getListFormations() {
        //Log.d("LOG", "getListFormations");

        newFormations.clear();
        formationList.clear();
        swipeRefreshLayout.setRefreshing(true);
        mEmptyViewContainer.setRefreshing(true);
        mEmptyViewContainer.setVisibility(View.GONE);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String listString = preferences.getString("recommended_categories", "");
        String[] list = listString.split(";");

        final int listLength = list.length <= 1 ? 1 : list.length + 1;

        if (list.length > 1) {

            for (String id : list) {

                if (!id.isEmpty()) {

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
            titleListAdapter.notifyDataSetChanged();
        }

        Formation.getSubcategoryFormations("55ee4121ab734aeb0a4781d3", new Formation.getFormationListCallback() {
            @Override
            public void onGetFinished(List<Formation> formations) {

                for (int i = 0; i < formations.size(); i++) {

                    Formation obj = formations.get(i);

                    if (obj.getProgress() <= 0) {
                        newFormations.add(obj);
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
    }


}
