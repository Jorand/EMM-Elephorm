package com.emm.elephorm.models;

import android.util.Log;

import java.util.List;

/**
 * Created by Inikaam on 11/08/2015.
 */
public class Formation {
    protected String title;
    protected String subtitle;
    protected int duration;
    protected String description;
    protected String teaser;
    protected String images;
    protected double price;
    protected int video_count;
    protected String objectives;
    protected String prerequisites;
    protected String product_url;
    protected String qcm;
    protected String category;
    protected String subcategory;
    protected int rating;
    protected List<Lesson> children;
    protected double progress; // Pourcentage

    /**
     * Met à jour l'avancement dans la formation
     */
    public void updateProgress() {

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
     * Convertit la durée au format HH:mm:ss
     * @return durée convertie
     */
    public String formatDuration() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVideo_count() {
        return video_count;
    }

    public void setVideo_count(int video_count) {
        this.video_count = video_count;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getProduct_url() {
        return product_url;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }

    public String getQcm() {
        return qcm;
    }

    public void setQcm(String qcm) {
        this.qcm = qcm;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<Lesson> getChildren() {
        return children;
    }

    public void setChildren(List<Lesson> children) {
        this.children = children;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
