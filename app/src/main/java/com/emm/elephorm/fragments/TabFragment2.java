package com.emm.elephorm.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.emm.elephorm.FormationsListActivity;
import com.emm.elephorm.R;
import com.emm.elephorm.adapters.ExpandableListAdapter;
import com.emm.elephorm.models.Category;
import com.emm.elephorm.models.Formation;
import com.emm.elephorm.models.Subcategory;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ExpandableListView expListView;
    ArrayList<Category> listCategories = new ArrayList<>();

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

        listAdapter = new ExpandableListAdapter(v.getContext(), listCategories);
        expListView.setAdapter(listAdapter);
        //expListView.setDivider(null);

        swipeRefreshLayout.setOnRefreshListener(this);

        //swipeRefreshLayout.setEnabled(false);

        swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);

                    prepareListCategory(true);
                }
            }
        );

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Subcategory obj = listCategories.get(groupPosition).getSubcategories().get(childPosition);
                String subcategoryId = obj.getId();

                Intent intent = new Intent(getActivity(), FormationsListActivity.class);
                intent.putExtra("EXTRA_SUBCATEGORY_ID", subcategoryId);
                intent.putExtra("EXTRA_SUBCATEGORY_NAME", obj.getTitle());
                startActivity(intent);

                return false;
            }
        });

        return v;
    }

    @Override
    public void onRefresh() {
        prepareListCategory(false);
    }

    /**
     * Récupere la liste de catégories et sous-catégories, et actualise la liste
     */

    protected List<Category> categories = new ArrayList<>();

    private void prepareListCategory(Boolean update) {

        listCategories.clear();

        swipeRefreshLayout.setRefreshing(true);

        Category.getCategoryList(update, new Category.updateCallback() {
            @Override
            public void onUpdateFinished(List<Category> cats) {
                categories = cats;
                for (int i = 0; i < categories.size(); i++) {

                    Category obj = categories.get(i);
                    listCategories.add(obj);
                }

                listAdapter.notifyDataSetChanged();

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onUpdateFail(String error) {

                swipeRefreshLayout.setRefreshing(false);
                Toast toast = Toast.makeText(getActivity(), "ERREUR", Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

}
