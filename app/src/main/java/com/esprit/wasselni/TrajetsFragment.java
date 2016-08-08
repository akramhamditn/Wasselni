package com.esprit.wasselni;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.esprit.wasselni.Adapters.TrajetsAdapter;
import com.esprit.wasselni.Entities.Trajets;
import com.esprit.wasselni.sqlite.DBHelperTrajes;

import java.util.ArrayList;
import java.util.List;


public class TrajetsFragment extends Fragment {
    List<Trajets> articleList;
    ListView listTrajets;
    DBHelperTrajes dbHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_trajets,container,false);

        articleList = new ArrayList<Trajets>();
        dbHelper= new DBHelperTrajes(getContext());
        articleList = dbHelper.getAllTrajets();
        listTrajets =(ListView)v.findViewById(R.id.listtrajes);
        TrajetsAdapter adapter= new TrajetsAdapter(getContext(),R.layout.item_trajes, dbHelper.getAllTrajets());
        //dbHelper.getAllTrajets().toString();
        listTrajets.setAdapter(adapter);
        return v;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
