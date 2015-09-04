package com.emm.elephorm.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.emm.elephorm.AppActivity;
import com.emm.elephorm.R;
import com.emm.elephorm.app.ElephormApp;
import com.emm.elephorm.models.Formation;

import java.util.List;

/**
 * Created by jorand on 04/09/15.
 */
public class CustomFormationAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Formation> formationsItems;

    ImageLoader imageLoader = ElephormApp.getInstance().getImageLoader();

    public CustomFormationAdapter(Activity activity, List<Formation> formationItems) {
        this.activity = activity;
        this.formationsItems = formationsItems;
    }

    @Override
    public int getCount() {
        return formationsItems.size();
    }

    @Override
    public Object getItem(int location) {
        return formationsItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_formation, null);

        if (imageLoader == null)
            imageLoader = ElephormApp.getInstance().getImageLoader();
        NetworkImageView poster = (NetworkImageView) convertView.findViewById(R.id.poster);

        //initialisation des vues xml
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView subtitle = (TextView) convertView.findViewById(R.id.subtitle);
        TextView description = (TextView) convertView.findViewById(R.id.description);

        // récupération liste info de la ligne sélectionné
        Formation m = formationsItems.get(position);
        String url = m.getPoster();

        // title
        title.setText(m.getTitle());
        subtitle.setText(m.getSubtitle());
        // description
        description.setText(Html.fromHtml(m.getDescription()));

        //poster
        poster.setImageUrl(url, imageLoader);
        */

        return convertView;
    }

}