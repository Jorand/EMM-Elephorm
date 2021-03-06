package com.emm.elephorm.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.emm.elephorm.R;
import com.emm.elephorm.app.ElephormApp;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Category {
    public static List<Category> categories = new ArrayList<>();
    protected String id;
    protected String title;
    protected String description;
    protected List<Subcategory> subcategories = new ArrayList<>();

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
            JSONObject obj;
            for(int i = 0;i < subs.length();i++) {
                obj = subs.getJSONObject(i);
                subcategories.add(new Subcategory(obj));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //define callback interface
    public interface updateCallback {
        void onUpdateFinished(List<Category> categories);
        void onUpdateFail(String error);
    }

    /**
     * Renvoie le tableau des catégories et sous-catégories actuel
     * @param update : true s'il faut mettre les données des catégories à jour, false sinon
     */
    public static void getCategoryList(boolean update, updateCallback cb) {
        final updateCallback callback = cb;
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ElephormApp.getInstance().getBaseContext());

        // Test de connexion
        ConnectivityManager cm =
                (ConnectivityManager)ElephormApp.getInstance().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(update && (activeNetwork == null || !activeNetwork.isConnectedOrConnecting())) {
            callback.onUpdateFail(ElephormApp.getInstance().getString(R.string.global_connexion_error));
        } else if(update || categories.size() == 0) {
            JsonArrayRequest request = new JsonArrayRequest("http://eas.elephorm.com/api/v1/categories",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        categories.clear();
                        JSONObject obj;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                obj = response.getJSONObject(i);
                                categories.add(new Category(obj));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // stockage
                        SharedPreferences.Editor editor = preferences.edit();
                        Gson gson = new Gson();
                        editor.putString("categories", gson.toJson(categories));

                        editor.apply();

                        callback.onUpdateFinished(categories);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(preferences.contains("categories")) {
                            try {
                                JSONArray list = new JSONArray(preferences.getString("categories", "[]"));
                                categories.clear();
                                JSONObject obj;
                                for (int i = 0; i < list.length(); i++) {
                                    obj = list.getJSONObject(i);
                                    Gson gson = new Gson();
                                    categories.add(gson.fromJson(obj.toString(), Category.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(categories.size() == 0)
                            callback.onUpdateFail(ElephormApp.getInstance().getString(R.string.global_volley_error));
                        else
                            callback.onUpdateFinished(categories);
                    }
                }
            );
            ElephormApp.getInstance().getRequestQueue().add(request);
        } else {
            callback.onUpdateFinished(categories);
        }
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

    public static List<Category> getCategories() {
        return categories;
    }

    public static void setCategories(List<Category> categories) {
        Category.categories = categories;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }
}
