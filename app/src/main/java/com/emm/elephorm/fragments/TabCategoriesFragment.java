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
import com.emm.elephorm.adapters.CategoryExpandableListAdapter;
import com.emm.elephorm.models.Category;
import com.emm.elephorm.models.Subcategory;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabCategoriesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<Category> listCategories = new ArrayList<>();
    private CategoryExpandableListAdapter listAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tab_categories, container, false);

        // Liste extensible
        ExpandableListView expListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
        listAdapter = new CategoryExpandableListAdapter(v.getContext(), listCategories);
        expListView.setAdapter(listAdapter);

        // Clic sur un élément
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

        // Refresh en swipe
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.ColorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        prepareListCategory(false);
    }

    @Override
    public void onRefresh() {
        prepareListCategory(false);
    }

    /**
     * Récupere la liste de catégories et sous-catégories, et actualise la liste
     */
    private void prepareListCategory(Boolean update) {

        listCategories.clear();

        swipeRefreshLayout.setRefreshing(true);

        Category.getCategoryList(update, new Category.updateCallback() {
            @Override
            public void onUpdateFinished(List<Category> cats) {

                listCategories.clear();

                for (int i = 0; i < cats.size(); i++) {

                    Category obj = cats.get(i);
                    listCategories.add(obj);
                }

                listAdapter.notifyDataSetChanged();

                // stop swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onUpdateFail(String error) {
                swipeRefreshLayout.setRefreshing(false);

                Toast toast = Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

}
