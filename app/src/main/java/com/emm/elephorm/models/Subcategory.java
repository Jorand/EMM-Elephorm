package com.emm.elephorm.models;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.emm.elephorm.R;
import com.emm.elephorm.app.ElephormApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Inikaam on 12/08/2015.
 */
public class Subcategory {
    protected String id;
    protected String title;
    protected String description;
    protected boolean active;
    protected List<Formation> formations = new ArrayList<Formation>();

    /**
     * Initialise la sous-catégorie avec un objet JSON
     * @param data : données de la sous-catégorie
     */
    public Subcategory(JSONObject data) {
        try {
            id = data.getString("_id");
            title = data.getString("title");
            description = data.getString("description");
            active = Boolean.parseBoolean(data.getString("active"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //define callback interface
    public interface updateCallback {
        void onUpdateFinished(List<Formation> formations);
        void onUpdateFail(String error);
    }

    /**
     * Renvoie le tableau des formations de la sous-catégorie
     * @param update : true s'il faut mettre les données des formations à jour, false sinon
     * @return
     */
    public void getFormationList(boolean update, updateCallback cb) {
        final updateCallback callback = cb;

        // Test de connexion
        ConnectivityManager cm =
                (ConnectivityManager)ElephormApp.getInstance().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            callback.onUpdateFail(ElephormApp.getInstance().getString(R.string.global_connexion_error));
        } else if(update || formations.size() == 0) {
            formations.clear();
            JsonArrayRequest request = new JsonArrayRequest("http://eas.elephorm.com/api/v1/subcategories/" + id + "/trainings",
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            formations.clear();
                            JSONObject obj = null;
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    obj = response.getJSONObject(i);
                                    formations.add(new Formation(obj));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            callback.onUpdateFinished(formations);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callback.onUpdateFail(ElephormApp.getInstance().getString(R.string.global_volley_error));
                            throw new Error(error.toString());
                        }
                    }
            );
            ElephormApp.getInstance().getRequestQueue().add(request);
        } else {
            callback.onUpdateFinished(formations);
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

    public boolean isActive() {
        return active;
    }
    
}
