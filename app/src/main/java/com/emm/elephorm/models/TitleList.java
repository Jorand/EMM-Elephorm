package com.emm.elephorm.models;

import java.util.ArrayList;
import java.util.List;

public class TitleList {

    protected String title;
    protected List<Formation> formationsList = new ArrayList<>();

    public TitleList(String titleData, List<Formation> formationsData) {
        title = titleData;
        formationsList = formationsData;
    }

    /** Getters **/

    public String getTitle() {
        return title;
    }

    public List<Formation> getFormations() {
        return formationsList;
    }

}
