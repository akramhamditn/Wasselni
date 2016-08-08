package com.esprit.wasselni.Chauffeur;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.esprit.wasselni.Entities.Chauffeur;
import com.esprit.wasselni.Entities.Demande;
import com.esprit.wasselni.R;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class DifferentMenuFragment extends Fragment {

    private List<ParseObject> mAppList= new ArrayList<ParseObject>();
    private AppAdapter mAdapter;
    static final LatLng ESPRIT = new LatLng(36.8999119,10.1882127);
    View v;
    SwipeMenuListView listView;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_list, container, false);
        listView = (SwipeMenuListView) v.findViewById(R.id.listView);

        //******************************************************
        final Handler handler = new Handler();

        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Demande");
        query.whereEqualTo("Etat", "demande");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> clientList, ParseException e) {

                if ((e == null) && (clientList.size() != 0)) {
                    Log.e("score", "Retrieved " + clientList.size() + " scores");
                   // Log.e("score", clientList.get(0).getString("Type"));
                    mAppList = clientList;
                }
            }


        });
        //******************************************************

        mAdapter = new AppAdapter();
        listView.setAdapter(mAdapter);

                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timertask, 0, 100000);
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                switch (menu.getViewType()) {
                    case 0:
                        createMenu1(menu);
                        break;
                    case 1:
                        createMenu2(menu);
                        break;
                    case 2:
                        createMenu3(menu);
                        break;
                }
            }

            private void createMenu1(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0xFF, 0xFF,
                        0xFF)));
                item1.setWidth(dp2px(90));
                item1.setIcon(R.drawable.check);
                menu.addMenuItem(item1);
                SwipeMenuItem item2 = new SwipeMenuItem(
                        getContext());
                item2.setBackground(new ColorDrawable(Color.rgb(0xFF, 0xFF,
                        0xFF)));
                item2.setWidth(dp2px(90));
                item2.setIcon(R.drawable.ic_error);
                menu.addMenuItem(item2);
            }

            private void createMenu2(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0xFF, 0xFF,
                        0xFF)));
                item1.setWidth(dp2px(90));
                item1.setIcon(R.drawable.check);
                menu.addMenuItem(item1);
                SwipeMenuItem item2 = new SwipeMenuItem(
                        getContext());
                item2.setBackground(new ColorDrawable(Color.rgb(0xFF,
                        0xFF, 0xFF)));
                item2.setWidth(dp2px(90));
                item2.setIcon(R.drawable.ic_error);
                menu.addMenuItem(item2);
            }

            private void createMenu3(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0xFF, 0xFF,
                        0xFF)));
                item1.setWidth(dp2px(90));
                item1.setIcon(R.drawable.check);
                menu.addMenuItem(item1);
                SwipeMenuItem item2 = new SwipeMenuItem(
                        getContext());
                item2.setBackground(new ColorDrawable(Color.rgb(0xFF, 0xFF,
                        0xFF)));
                item2.setWidth(dp2px(90));
                item2.setIcon(R.drawable.ic_error);
                menu.addMenuItem(item2);
            }
        };
        // set creator
        listView.setMenuCreator(creator);

        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                ParseObject item = mAppList.get(position);
                item.getString("Destination");
                Log.e("ID........", item.getObjectId());
                switch (index) {
                    case 0:
                        // open
                        //ParseObject testObject = new ParseObject("Demande");
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Demande");
                        // Retrieve the object by id
                        query.getInBackground(item.getObjectId(), new GetCallback<ParseObject>() {
                            public void done(ParseObject gameScore, ParseException e) {
                                if (e == null) {
                                    // Now let's update it with some new data. In this case, only cheatMode and score
                                    // will get sent to the Parse Cloud. playerName hasn't changed.
                                    gameScore.put("Etat", "accepter");
                                    gameScore.put("NumeroPermis", Chauffeur.id);
                                    gameScore.saveInBackground();
                                }
                            }
                        });
                        //testObject.put("Etat","accepter");
                        Bundle bundle = new Bundle();
                        bundle.putString("id", item.getObjectId());
                        bundle.putDouble("latDes", item.getDouble("LatitudeDestination"));
                        bundle.putDouble("lonDes", item.getDouble("LongitudeDestination"));
                        Fragment frag = new MapChauffeurFragment();
                        frag.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.frame, frag);
                        ft.commit();
                        Demande.latitude = item.getDouble("Latitude");
                        Demande.longitude = item.getDouble("Longitude");
                        break;
                    case 1:
                        // delete
//					delete(item);
                        mAppList.remove(position);
                        mAdapter.notifyDataSetChanged();

                        break;
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final ParseObject item = mAppList.get(position);
                //Log.e("ID........", item1.getString("CIN"));
                double ds = distance(Chauffeur.latitude,Chauffeur.longitude,item.getDouble("LatitudeDestination"),item.getDouble("LongitudeDestination"));
                double d = (double) Math.round(ds * 100) / 100;
                Bundle bundle = new Bundle();
                bundle.putString("id", item.getObjectId());
                bundle.putString("des", item.getString("Destination"));
                bundle.putString("idUser", item.getString("CIN"));
                bundle.putString("prix", String.valueOf(d + 0.000) );
                bundle.putString("dis", String.valueOf(d + 0.000) );
                bundle.putDouble("latDes", item.getDouble("LatitudeDestination"));
                bundle.putDouble("lonDes", item.getDouble("LongitudeDestination"));
                Fragment frag = new MapChauffeurDetailsFragment();
                frag.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack("msg");
                ft.replace(R.id.frame, frag);
                ft.commit();
                Demande.latitude = item.getDouble("Latitude");
                Demande.longitude = item.getDouble("Longitude");


                //Toast.makeText(getContext(), String.valueOf(d+0.000)+" Dt", Toast.LENGTH_LONG).show();


            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final ParseObject item = mAppList.get(position);
                Log.e("ID........", item.getString("CIN"));
                double ds = distance(Chauffeur.latitude,Chauffeur.longitude,item.getDouble("LatitudeDestination"),item.getDouble("LongitudeDestination"));
                double d = (double) Math.round(ds * 100) / 100;

                //Toast.makeText(getContext(), String.valueOf(d+0.000)+" Dt", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Réservation");
                builder.setMessage("Cette commande sera passée d'ici à " + item.getString("Destination") + " ça coûte "+String.valueOf(d + 0.000)+" Dt");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface alert, int which) {
                        alert.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });
        return v;
    }

    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public ParseObject getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            // menu type count
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            // current menu type
            return position % 3;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            ParseObject item = getItem(position);
            //holder.iv_icon.setImageDrawable(item.loadIcon(getContext().getPackageManager()));
            holder.tv_name.setText(item.getString("Destination"));
            double ds = distance(Chauffeur.latitude,Chauffeur.longitude,item.getDouble("LatitudeDestination"),item.getDouble("LongitudeDestination"));
            double d = (double) Math.round(ds * 100) / 100;
            if(item.getDouble("LatitudeDestination")==0.0&&item.getDouble("LatitudeDestination")==0.0) {
                holder.tv_prix.setText("prix non calculé");
            }else{
                holder.tv_prix.setText(String.valueOf(d + 0.000) + " Dt");

            }
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name;
            TextView tv_prix;
            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_prix = (TextView) view.findViewById(R.id.tv_prix);
                view.setTag(this);
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
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
