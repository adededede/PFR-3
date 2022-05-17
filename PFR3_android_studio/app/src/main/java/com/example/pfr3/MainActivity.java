package com.example.pfr3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //https://stackoverflow.com/questions/18657427/ioexception-read-failed-socket-might-closed-bluetooth-on-android-4-3
    //https://github.com/edufolly/flutter_bluetooth_serial/issues/18
    //https://www.youtube.com/watch?v=TLXpDY1pItQ
    //https://www.youtube.com/watch?v=Kfe3IYhiKFo
    //https://www.geeksforgeeks.org/all-about-hc-05-bluetooth-module-connection-with-android/

    //variable
    int clicAutomatique,clicManuel = 0;

    //Données qu'on prend du design
    View cartographie;
    DrawerLayout dLayout;
    NavigationView nView;
    androidx.appcompat.widget.Toolbar tBar;
    String dernierFragement;
    Button btnGauche,btnDroit, btnBas, btnHaut;

    //variable bluetooth
    Connexion connexion;
    BluetoothManager bluetoothManager;
    Set<BluetoothDevice> pairedDevices = new HashSet<>();
    Set<BluetoothDevice> notPairedDevices = new HashSet<>();
    Set<BluetoothDevice> devices = new HashSet<>();
    IntentFilter intentFilter;
    BluetoothDevice jimmy;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final int MESSAGE_STATE_CHANGED = 2;
    public static final int MESSAGE_STATE_READ = 3;
    public static final int MESSAGE_STATE_WRITE= 4;
    public static final int MESSAGE_DEVICE_NAME = 5;
    public static  final String DEVICE_NAME = "";
    private String connecte_a;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case MESSAGE_STATE_CHANGED:
                    switch (msg.arg1){
                        case Connexion.STATE_NONE:
                            setState("PAS CONNECTE");
                            Toast.makeText(getApplicationContext(),"PAS CONNECTE",Toast.LENGTH_SHORT).show();
                            break;
                        case Connexion.STATE_CONNECTED:
                            setState("CONNECTE : "+connecte_a);
                            Toast.makeText(getApplicationContext(),"CONNECTE",Toast.LENGTH_SHORT).show();
                            break;
                        case Connexion.STATE_CONNECTING:
                            setState("CONNEXION EN COURS...");
                            Toast.makeText(getApplicationContext(),"CONNEXION EN COURS",Toast.LENGTH_SHORT).show();
                            break;
                        case Connexion.STATE_LISTEN:
                            setState("PAS CONNECTE, LISTEN");
                            Toast.makeText(getApplicationContext(),"PAS CONNECTE, LISTEN",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case MESSAGE_STATE_READ:
                    byte[] buffer_e = (byte[]) msg.obj;
                    String entree = new String(buffer_e,0,msg.arg1);
                    Toast.makeText(getApplicationContext(),"RECU : "+entree,Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_STATE_WRITE:
                    byte[] buffer_s = (byte[])msg.obj;
                    String sortie = new String(buffer_s);
                    Toast.makeText(getApplicationContext(),"ENVOIE : "+sortie,Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_DEVICE_NAME:
                    connecte_a = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),"DEVICE : "+connecte_a,Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //Toast.makeText(context, "nom : "+device.getName()+"\n@MAC : "+device.getAddress(),Toast.LENGTH_LONG).show();
                //on initialise le set contenant les devices inconnus
                notPairedDevices.add(device);
            }
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                try {
                    affichagePeripheriques();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialisation de la vue de l'app
        setContentView(R.layout.activity_main);

        //initialisation des données
        dLayout = findViewById(R.id.drawer_layout);
        nView = findViewById(R.id.navigation_view);
        tBar = findViewById(R.id.toolbar);
        btnBas = findViewById(R.id.btnBas);
        btnHaut = findViewById(R.id.btnHaut);
        btnGauche = findViewById(R.id.btnGauche);
        btnDroit =  findViewById(R.id.btnDroit);
        bluetoothManager = getSystemService(BluetoothManager.class);
        connexion = new Connexion(this, handler,getDeviceId(this));
        cartographie = findViewById(R.id.view_cartographie);

        //on cache les boutons de commandes
        btnDroit.setVisibility(View.GONE);
        btnGauche.setVisibility(View.GONE);
        btnHaut.setVisibility(View.GONE);
        btnBas.setVisibility(View.GONE);

        //met la nView au front
        nView.bringToFront();

        //initialisation d'écouteur pour les boutons
        btnDroit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on envoie à l'appareil bluetooth le signal pour aller à droite

            }
        });
        btnGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on envoie à l'appareil bluetooth le signal pour aller à gauche

            }
        });
        btnHaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on envoie à l'appareil bluetooth le signal pour aller tout droit

            }
        });
        btnBas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on envoie à l'appareil bluetooth le signal pour aller en arrière

            }
        });

        //initialisation d'un écouteur pour le dLayout
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, dLayout,tBar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        dLayout.addDrawerListener(toggle);
        toggle.syncState();

        //add an event listener to design items
        nView.setNavigationItemSelectedListener(this);

        //initialisation du filtre de discovery
        //et on lance la recherche de nouveaux appareils
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, intentFilter);
    }

    private void setState(CharSequence c){
        Toast.makeText(this,c,Toast.LENGTH_SHORT).show();
    }

    public static UUID getDeviceId(Context context) {
        final String tmSerial, androidId;
        //num IMEI de mon tel : 861758043102178
        tmSerial = "861758043102178";
        androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(),  tmSerial.hashCode());
        return deviceUuid;
    }

    private void connexionBluetooth(){;
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
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGpsEnabled) {
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        }
        //Toast.makeText(this,"CONNEXIONBLUETOOTH : unpaired : "+notPairedDevices.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //si on clic sur la flech de retour
        //et que la bar de naviguation est ouverte
        if(dLayout.isDrawerOpen(GravityCompat.START)){
            //on ferme la bar de naviguation
            dLayout.closeDrawer((GravityCompat.START));
        }
        //si on a un fragment en cours
        else if(!dernierFragement.isEmpty()){
            //on le supprime et on vide la variable et on deselctionne le drawerlayout
            fragmentTransaction.remove(fragmentManager.findFragmentByTag(dernierFragement)).commit();
            dernierFragement="";
            LinearLayout layout_main = findViewById(R.id.layout_lineaire_main);
            layout_main.setVisibility(View.VISIBLE);
            dLayout.setSelected(false);
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
                item.setChecked(false);
                if(!bluetoothManager.getAdapter().isEnabled()){
                    item.setIcon(R.mipmap.ic_check);
                }
                else{
                    item.setIcon(null);
                }
                connexionBluetooth();
                break;
            //peripheriques
            case R.id.navigation_toolbar_peripherique:
                item.setChecked(false);
                affichagePeripherique();
                break;
            //mode automatique
            case R.id.navigation_toolbar_automatique:
                //connexion.connexion(bluetoothManager.getAdapter().getRemoteDevice("98:D3:91:FD:AD:50"));
                //connexion.write("prout".getBytes());
                item.setChecked(false);
                if(clicAutomatique%2 == 1){
                    item.setIcon(R.mipmap.ic_mode_on);
                    this.btnBas.setVisibility(View.VISIBLE);
                    this.btnHaut.setVisibility(View.VISIBLE);
                    this.btnGauche.setVisibility(View.VISIBLE);
                    this.btnDroit.setVisibility(View.VISIBLE);
                }
                else{
                    //remettre l'icon du mode manuel en on
                    item.setIcon(null);
                    this.btnBas.setVisibility(View.GONE);
                    this.btnHaut.setVisibility(View.GONE);
                    this.btnGauche.setVisibility(View.GONE);
                    this.btnDroit.setVisibility(View.GONE);
                }
                clicAutomatique++;
                break;
            //mode manuel
            case R.id.navigation_toolbar_manuel:
                item.setChecked(false);
                if(clicManuel%2 == 1){
                    //remettre l'icon du mode automatique en on
                    item.setIcon(null);
                    this.btnBas.setVisibility(View.GONE);
                    this.btnHaut.setVisibility(View.GONE);
                    this.btnGauche.setVisibility(View.GONE);
                    this.btnDroit.setVisibility(View.GONE);
                }
                else{
                    item.setIcon(R.mipmap.ic_mode_on);
                    this.btnBas.setVisibility(View.VISIBLE);
                    this.btnHaut.setVisibility(View.VISIBLE);
                    this.btnGauche.setVisibility(View.VISIBLE);
                    this.btnDroit.setVisibility(View.VISIBLE);
                }
                clicManuel++;
                break;
        }
        //on ferme la bar de naviguation
        dLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void affichagePeripheriques() throws IOException {
        jimmy = null;
        devices.addAll(pairedDevices);
        devices.addAll(notPairedDevices);
        Toast.makeText(this,"nb devices : " + devices.size(),Toast.LENGTH_SHORT).show();
       /*
       //Connexion a Jimmy directe
        if(!notPairedDevices.isEmpty() && notPairedDevices.size()>0){
            for(BluetoothDevice d : notPairedDevices){
                if(d.getAddress()=="98:D3:91:FD:AD:50"){
                jimmy = d;
                }
            }
        }*/

        FragementPeripheriques f = new FragementPeripheriques(devices);
        //lance le fragment des périphériques
        LaunchFragment(f,"FragementPeripheriques");
    }

    private void LaunchFragment(Fragment frag, String tag){
        LinearLayout layout_main = findViewById(R.id.layout_lineaire_main);
        layout_main.setVisibility(View.GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //selection de la transition voulu
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        //si le container du fragemnt n'est pas vide
        if(!fragmentTransaction.isEmpty()){
            //onsupprime le dernier fragment
            fragmentTransaction.remove(fragmentManager.findFragmentByTag(dernierFragement)).commit();
        }
        //on ajoute le fragement au container voulu
        fragmentTransaction.add(R.id.fragment_container,frag,tag);
        //on commit
        fragmentTransaction.commit();
        //MAJ du dernier fragment
        dernierFragement = tag;
    }

    private void affichagePeripherique() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGpsEnabled) {
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        }
        //Toast.makeText(this,"AFFICHAGEPERIPHERIQUE : unpaired : "+notPairedDevices.toString(),Toast.LENGTH_SHORT).show();
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
                            //Toast.makeText(MainActivity.this, "Start discovery : "+a, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                // TODO remplacer par un popup de chargement
                //Toast.makeText(this,"Scan des appareils autour de vous ....",Toast.LENGTH_LONG).show();
            }
            // TEST REUSSI
            //for(BluetoothDevice bd : pairedDevices){
            //    Toast.makeText(this, "nom : "+bd.getName()+"\n@MAC : "+bd.getAddress(),Toast.LENGTH_SHORT).show();
            //}

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
                    //Toast.makeText(MainActivity.this, "Start discovery : " + a, Toast.LENGTH_SHORT).show();
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
        if(connexion!=null){
            connexion.stop();
        }
    }

    public void connexion(String adresse) {
        connexion.connexion(bluetoothManager.getAdapter().getRemoteDevice(adresse));
        connexion.write("prout".getBytes());
    }


}