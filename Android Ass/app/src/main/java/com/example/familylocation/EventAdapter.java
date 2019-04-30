package com.example.familylocation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends ArrayAdapter<eventRecord> {
    private int resource;
    private ArrayList<eventRecord> event;
    private Context context;

    public EventAdapter(Context context, int resource, ArrayList<eventRecord> event) {
        super(context, resource, event);
        this.resource = resource;
        this.event = event;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Log.d("EventAdapter", "textViewName:fuck ");
        try {
            if (v == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = layoutInflater.inflate(resource, parent, false);
            }

            Button viewMapbtn = (Button) v.findViewById(R.id.viewMapbtn);
            TextView textViewName = (TextView) v.findViewById(R.id.txtnickName);
            TextView txtTime = (TextView) v.findViewById(R.id.txtCurrentTime);
            TextView txtEvent = (TextView) v.findViewById(R.id.txtEvent);

            textViewName.setText(event.get(position).getNickName());
            txtTime.setText(event.get(position).getCurrentTime());
            txtEvent.setText(event.get(position).getEvent());

            viewMapbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ViewMapsActivity.class);
                    intent.putExtra("mapLat", event.get(position).getLat());
                    intent.putExtra("mapLongt", event.get(position).getLongt());
                    getContext().startActivity(intent);
                }
            });



            Log.d("EventAdapter", "textViewName: "+event.get(position).getNickName());


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }
}
