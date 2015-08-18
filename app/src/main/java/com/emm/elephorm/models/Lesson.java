package com.emm.elephorm.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Inikaam on 11/08/2015.
 */
public class Lesson {
    protected String id;
    protected String type;
    protected String title;
    protected String video;
    protected int duration;
    protected String poster;
    protected boolean free;
    protected boolean viewed;
    protected List<Lesson> items = new ArrayList<Lesson>();

    public Lesson(JSONObject data) {
        try {
            id = data.getString("_id");
            type = data.getString("type");
            title = data.getString("title");
            free = Boolean.parseBoolean(data.getString("free"));

            JSONArray fieldVideo = new JSONArray(data.getString("video"));

            if(fieldVideo.length() > 0) {
                video = fieldVideo.getString(0);
            }

            // Isolation des cas pour permettre au script de continuer malgré l'échec
            try {
                duration = Integer.parseInt(data.getString("duration"));
            } catch (JSONException e) {
                duration = 0;
            }
            try {
                poster = data.getString("field_poster");
            } catch (JSONException e) {
                poster = null;
            }
            try {
                JSONArray items = new JSONArray(data.getString("items"));

                this.items = Lesson.getLessonList(items);
            } catch (JSONException e) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Lesson> getLessonList(JSONArray data) {
        Log.d("custom", data.toString());
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

    /** Getters & setters **/

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

    public int getDuration() {
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
