package com.emm.elephorm.models;

import android.util.Log;

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
    protected double progress; // Pourcentage
    protected List<Lesson> items = new ArrayList<Lesson>();

    public Formation(JSONObject data) {
        try {
            id              = data.getString("_id");
            title           = data.getString("title");
            subtitle        = data.getString("subtitle");
            productUrl      = data.getString("product_url");
            price           = data.getString("price") != "null" ? round(Double.parseDouble(data.getString("price")), 2) + " €" : "0.00 €";
            description     = data.getString("description");
            duration        = data.getString("duration") != "null" ? formatDuration(Integer.parseInt(data.getString("duration"), 10)) : "00:00:00";
            objectives      = data.getString("objectives");
            prerequisites   = data.getString("prerequisites");
            qcmUrl          = data.getString("qcm");
            teaserText      = data.getString("teaser_text");
            category        = data.getString("category");
            subcategory     = data.getString("subcategory");
            teaser          = data.getString("teaser");
            poster          = data.getString("poster");
            free            = Boolean.parseBoolean(data.getString("free"));
            videoCount      = data.getString("video_count") != "null" ? Integer.parseInt(data.getString("video_count"), 10) : 0;
            active          = Boolean.parseBoolean(data.getString("active"));
            publishedDate   = data.getString("publishedDate");

            JSONObject ratingObj = new JSONObject(data.getString("rating"));
            rating = ratingObj.getString("average") != "null" ? Double.parseDouble(ratingObj.getString("average")) : 0;

            progress = 0; // TODO : Aller chercher le progrès dans l'historique

            Log.d("custom", this.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Met à jour l'avancement dans la formation
     */
    public void updateProgress() {

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
        double percent = 0;
        return percent;
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
}
