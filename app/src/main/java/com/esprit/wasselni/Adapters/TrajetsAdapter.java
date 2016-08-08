package com.esprit.wasselni.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.esprit.wasselni.R;

import java.util.List;

import com.esprit.wasselni.Entities.Trajets;


public class TrajetsAdapter extends ArrayAdapter<Trajets> {

  public final static String TAG = "trajes";
  private int resourceId = 0;
  private LayoutInflater inflater;

  public TrajetsAdapter(Context context, int resourceId, List<Trajets> trajets) {
    super(context, 0, trajets);
    this.resourceId = resourceId;
    inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    View view;

    TextView textName, textPost;

    view = inflater.inflate(resourceId, parent, false);

    try {
      textName = (TextView)view.findViewById(R.id.distination);
     textPost = (TextView)view.findViewById(R.id.distination1);
       } catch( ClassCastException e ) {
      throw e;
    }
    
    Trajets item = getItem(position);
    
    
    textName.setText(item.getDestination());
    textPost.setText(item.getLatitude());

    return view;
  }

}
