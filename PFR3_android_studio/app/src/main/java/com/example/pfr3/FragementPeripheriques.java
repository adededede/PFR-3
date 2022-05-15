package com.example.pfr3;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragementPeripheriques extends Fragment {
    private static final Set<BluetoothDevice> tous_peripheriques = null;
    private Set<BluetoothDevice> peripheriques;

    public FragementPeripheriques(Set<BluetoothDevice> s) {
        this.peripheriques = s;
    }

    public static FragementPeripheriques newInstance(Set<BluetoothDevice> d) {
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), R.layout.peripheriques_item) ;
        if(tous_peripheriques == null){
            if(peripheriques!=null && peripheriques.size()>0){
                for(BluetoothDevice d : peripheriques){
                    adapter.add("Nom : "+d.getName()+"\n@MAC : "+d.getAddress()+"\n");
                }
            }
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = ((TextView) view).getText().toString();
                String adresse = text.substring(text.length()-18,text.length()-1);
                //TODO affiche connexion en cours

                //on stock l'adress de l'item cliqué
                MainActivity main = (MainActivity)getActivity();
                main.connexion(adresse);
                //on reviens à la mainactivity
                getActivity().getFragmentManager().popBackStack();
            }
        });
        list.setAdapter(adapter);
        return v;
    }
}
