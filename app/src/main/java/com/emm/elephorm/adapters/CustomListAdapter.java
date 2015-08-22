package com.emm.elephorm.adapters;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.emm.elephorm.R;
import com.emm.elephorm.app.ElephormApp;
import com.emm.elephorm.models.Formation;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Formation> formationItems;
    ImageLoader imageLoader = ElephormApp.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Formation> formationItems) {
        this.activity = activity;
        this.formationItems = formationItems;
    }

    @Override
    public int getCount() {
        return formationItems.size();
    }

    @Override
    public Object getItem(int location) {
        return formationItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.home_list_row, null);

        if (imageLoader == null)
            imageLoader = ElephormApp.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView desc = (TextView) convertView.findViewById(R.id.desc);
        //TextView cat = (TextView) convertView.findViewById(R.id.cat);

        // getting movie data for the row
        Formation f = formationItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(f.getPoster(), imageLoader);

        // title
        title.setText(f.getTitle());

        // Desc
        //desc.setText(Html.fromHtml(f.getTeaserText())); // HTML
        desc.setText(f.getPublishedDate());

        // category
        //cat.setText(f.getPublishedDate());

        return convertView;
    }

}