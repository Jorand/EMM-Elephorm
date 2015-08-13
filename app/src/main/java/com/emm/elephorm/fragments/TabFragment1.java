package com.emm.elephorm.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.emm.elephorm.R;
import com.emm.elephorm.adapters.CustomListAdapter;
import com.emm.elephorm.models.Category;
import com.emm.elephorm.models.Formation;
import com.emm.elephorm.models.Subcategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment1 extends Fragment {
    private View v;
    private List<Formation> formationList = new ArrayList<>();
    private ListView listView1;
    private CustomListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);

        listView1 = (ListView) v.findViewById(R.id.home_list_1);

        // Header View
        View headerView = inflater.inflate(R.layout.home_list_header, null, false);
        listView1.addHeaderView(headerView);

        adapter = new CustomListAdapter((Activity) v.getContext(), formationList);
        listView1.setAdapter(adapter);

        ListView listView2 = (ListView) v.findViewById(R.id.home_list_2);

        listView2.addHeaderView(headerView);

        adapter = new CustomListAdapter((Activity) v.getContext(), formationList);
        listView2.setAdapter(adapter);

        /*prepareListFormations();*/

        return v;
    }

    /*private Formation f = new Formation();
    private Formation f1 = new Formation();

    private void prepareListFormations() {

        f.setTitle("Giant Water Balloons and Fireworks - The Slow-Mo-guy in lol");
        f.setSubtitle("SQUEEZIE");
        f.setDescription("Angular JS");
        f.setImages("http://api.androidhive.info/json/movies/1.jpg");

        formationList.add(f);

        f1.setTitle("TITRE EN CAP");
        f1.setSubtitle("Experiemnt");
        f1.setDescription("Java Android");
        f1.setImages("http://api.androidhive.info/json/movies/2.jpg");

        formationList.add(f1);

        adapter.notifyDataSetChanged();


    }*/

}
