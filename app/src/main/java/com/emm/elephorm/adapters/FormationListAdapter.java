package com.emm.elephorm.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.emm.elephorm.R;
import com.emm.elephorm.app.ElephormApp;
import com.emm.elephorm.models.Formation;

import java.util.List;

public class FormationListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Formation> formationItems;
    ImageLoader imageLoader = ElephormApp.getInstance().getImageLoader();

    public FormationListAdapter(Activity activity, List<Formation> formationItems) {
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
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView subTitle = (TextView) convertView.findViewById(R.id.subtitle);

        // getting movie data for the row
        Formation f = formationItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(f.getPoster(), imageLoader);

        // title
        title.setText(f.getTitle());

        // Desc
        //desc.setText(Html.fromHtml(f.getTeaserText())); // HTML
        subTitle.setText(f.getSubtitle());

        if (f.getSubtitle().isEmpty())
            subTitle.setVisibility(View.GONE);

        // progress
        ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.progress);

        if (f.getProgress() > 0) {
            progress.setProgress(Math.round(f.getProgress()));
        }
        else {
            progress.setVisibility(View.GONE);
        }

        return convertView;
    }

}