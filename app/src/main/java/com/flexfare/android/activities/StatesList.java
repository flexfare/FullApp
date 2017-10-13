package com.flexfare.android.activities;

/**
 * Created by kodenerd on 8/26/17.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.flexfare.android.R;

import java.util.ArrayList;
import java.util.List;


public class StatesList extends AppCompatActivity {

//    @Bind(R.id.states_picker)
    Spinner spinner;
    List<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.states_list);
        //ButterKnife.bind(this);


        spinner = (Spinner) findViewById(R.id.states_picker);

        //final String[] states = getResources().getStringArray(R.array.states);
        list = new ArrayList<String>();
        list.add("All States");
        list.add("Abia");
        list.add("Adamawa");
        list.add("Anambra");
        list.add("Akwa Ibom");
        list.add("Bauchi");
        list.add("Bayelsa");
        list.add("Benue");
        list.add("Borno");
        list.add("Cross River");
        list.add("Delta");
        list.add("Ebonyi");
        list.add("Enugu");
        list.add("Edo");
        list.add("Ekiti");
        list.add("FCT");
        list.add("Gombe");
        list.add("Imo");
        list.add("Jigawa");
        list.add("Kaduna");
        list.add("Kano");
        list.add("Katsina");
        list.add("Kebbi");
        list.add("Kogi");
        list.add("Kwara");
        list.add("Lagos");
        list.add("Nasarawa");
        list.add("Niger");
        list.add("Ogun");
        list.add("Ondo");
        list.add("Osun");
        list.add("Oyo");
        list.add("Plateau");
        list.add("Rivers");
        list.add("Sokoto");
        list.add("Taraba");
        list.add("Yobe");
        list.add("Zamfara");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextColor(Color.parseColor("#FF9800"));
                return v;
            }
            public View getDropDownView(int position, View converView, ViewGroup parent) {
                View v = super.getDropDownView(position, converView, parent);
                v.setBackgroundColor(Color.parseColor("#FF9800"));
                ((TextView) v).setTextColor(Color.parseColor("#ffffff"));
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String states = parent.getItemAtPosition(position).toString();
                ((TextView) findViewById(R.id.selectedState)).setText(states);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
