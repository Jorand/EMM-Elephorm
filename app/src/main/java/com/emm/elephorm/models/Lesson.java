package com.emm.elephorm.models;

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
    protected String vignette;
    protected String file;
    protected boolean viewed;



    /** Getters & setters **/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getVignette() {
        return vignette;
    }

    public void setVignette(String vignette) {
        this.vignette = vignette;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
