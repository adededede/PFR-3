package com.example.pfr3;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragementPeripheriques extends Fragment {
    private static final String tous_peripheriques = "";
    private String peripheriques;

    public FragementPeripheriques(String s) {
        this.peripheriques = s;
    }

    public static FragementPeripheriques newInstance(String d) {
        FragementPeripheriques fragment = new FragementPeripheriques(d);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_affichage_peripheriques, container, false);
        ListView list = v.findViewById(R.id.fragment_p);
        List<String> adresse_mac = new ArrayList<String>();
        if(tous_peripheriques.isEmpty()){
           adresse_mac = Arrays.asList(peripheriques.split("\n").clone());
        }
        else{
            adresse_mac = Arrays.asList(tous_peripheriques.split("\n").clone());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1,adresse_mac) ;
        list.setAdapter(adapter);
        return v;
    }
}
