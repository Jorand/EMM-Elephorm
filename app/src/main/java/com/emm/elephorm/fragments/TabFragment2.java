package com.emm.elephorm.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.emm.elephorm.R;
import com.emm.elephorm.adapters.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment2 extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_tab_fragment2, container, false);

        expListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
        prepareListData();
        listAdapter = new ExpandableListAdapter(v.getContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setDivider(null);
        return v;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("3D");
        listDataHeader.add("Code");
        listDataHeader.add("Infographie");
        listDataHeader.add("Web & Mobiles");
        listDataHeader.add("Photographie");
        listDataHeader.add("Vidéo & VFX");
        listDataHeader.add("Audio");
        listDataHeader.add("Bureautique");
        listDataHeader.add("Business");

        // Adding child data
        List<String> D3 = new ArrayList<>();
        D3.add("The Shawshank Redemption");
        D3.add("375 tutos 3D gratuits");
        D3.add("3ds Max31");
        D3.add("ArchiCAD");
        D3.add("Artlantis");
        D3.add("AutoCAD");
        D3.add("Blender");
        D3.add("Cinema 4D");
        D3.add("Droit d'Auteur");
        D3.add("Game Design");
        D3.add("Jeux vidéo");
        D3.add("KeyShot");

        List<String> code = new ArrayList<>();
        code.add("The Shawshank Redemption");
        code.add("375 tutos 3D gratuits");
        code.add("3ds Max31");
        code.add("ArchiCAD");
        code.add("Artlantis");
        code.add("AutoCAD");
        code.add("Blender");
        code.add("Cinema 4D");
        code.add("Droit d'Auteur");
        code.add("Game Design");
        code.add("Jeux vidéo");
        code.add("KeyShot");

        List<String> infographie = new ArrayList<>();
        infographie.add("The Shawshank Redemption");
        infographie.add("375 tutos 3D gratuits");
        infographie.add("3ds Max31");
        infographie.add("ArchiCAD");

        List<String> wM = new ArrayList<>();
        wM.add("The Shawshank Redemption");
        wM.add("375 tutos 3D gratuits");
        wM.add("3ds Max31");
        wM.add("ArchiCAD");

        List<String> photo = new ArrayList<>();
        photo.add("The Shawshank Redemption");
        photo.add("375 tutos 3D gratuits");
        photo.add("3ds Max31");
        photo.add("ArchiCAD");

        List<String> vV = new ArrayList<>();
        vV.add("The Shawshank Redemption");
        vV.add("375 tutos 3D gratuits");
        vV.add("3ds Max31");
        vV.add("ArchiCAD");

        List<String> audio = new ArrayList<>();
        audio.add("The Shawshank Redemption");
        audio.add("375 tutos 3D gratuits");

        List<String> bureautique = new ArrayList<>();
        bureautique.add("The Shawshank Redemption");
        bureautique.add("375 tutos 3D gratuits");
        bureautique.add("3ds Max31");
        bureautique.add("ArchiCAD");

        List<String> business = new ArrayList<>();
        business.add("The Shawshank Redemption");
        business.add("375 tutos 3D gratuits");

        listDataChild.put(listDataHeader.get(0), D3); // Header, Child data
        listDataChild.put(listDataHeader.get(1), code);
        listDataChild.put(listDataHeader.get(2), infographie);
        listDataChild.put(listDataHeader.get(3), wM);
        listDataChild.put(listDataHeader.get(4), photo);
        listDataChild.put(listDataHeader.get(5), vV);
        listDataChild.put(listDataHeader.get(6), audio);
        listDataChild.put(listDataHeader.get(7), bureautique);
        listDataChild.put(listDataHeader.get(8), business);
    }


}
