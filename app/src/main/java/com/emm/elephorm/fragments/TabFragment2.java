package com.emm.elephorm.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.emm.elephorm.R;
import com.emm.elephorm.adapters.ExpandableListAdapter;
import com.emm.elephorm.models.Category;
import com.emm.elephorm.models.Subcategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<String>> listDataChild = new HashMap<>();

    private String TAG = TabFragment2.class.getSimpleName(); // A utiliser pour filter les log | TODO ajouter en global

    private SwipeRefreshLayout swipeRefreshLayout;
    private ExpandableListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_fragment2, container, false);

        expListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.ColorPrimary);

        listAdapter = new ExpandableListAdapter(v.getContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        //expListView.setDivider(null);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);

                    prepareListCategory();
                }
            }
        );

        return v;
    }

    @Override
    public void onRefresh() {
        prepareListCategory();
    }

    /**
     * Récupere la liste de catégories et sous-catégories, et actualise la liste
     */

    protected List<Category> categories = new ArrayList<>();
    protected List<Subcategory> subcategory = new ArrayList<>();

    private void prepareListCategory() {

        listDataHeader.clear();
        listDataChild.clear();

        swipeRefreshLayout.setRefreshing(true);

        Category.getCategoryList(true, new Category.updateCallback() {
            @Override
            public void onUpdateFinished(List<Category> cats) {
                categories = cats;
                for (int i = 0; i < categories.size(); i++) {

                    Category obj = categories.get(i);

                    String id = obj.getId();
                    String title = obj.getTitle();

                    listDataHeader.add(title);

                    subcategory = categories.get(i).getSubcategories();
                    List<String> subCatList = new ArrayList<>();

                    for (int j = 0; j < subcategory.size(); j++) {

                        Subcategory subObj = subcategory.get(j);
                        String subTitle = subObj.getTitle();

                        subCatList.add(subTitle);
                    }

                    listDataChild.put(listDataHeader.get(i), subCatList);
                }

                listAdapter.notifyDataSetChanged();

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

}
