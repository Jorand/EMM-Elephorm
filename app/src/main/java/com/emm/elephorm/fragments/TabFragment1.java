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
import com.emm.elephorm.models.Category;
import com.emm.elephorm.models.Formation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View v;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private CustomListAdapter listAdapter;

    private List<Formation> formationList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);

        listView = (ListView) v.findViewById(R.id.homeList);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.ColorPrimary);


        listAdapter = new CustomListAdapter(getActivity(), formationList);
        listView.setAdapter(listAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

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
                Intent intent = new Intent(getActivity(), FormationActivity.class);
                String formationId = formation.getEan();
                intent.putExtra("EXTRA_FORMATION_ID", formationId);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onRefresh() {
        getListFormations();
    }

    private void getListFormations() {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String listString = preferences.getString("recommended_categories", "");
        String[] list = listString.split(";");

        Toast toast = Toast.makeText(getActivity(), String.valueOf(list.length), Toast.LENGTH_LONG);
        toast.show();

        if (list.length > 1) {

            for (int i = 0; i < list.length; i++) {

                try {
                    JSONObject video = new JSONObject(list[i]);
                    String id = video.getString("_id");

                    Formation.getSubcategoryFormations(id, new Formation.getFormationListCallback() {
                        @Override
                        public void onGetFinished(List<Formation> formations) {

                            for (int i = 0; i < formations.size(); i++) {

                                Formation obj = formations.get(i);
                                if (obj.getProgress() == 0) {
                                    formationList.add(obj);
                                }
                            }

                            listAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onGetFail(String error) {

                            swipeRefreshLayout.setRefreshing(false);
                            Toast toast = Toast.makeText(getActivity(), "ERREUR", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
