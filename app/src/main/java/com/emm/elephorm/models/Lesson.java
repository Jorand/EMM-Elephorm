package com.emm.elephorm.models;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.emm.elephorm.app.ElephormApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Inikaam on 11/08/2015.
 */
public class Lesson {
    protected String id;
    protected String type;
    protected String title;
    protected String video;
    protected String duration;
    protected String poster;
    protected boolean free;
    protected boolean viewed;
    protected List<Lesson> items = new ArrayList<Lesson>();
    private static final String ID_KEY = "LESSON_";

    public Lesson(JSONObject data) {
        try {
            id = data.getString("_id");
            type = data.getString("type");
            title = data.getString("title");
            free = Boolean.parseBoolean(data.getString("free"));
            if(data.has("duration"))
                duration = formatDuration((int) Double.parseDouble(data.getString("duration")));
            if(data.has("field_poster"))
                poster = data.getString("field_poster");
            if(data.has("items")) {
                JSONArray items = new JSONArray(data.getString("items"));

                this.items = Lesson.getLessonList(items);
            }

            JSONArray fieldVideo = new JSONArray(data.getString("field_video"));
            if(fieldVideo.length() > 0) {
                video = fieldVideo.getString(0);
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ElephormApp.getInstance().getBaseContext());
            viewed = preferences.getBoolean(ID_KEY + id, false);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convertit la durée au format HH:mm:ss
     * @return durée convertie
     */
    protected String formatDuration(int duration) {
        int h = duration / 3600;
        int m = (duration % 3600) / 60;
        int s = (duration % 3600) % 60;

        return timeItemToString(h) + ":"
                + timeItemToString(m) + ":"
                + timeItemToString(s);
    }

    /**
     * Renvoie la valeur au format 2 chiffres mini
     * @param timeItem : valeur (heure, minute ou seconde)
     * @return valeur à 2 chiffres
     */
    protected String timeItemToString(int timeItem) {
        String stringItem;
        if(timeItem < 10 )
            stringItem = "0" + timeItem;
        else
            stringItem = "" + timeItem;

        return stringItem;
    }

    public void setViewed() {
        if(viewed)
            return;

        viewed = true;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ElephormApp.getInstance().getBaseContext());
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(ID_KEY + id, viewed);
    }

    /** STATICS **/

    public static List<Lesson> getLessonList(JSONArray data) {
        List<Lesson> lessons = new ArrayList<Lesson>();
        for(int i = 0;i<data.length();i++) {
            try {
                lessons.add(new Lesson(data.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return lessons;
    }

    /** Getters **/

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getVideo() {
        return video;
    }

    public String getDuration() {
        return duration;
    }

    public String getPoster() {
        return poster;
    }

    public boolean isViewed() {
        return viewed;
    }

    public boolean isFree() {
        return free;
    }

    public List<Lesson> getItems() {
        return items;
    }
}
