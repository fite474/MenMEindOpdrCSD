package com.example.maurice.menmeindopdr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maurice.menmeindopdr.NSData.TreinReis;
import com.example.maurice.menmeindopdr.NSData.TreinType;

import java.util.ArrayList;

public class RouteSelectAdapter extends ArrayAdapter<TreinReis> {

    ImageView trainImage;
    ImageView trackImage;
    ImageView transferImage;
    TextView departureTrack;
    TextView transfers;
    TextView departureTime;
    TextView arrivalTime;
    TextView journeyDuration;
    TextView destination;

    public RouteSelectAdapter(Context context, ArrayList<TreinReis> items) {
        super( context, 0, items);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        // Mural ophalen
        TreinReis treinReis = getItem(position);

        // View aanmaken of herbruiken
        if( convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_item_pickjourney,
                    parent,
                    false
            );
        }
        if(treinReis != null)
        {
            destination = convertView.findViewById(R.id.destinationTV);
            destination.setText(treinReis.getFirstDestination());

            trainImage = convertView.findViewById(R.id.trainIconDetail);
            if (treinReis.getLegs().get(0).getType() == TreinType.INTERCITY)
            {
                trainImage.setImageResource(R.drawable.trainicon_ic);
            }
            else
            {
                trainImage.setImageResource(R.drawable.trainicon_spr);
            }

            trackImage = convertView.findViewById(R.id.trackIcon);
            trackImage.setImageResource(R.drawable.track);

            transferImage = convertView.findViewById(R.id.transferIcon);
            transferImage.setImageResource(R.drawable.transfer);

            departureTrack = convertView.findViewById(R.id.trackText);
            departureTrack.setText(treinReis.getVertrekSpoor());

            transfers = convertView.findViewById(R.id.transferText);
            transfers.setText(String.valueOf(treinReis.getAantalOverstappen()));

            departureTime = convertView.findViewById(R.id.depTimeTextView);
            String departuretime = "";
            if(treinReis.getVertrektijd().getMinutes() < 10)
            {
                departuretime = ""+ treinReis.getVertrektijd().getHours() + ":0" + treinReis.getVertrektijd().getMinutes();
                Log.d("VERTREKTIJDTAGGGG", String.valueOf(treinReis.getVertrektijd().getHours()));
            }
            else
            {
                departuretime = ""+ treinReis.getVertrektijd().getHours() + ":"+ treinReis.getVertrektijd().getMinutes();
                Log.d("VERTREKTIJDTAGGGG", String.valueOf(treinReis.getVertrektijd().getHours()));
            }
            departureTime.setText(departuretime);

            arrivalTime = convertView.findViewById(R.id.arrTimeTV);
            String arrivaltime = "";
            if(treinReis.getAankomsttijd().getMinutes() < 10)
            {
                arrivaltime = ""+ treinReis.getAankomsttijd().getHours() + ":0" + treinReis.getAankomsttijd().getMinutes();
            }
            else
            {
                arrivaltime = ""+ treinReis.getAankomsttijd().getHours() + ":"+ treinReis.getAankomsttijd().getMinutes();
            }
            arrivalTime.setText(arrivaltime);

            journeyDuration = convertView.findViewById(R.id.durTV);
            int minutes = treinReis.getRitDuur() % 60;
            int hours = treinReis.getRitDuur() / 60;

            String ritduur = " ";
            if(minutes < 10)
            {
                ritduur = hours + ":0"+minutes + " uur";
            }
            else
            {
                ritduur =hours + ":"+minutes + " uur";
            }
            journeyDuration.setText(ritduur);

        }
        return convertView;
    }
}
