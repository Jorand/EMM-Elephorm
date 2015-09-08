package com.emm.elephorm.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.emm.elephorm.R;
import com.emm.elephorm.app.ElephormApp;
import com.emm.elephorm.models.Formation;
import com.emm.elephorm.models.TitleList;

import java.util.ArrayList;


public class FormationExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<TitleList> mTitlesList;

    ImageLoader imageLoader = ElephormApp.getInstance().getImageLoader();

    public FormationExpandableListAdapter(Context context, ArrayList<TitleList> mTitlesList) {

        this._context = context;
        this.mTitlesList = mTitlesList;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = this.mTitlesList.get(groupPosition).getTitle();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.titlelist_group, null);
        }

        TextView titleHeader = (TextView) convertView.findViewById(R.id.titleList_label);
        titleHeader.setText(headerTitle);

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Formation formation = this.mTitlesList.get(groupPosition).getFormations().get(childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.home_list_row, null);
        }

        if (imageLoader == null)
            imageLoader = ElephormApp.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView subTitle = (TextView) convertView.findViewById(R.id.subtitle);


        // THUMBNAIL
        thumbNail.setImageUrl(formation.getPoster(), imageLoader);

        // TITLE
        title.setText(formation.getTitle());

        // SUBTITLE
        subTitle.setText(formation.getSubtitle());

        if (formation.getSubtitle().isEmpty())
            subTitle.setVisibility(View.GONE);

        // progress
        ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.progress);

        if (formation.getProgress() > 0) {
            progress.setProgress(Math.round(formation.getProgress()));
        }
        else {
            progress.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.mTitlesList.get(groupPosition).getFormations().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mTitlesList.get(groupPosition).getFormations().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mTitlesList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mTitlesList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }



    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}