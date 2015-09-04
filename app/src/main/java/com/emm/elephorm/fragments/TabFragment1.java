package com.emm.elephorm.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.emm.elephorm.R;
import com.emm.elephorm.adapters.CustomListAdapter;
import com.emm.elephorm.models.Category;
import com.emm.elephorm.models.Formation;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment1 extends Fragment {
    private View v;
    private List<Formation> formationList = new ArrayList<>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);

        //listView = (ListView) v.findViewById(R.id.expandableHomeList);

        // Header View
        //View headerView = inflater.inflate(R.layout.list_header, null, false);
        //listView1.addHeaderView(headerView);

        //adapter = new CustomListAdapter((Activity) v.getContext(), formationList);
        //listView.setAdapter(adapter);

        //prepareListFormations();
        /*
        Formation.getFormation("E3760141112624", new Formation.getFormationCallback() {

            @Override
            public void onGetFinished(Formation formation) {
                Log.d("custom2", formation.getTitle());
            }

            @Override
            public void onGetFail(String error) {

            }
        });
        */

        return v;
    }


    protected List<Category> categories = new ArrayList<>();

    /*private void prepareListFormations() {

        Category.getCategoryList(true, new Category.updateCallback() {
            @Override
            public void onUpdateFinished(List<Category> cats) {
            categories = cats;
            for (int i = 0; i < categories.size(); i++) {

                Category obj = categories.get(i);
            }

            categories.get(0).getSubcategories().get(0).getFormationList(true, new Subcategory.updateCallback() {
                @Override
                public void onUpdateFinished(List<Formation> formations) {

                    for (int i = 0; i < formations.size(); i++) {

                        Formation obj = formations.get(i);

                        formationList.add(obj);

                        adapter.notifyDataSetChanged();
                    }
                }
            });

            adapter.notifyDataSetChanged();
            }
        });

    }*/

}
