package com.example.pfr3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    //Données qu'on prend du design
    DrawerLayout dLayout;
    NavigationView nView;
    androidx.appcompat.widget.Toolbar tBar;

    //variable bluetooth
    BluetoothManager bluetoothManager;
    Set<BluetoothDevice> pairedDevices = new HashSet<>();
    Set<BluetoothDevice> notPairedDevices = new HashSet<>();
    IntentFilter intentFilter;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 16;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(context, "nom : "+device.getName()+"\n@MAC : "+device.getAddress(),Toast.LENGTH_SHORT).show();
                //on initialise le set contenant les devices inconnus
                notPairedDevices.add(device);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mettre en pleine écran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //initialisation de la vue de l'app
        setContentView(R.layout.activity_main);

        //initialisation des données
        dLayout = findViewById(R.id.drawer_layout);
        nView = findViewById(R.id.navigation_view);
        tBar = findViewById(R.id.toolbar);
        bluetoothManager = (BluetoothManager) getSystemService(BluetoothManager.class);

        //met la nView au front
        nView.bringToFront();

        //initialisation d'un écouteur pour le dLayout
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, dLayout,tBar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        dLayout.addDrawerListener(toggle);
        toggle.syncState();

        //add an event lister to the navigation items
        nView.setNavigationItemSelectedListener(this);

        //initialisation du filtre de discovery
        //et on lance la recherche de nouveaux appareils
         intentFilter =new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, intentFilter);
    }

    private void connexionBluetooth(){
        if(bluetoothManager.getAdapter() == null){
            //le téléphone ne supporte pas le bluetooth o_O
        }
        else{
            //si le bluetooth n'est pas activé
            if(!bluetoothManager.getAdapter().isEnabled()){
                //on active le bluetooth
                bluetoothManager.getAdapter().enable();
            }
            else{
                //on le désactive
                bluetoothManager.getAdapter().disable();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //si on clic sur la flech de retour
        //et que la bar de naviguation est ouverte
        if(dLayout.isDrawerOpen(GravityCompat.START)){
            //on ferme la bar de naviguation
            dLayout.closeDrawer((GravityCompat.START));
        }
        else{
            //sinon on ferme l'app
            this.finishAffinity();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            //si on selectionné activer
            case R.id.navigation_toolbar_activer:
                connexionBluetooth();
                break;
            //peripheriques
            case R.id.navigation_toolbar_peripherique:
                affichagePeripherique();
                break;
            //mode automatique
            case R.id.navigation_toolbar_automatique:

                break;
            //mode manuel
            case R.id.navigation_toolbar_manuel:

                break;
        }
        //on ferme la bar de naviguation
        dLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void affichagePeripherique() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGpsEnabled) {
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        }
        //si le bluetooth est activé
        if(bluetoothManager.getAdapter().isEnabled()){
            //initialisation du set avec les devices connus
            pairedDevices = bluetoothManager.getAdapter().getBondedDevices();
            //si le discovery est activé
            if(bluetoothManager.getAdapter().isDiscovering()){
                Toast.makeText(this, "discovery en cours ...",Toast.LENGTH_SHORT).show();
                //on le désactive
                bluetoothManager.getAdapter().cancelDiscovery();
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    switch (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        case PackageManager.PERMISSION_DENIED:
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_ACCESS_FINE_LOCATION);

                            break;
                        case PackageManager.PERMISSION_GRANTED:
                            boolean a = bluetoothManager.getAdapter().startDiscovery();
                            Toast.makeText(MainActivity.this, "Start discovery es "+a, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
            // TEST
            for(BluetoothDevice bd : notPairedDevices){
                Toast.makeText(this, "nom : "+bd.getName()+"\n@MAC : "+bd.getAddress(),Toast.LENGTH_SHORT).show();
            }
            // TEST REUSSI
            //for(BluetoothDevice bd : pairedDevices){
            //    Toast.makeText(this, "nom : "+bd.getName()+"\n@MAC : "+bd.getAddress(),Toast.LENGTH_SHORT).show();
            //}


            //on ajoute le tout à l'endroit fait pour
            /* NOT YET ;-;

            Dialog peripheriques = new Dialog(this);
            peripheriques.setContentView(R.layout.popup_peripheriques);
            ListView liste = peripheriques.findViewById(R.id.list_view);
            BluetoothDevice[] devices = new BluetoothDevice[10];
            int index = 0;
            for(BluetoothDevice device : this.pairedDevices){
                devices[index] = device;
            }
            ArrayAdapter<BluetoothDevice> arrayAdapter = new ArrayAdapter<BluetoothDevice>(this,R.layout.text_view , devices);
            liste.setAdapter(arrayAdapter);
            peripheriques.show();*/
        }
        else{
            //le bluetooth n'est pas activé
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean a = bluetoothManager.getAdapter().startDiscovery();
                    Toast.makeText(MainActivity.this, "Start discovery es " + a, Toast.LENGTH_SHORT).show();
                } else {
                    //exit application or do the needful
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}