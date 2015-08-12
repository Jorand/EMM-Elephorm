package com.emm.elephorm.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Inikaam on 12/08/2015.
 */
public class Subcategory {
    protected String id;
    protected String title;
    protected String description;
    protected boolean active;

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
}
