package com.example.maurice.menmeindopdr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maurice.menmeindopdr.NSData.Station;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RouteSelectAdapter extends ArrayAdapter<Station> {

    //TextView listviewItemTitle;
//    TextView vakantieTxt;
//    TextView regiosTxt;
//    TextView regiosNummersTxt;

    public RouteSelectAdapter(Context context, ArrayList<Station> items) {
        super( context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        // Mural ophalen
        Station station = getItem(position);

        // View aanmaken of herbruiken
        if( convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_item_pickroute,
                    parent,
                    false
            );
        }

        TextView listViewItemTitle = convertView.findViewById(R.id.textViewTest);



//        final ImageView image = convertView.findViewById(R.id.listviewItemImage);
//
//        for(int i = 0; i < blindWall.getImagesUrls().size(); i++)
//        {
//            String imageUrlCurr = blindWall.getImagesUrls().get(i);
//
//            if(imageUrlCurr.contains("_1"))
//            {
//
//                Picasso.get().load(imageUrlCurr).into(image);
//            }
//        }
        return convertView;
    }
}
