package com.emm.elephorm.models;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.emm.elephorm.app.ElephormApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Inikaam on 12/08/2015.
 */
public class Category {
    public static List<Category> categories = new ArrayList<Category>();
    protected String id;
    protected String title;
    protected String description;
    protected List<Subcategory> subcategories = new ArrayList<Subcategory>();

    /**
     * Initialise la catégorie avec un objet JSON
     * génère aussi les sous-catégories
     * @param data : données de la catégorie
     */
    public Category(JSONObject data) {
        try {
            id = data.getString("_id");
            title = data.getString("title");
            description = data.getString("description");
            JSONArray subs = new JSONArray(data.getString("subcategories"));
            JSONObject obj = null;
            for(int i = 0;i < subs.length();i++) {
                obj = subs.getJSONObject(i);
                subcategories.add(new Subcategory(obj));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renvoie le tableau des catégories et sous-catégories actuel
     * @param update : true s'il faut mettre les données des catégories à jour, false sinon
     * @return
     */
    public static List<Category> getCategoryList(boolean update) {
        if(update) {
            categories.clear();
            JsonArrayRequest request = new JsonArrayRequest("http://eas.elephorm.com/api/v1/categories",
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JSONObject obj = null;
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    obj = response.getJSONObject(i);
                                    categories.add(new Category(obj));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );
            ElephormApp.getInstance().getRequestQueue().add(request);
        }

        return categories;
    }

    /** Getters **/

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }
}
