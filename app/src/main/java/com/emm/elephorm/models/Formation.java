package com.emm.elephorm.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.emm.elephorm.R;
import com.emm.elephorm.app.ElephormApp;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Inikaam on 11/08/2015.
 */
public class Formation {
    protected String id;
    protected String title;
    protected String subtitle;
    protected String duration;
    protected String description;
    protected String teaserText;
    protected String teaser;
    protected String images;
    protected String price;
    protected int videoCount;
    protected String objectives;
    protected String prerequisites;
    protected String productUrl;
    protected String qcmUrl;
    protected String category;
    protected String subcategory;
    protected double rating;
    protected String publishedDate;
    protected String poster;
    protected boolean active;
    protected boolean free;
    protected float progress; // Pourcentage
    protected String ean;
    protected List<Lesson> items = new ArrayList<Lesson>();

    public Formation(JSONObject data) {
        try {
            title           = data.getString("title");
            subtitle        = data.getString("subtitle");
            productUrl      = data.getString("product_url");
            price           = !data.getString("price").equals("null") ? round(Double.parseDouble(data.getString("price")), 2) + " €" : "0.00 €";
            description     = data.getString("description");
            duration        = !data.getString("duration").equals("null") ? formatDuration((int) Double.parseDouble(data.getString("duration"))) : "00:00:00";
            objectives      = data.getString("objectives");
            prerequisites   = data.getString("prerequisites");
            qcmUrl          = data.getString("qcm");
            teaserText      = data.getString("teaser_text");
            category        = data.getString("category");
            subcategory     = data.getString("subcategory");
            teaser          = data.getString("teaser");
            poster          = data.getString("poster");
            free            = Boolean.parseBoolean(data.getString("free"));
            videoCount      = !(data.getString("video_count").equals("null")) ? Integer.parseInt(data.getString("video_count"), 10) : 0;
            active          = Boolean.parseBoolean(data.getString("active"));
            publishedDate   = data.getString("publishedDate");
            ean             = data.getString("ean13");

            JSONObject ratingObj = new JSONObject(data.getString("rating"));
            rating = !ratingObj.getString("average").equals("null") ? Double.parseDouble(ratingObj.getString("average")) : 0;

            // Gestion des items
            if(data.has("items")) {
                JSONArray items = new JSONArray(data.getString("items"));

                this.items = Lesson.getLessonList(items);
                updateProgress();
            }


            progress = 0; // TODO : Aller chercher le progrès dans l'historique

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    //define callback interface
    public interface getFormationCallback {
        void onGetFinished(Formation formation);
        void onGetFail(String error);
    }

    /**
     * Renvoie un formation à partir de son code ean
     * @param ean : code ean de la formation
     */
    public static void getFormation(String ean, getFormationCallback cb) {
        final getFormationCallback callback = cb;

        // Test de connexion
        ConnectivityManager cm =
                (ConnectivityManager)ElephormApp.getInstance().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            callback.onGetFail(ElephormApp.getInstance().getString(R.string.global_connexion_error));
        } else {
            JsonObjectRequest request = new JsonObjectRequest("http://eas.elephorm.com/api/v1/trainings/" + ean,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Formation formation = new Formation(response);
                        callback.onGetFinished(formation);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onGetFail(ElephormApp.getInstance().getString(R.string.global_volley_error));

                    }
                }
            );
            ElephormApp.getInstance().getRequestQueue().add(request);
        }
    }

    //define callback interface
    public interface getFormationListCallback {
        void onGetFinished(List<Formation> formations);
        void onGetFail(String error);
    }

    /**
     * Renvoie le tableau des formations de la sous-catégorie indiquée
     * @param id : id de la sous-catégorie
     */
    public static void getSubcategoryFormations(String id, getFormationListCallback cb) {
        final getFormationListCallback callback = cb;
        // Test de connexion
        ConnectivityManager cm =
                (ConnectivityManager)ElephormApp.getInstance().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            callback.onGetFail(ElephormApp.getInstance().getString(R.string.global_connexion_error));
        } else {
            JsonArrayRequest request = new JsonArrayRequest("http://eas.elephorm.com/api/v1/subcategories/" + id + "/trainings",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject obj = null;
                        List<Formation> formations = new ArrayList<Formation>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                obj = response.getJSONObject(i);
                                formations.add(new Formation(obj));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onGetFinished(formations);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onGetFail(ElephormApp.getInstance().getString(R.string.global_volley_error));

                    }
                }
            );
            ElephormApp.getInstance().getRequestQueue().add(request);
        }
    }

    /**
     * Met à jour l'avancement dans la formation
     */
    public void updateProgress() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ElephormApp.getInstance().getBaseContext());
        int count = countViewedLessons(items);

        progress = ((float) count/(float) videoCount) * 100;

        if(progress > 0) {
            addIdFromList("recommended_categories", subcategory);
            if(progress < 100) {
                addIdFromList("current_formations", ean);
            } else {
                removeIdFromList("current_formations", ean);
                addIdFromList("finished_formations", ean);
            }
        }
    }

    protected void addIdFromList(String listName, String id) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ElephormApp.getInstance().getBaseContext());
        String listString = preferences.getString(listName, "");
        String[] list = listString.split(";");
        boolean isInList = false;
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(id))
                isInList = true;
        }

        if (!isInList) {
            if (list.length > 0)
                listString += ";" + id;
            else
                listString = id;

            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(listName, listString);
            editor.commit();
        }
    }

    protected void removeIdFromList(String listName, String id) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ElephormApp.getInstance().getBaseContext());
        String listString = preferences.getString(listName, "");
        String[] list = listString.split(";");

        listString = "";
        for (int i = 0; i < list.length; i++) {
            if (!list[i].equals(id))
                listString +=
                    (listString.equals("") ? "" : ";")
                    + list[i];
        }

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(listName, listString);
        editor.commit();
    }

    /**
     * Compte le nombre de leçons vues
     * /!\ Récursive
     * @return
     */
    protected int countViewedLessons(List<Lesson> lessons) {
        int count = 0;
        for(int i = 0;i<lessons.size();i++) {
            if(lessons.get(i).viewed)
                count++;

            if(lessons.get(i).items.size() > 0)
                count += countViewedLessons(lessons.get(i).items);
        }
        return count;
    }

    public List<Lesson> getLessonList(int floor, List<Lesson> lessons) {
        List<Lesson> lessonList = new ArrayList<Lesson>();

        for(int i = 0;i<lessons.size();i++) {
            lessonList.add(lessons.get(i));
            lessonList.get(i).setFloor(floor);
            if(lessons.get(i).getItems().size() > 0)
                lessonList.addAll(getLessonList((floor + 1), lessons.get(i).getItems()));
        }

        return lessonList;
    }

    /**
     * Renvoie la publishedDate au format dd/mm/yyyy
     * @return
     */
    protected String getPublishedDateFormated() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
        Date dateObj = null;
        try {
            dateObj = formatter.parse(publishedDate);
            String[] parts = formatter.format(dateObj).split("-");
            return parts[2] + "/" + parts[1] + "/" + parts[0];
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calcule la valeur de pourcentage de chaque leçon
     * @return valeur d'une leçon
     */
    protected double lessonPercentValue() {
        return (double) 0;
    }

    /**
     * Arrondit le nombre
     * @param number : nombre à arrondir
     * @param decimals : nombre de décimales désirées
     * @return nombre arrondit
     */
    protected double round(double number, int decimals) {
        if (decimals < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(decimals, RoundingMode.HALF_UP);
        return bd.doubleValue();
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

    /** Getters & setters **/

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public String getTeaserText() {
        return teaserText;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getPrice() {
        return price;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public String getObjectives() {
        return objectives;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getQcmUrl() {
        return qcmUrl;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public double getRating() {
        return rating;
    }

    public List<Lesson> getItems() {
        return items;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getPoster() {
        return poster;
    }

    public boolean isActive() {
        return active;
    }

    public double getProgress() {
        return progress;
    }

    public boolean isFree() {
        return free;
    }

    public String getEan() {
        return ean;
    }
}
