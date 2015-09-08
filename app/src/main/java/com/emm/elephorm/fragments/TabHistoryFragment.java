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
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.emm.elephorm.FormationActivity;
import com.emm.elephorm.R;
import com.emm.elephorm.adapters.FormationExpandableListAdapter;
import com.emm.elephorm.app.ElephormApp;
import com.emm.elephorm.models.Formation;
import com.emm.elephorm.models.TitleList;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabHistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View v;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout mEmptyViewContainer;
    private List<Formation> formationList = new ArrayList<>();

    private List<Formation> currentFormationList = new ArrayList<>();
    private List<Formation> finishedFormationList = new ArrayList<>();

    private FormationExpandableListAdapter titleListAdapter;
    private ArrayList<TitleList> titleLists = new ArrayList<>();

    private int current = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_tab_history, container, false);

        // refresh en swipe
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        mEmptyViewContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout_emptyView);
        onCreateSwipeToRefresh(swipeRefreshLayout);
        onCreateSwipeToRefresh(mEmptyViewContainer);

        // Liste extensible
        ExpandableListView expListView = (ExpandableListView) v.findViewById(R.id.historyExpandableList);
        titleListAdapter = new FormationExpandableListAdapter(v.getContext(), titleLists);
        expListView.setAdapter(titleListAdapter);

        // Clic sur un élément
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

    /**
     * Rafraichit le layout
     * @param refreshLayout
     */
    private void onCreateSwipeToRefresh(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setColorSchemeResources(R.color.ColorPrimary);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateList();
    }

    @Override
    public void onRefresh() {
        updateList();
    }

    /**
     * Met à jour l'adapter
     * @param lengh longueur de la liste
     */
    private void updateAdapter(int lengh) {

        current++;

        if (current >= lengh) {

            titleLists.clear();

            if (currentFormationList.size() > 0) {
                TitleList newsList = new TitleList("En cours", currentFormationList);
                titleLists.add(newsList);
            }

            if (finishedFormationList.size() > 0) {
                TitleList recommendedList = new TitleList("Terminées", finishedFormationList);
                titleLists.add(recommendedList);
            }

            titleListAdapter.notifyDataSetChanged();

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

    /**
     * Met à jour les listes à partir des préférences
     */
    private void updateList() {
        formationList.clear();

        getFormations("current_formations");
        getFormations("finished_formations");
    }

    /**
     * Récupère la liste de formations dans les préférences
     * @param key nom de la liste
     */
    private void getFormations(String key) {
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
            titleListAdapter.notifyDataSetChanged();
        }
    }

}
