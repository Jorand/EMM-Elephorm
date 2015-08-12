package com.emm.elephorm.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.emm.elephorm.R;
import com.emm.elephorm.adapters.ExpandableListAdapter;
import com.emm.elephorm.app.ElephormApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private String TAG = TabFragment2.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private ExpandableListAdapter listAdapter;

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab_fragment2, container, false);

        expListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

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

    private void prepareListCategory() {

        swipeRefreshLayout.setRefreshing(true);

        JsonArrayRequest req = new JsonArrayRequest("http://eas.elephorm.com/api/v1/categories",
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, response.toString());

                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);

                                String id = obj.getString("_id");
                                String title = obj.getString("title");

                                listDataHeader.add(title);

                                List<String> subCatList = new ArrayList<>();
                                JSONArray subCat = obj.getJSONArray("subcategories");

                                for (int ii = 0; ii < subCat.length(); ii++) {

                                    JSONObject subObj = subCat.getJSONObject(ii);
                                    String subTitle = subObj.getString("title");

                                    subCatList.add(subTitle);
                                }

                                listDataChild.put(listDataHeader.get(i), subCatList);

                            } catch (JSONException e) {
                                Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                            }
                        }
                        listAdapter.notifyDataSetChanged();
                    }

                    // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server Error: " + error.getMessage());

                Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        ElephormApp.getInstance().addToRequestQueue(req);
    }


}
