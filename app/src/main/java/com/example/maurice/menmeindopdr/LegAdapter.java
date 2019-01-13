package com.example.maurice.menmeindopdr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maurice.menmeindopdr.NSData.TreinRit;
import com.example.maurice.menmeindopdr.NSData.TreinType;

import java.util.ArrayList;

public class LegAdapter extends ArrayAdapter<TreinRit>
{
    ImageView trainIcon;
    TextView startStationTV;
    TextView endStationTV;
    TextView startTrackTV;
    TextView endTrackTV;
    TextView startTimeTV;
    TextView endTimeTV;
    TextView crowdTV;

    public LegAdapter(Context context, ArrayList<TreinRit> items)
    {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        TreinRit rit = getItem(position);

        if( convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_item_leg,
                    parent,
                    false
            );
        }
        if(rit != null)
        {
            trainIcon = convertView.findViewById(R.id.trainIconDetail);
            startStationTV = convertView.findViewById(R.id.vertrekTV);
            endStationTV = convertView.findViewById(R.id.aankTV);
            startTrackTV = convertView.findViewById(R.id.depTrackTV);
            endTrackTV = convertView.findViewById(R.id.arrTrackTV);
            startTimeTV = convertView.findViewById(R.id.depTimeTV);
            endTimeTV = convertView.findViewById(R.id.arrTimeTV);
            crowdTV = convertView.findViewById(R.id.crowdnessTV);

            if(rit.getType().equals(TreinType.SPRINTER))
            {
                trainIcon.setImageResource(R.drawable.trainicon_spr);
            }
            else
            {
                trainIcon.setImageResource(R.drawable.trainicon_ic);
            }

            startStationTV.setText(rit.getStartStation());
            endStationTV.setText(rit.getEndStation());
            startTrackTV.setText(rit.getDepartureTrack());
            endTrackTV.setText(rit.getArrivalTrack());
            if(rit.getDepMinutes() < 10)
            {
                startTimeTV.setText(rit.getDepHours() + ":0"+ rit.getDepMinutes());
            }
            else
            {
                startTimeTV.setText(rit.getDepHours() + ":"+ rit.getDepMinutes());
            }
            if(rit.getArrMinutes() < 10)
            {
                endTimeTV.setText(rit.getArrHours() + ":0"+ rit.getArrMinutes());
            }
            else
            {
                endTimeTV.setText(rit.getArrHours() + ":"+ rit.getArrMinutes());
            }

            if(rit.getCrowdness() == "LOW")
            {
                crowdTV.setText("Rustig");
            }
            else
            {
                crowdTV.setText("Druk");
            }


        }

        return convertView;
    }
}
