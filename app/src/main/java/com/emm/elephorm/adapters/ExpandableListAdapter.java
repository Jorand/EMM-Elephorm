package com.emm.elephorm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.emm.elephorm.R;
import com.emm.elephorm.models.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<Category> mListCategorie;

    public ExpandableListAdapter(Context context, ArrayList<Category> listCategorie) {

        this._context = context;
        this.mListCategorie = listCategorie;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = this.mListCategorie.get(groupPosition).getTitle();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);

        if (isExpanded) {
            lblListHeader.setTextColor(convertView.getResources().getColor(R.color.ColorPrimary));
        }
        else {
            lblListHeader.setTextColor(convertView.getResources().getColor(R.color.ColorTxt));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = this.mListCategorie.get(groupPosition).getSubcategories().get(childPosition).getTitle();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.mListCategorie.get(groupPosition).getSubcategories().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mListCategorie.get(groupPosition).getSubcategories().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListCategorie.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mListCategorie.size();
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