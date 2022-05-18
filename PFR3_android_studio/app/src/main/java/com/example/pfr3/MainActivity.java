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
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    int clicAutomatique,clicManuel,clicCommencer = 0;

    //Données qu'on prend du design
    View cartographie;
    DrawerLayout dLayout;
    NavigationView nView;
    androidx.appcompat.widget.Toolbar tBar;
    String dernierFragement;
    TextView texte_nom,texte_vitesse,texte_position,texte_distance;
    Button btnGauche,btnDroit, btnBas, btnHaut,btnCommencer,btnRecommencer,btnArreter;

    //donnees bluetooth
    BluetoothManager bluetoothManager;
    public static BluetoothSocket chaussette;
    Set<BluetoothDevice> pairedDevices = new HashSet<>();
    Set<BluetoothDevice> notPairedDevices = new HashSet<>();
    Set<BluetoothDevice> devices = new HashSet<>();
    IntentFilter intentFilter;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final int STATE_READ = 3;
    public static final int STATE_WRITE= 4;
    public static final int STATE_CONNEXION = 5;
    private String connecte_a;
    private String addresse_mac;
    public static ThreadConnexion thread_connexion;
    public static ThreadConnecte thread_connecte;

    public static Handler handler;

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
        texte_distance  = findViewById(R.id.text_distance);
        texte_position  = findViewById(R.id.text_position);
        texte_vitesse  = findViewById(R.id.text_vitesse);
        texte_nom  = findViewById(R.id.text_nom);
        btnBas = findViewById(R.id.btnBas);
        btnHaut = findViewById(R.id.btnHaut);
        btnGauche = findViewById(R.id.btnGauche);
        btnDroit =  findViewById(R.id.btnDroit);
        btnRecommencer = findViewById(R.id.btnRecommencer);
        btnCommencer = findViewById(R.id.btnCommencer);
        btnArreter = findViewById(R.id.btnStop);
        bluetoothManager = getSystemService(BluetoothManager.class);
        //connexion = new Connexion(this, handler,getDeviceId(this));
        cartographie = findViewById(R.id.view_cartographie);
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message){
                switch(message.what){
                    case STATE_CONNEXION:
                        switch (message.arg1){
                            case 1:
                                Toast.makeText(getApplicationContext(),"CONNECTE ",Toast.LENGTH_SHORT).show();
                                //Connecté à
                                texte_nom.setText(connecte_a);
                                break;
                            case -1:
                                //echec de la connexion
                                break;
                        }
                        break;
                    case STATE_READ:
                        String recu = message.obj.toString();
                        Toast.makeText(getApplicationContext(),"RECU : "+recu,Toast.LENGTH_SHORT).show();
                        switch (recu.toLowerCase()){
                            //case on recoit une vitesse:
                            //  on fait ça
                            //  break;
                        }
                        break;
                }
            }
        };

        //on cache les boutons de commandes
        btnDroit.setVisibility(View.GONE);
        btnGauche.setVisibility(View.GONE);
        btnHaut.setVisibility(View.GONE);
        btnBas.setVisibility(View.GONE);
        btnArreter.setVisibility(View.GONE);
        btnRecommencer.setVisibility(View.GONE);

        //met la nView au front
        nView.bringToFront();

        //initialisation d'écouteur pour les boutons
        btnCommencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicCommencer%2==1){
                    btnCommencer.setBackgroundResource(R.mipmap.ic_commencer);
                    btnArreter.setVisibility(View.GONE);
                    btnRecommencer.setVisibility(View.GONE);
                }
                else{
                    btnCommencer.setBackgroundResource(R.mipmap.ic_pause);
                    btnArreter.setVisibility(View.VISIBLE);
                    btnRecommencer.setVisibility(View.VISIBLE);
                }
                clicCommencer++;
            }
        });
        btnRecommencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCommencer.setBackgroundResource(R.mipmap.ic_commencer);
                clicCommencer=0;
            }
        });
        btnArreter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCommencer.setBackgroundResource(R.mipmap.ic_commencer);
                clicCommencer=0;
            }
        });
        btnDroit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on envoie à l'appareil bluetooth le signal pour aller à droite
                if(thread_connecte!=null){
                    Toast.makeText(getApplicationContext(),"DROITE",Toast.LENGTH_SHORT).show();
                    thread_connecte.write("DROITE\n");
                }
            }
        });
        btnGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on envoie à l'appareil bluetooth le signal pour aller à gauche
                if(thread_connecte!=null){
                    Toast.makeText(getApplicationContext(),"GAUCHE",Toast.LENGTH_SHORT).show();
                    thread_connecte.write("GAUCHE\n");
                }
            }
        });
        btnHaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on envoie à l'appareil bluetooth le signal pour aller tout droit
                if(thread_connecte!=null){
                    Toast.makeText(getApplicationContext(),"DEVANT",Toast.LENGTH_SHORT).show();
                    thread_connecte.write("AVANT\n");
                }
            }
        });
        btnBas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on envoie à l'appareil bluetooth le signal pour aller en arrière
                if(thread_connecte!=null){
                    Toast.makeText(getApplicationContext(),"DERRIERE",Toast.LENGTH_SHORT).show();
                    thread_connecte.write("ARRIERE\n");
                }
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

        connecte_a = getIntent().getStringExtra("connecte_a");
        if(connecte_a != null){
            addresse_mac = getIntent().getStringExtra("addresse_mac");
        }
        if(addresse_mac != null){
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            thread_connexion = new ThreadConnexion(adapter,addresse_mac);
            thread_connexion.start();
        }
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
        if(!dernierFragement.isEmpty()){
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
                    item.setIcon(R.mipmap.ic_mode_on);
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
        devices.addAll(pairedDevices);
        devices.addAll(notPairedDevices);
        Toast.makeText(this,"nb devices : " + devices.size(),Toast.LENGTH_SHORT).show();
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
    }

    public static class ThreadConnexion extends Thread{

        public ThreadConnexion(BluetoothAdapter a, String adresse){
            BluetoothDevice robot = a.getRemoteDevice(adresse);
            BluetoothSocket chaussure = null;
            UUID uuid = robot.getUuids()[0].getUuid();

            try{
                chaussure = robot.createInsecureRfcommSocketToServiceRecord(uuid);
            }
            catch(IOException e){
                Log.e("THREADCONNEXION -> CONSTRUCTEUR","CHAUSSETTE NON INITIALISE");
            }
            chaussette = chaussure;
        }

        public void run(){
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            try{
                chaussette.connect();
                Log.e("THREADCONNEXION -> RUN"," : CONNECTED");
                handler.obtainMessage(STATE_CONNEXION, 1, -1).sendToTarget();

            }
            catch(IOException e){
                try{
                    chaussette.close();
                    Log.e("THREADCONNEXION -> RUN"," : CAN'T CONNECT");
                    handler.obtainMessage(STATE_CONNEXION,-1,-1).sendToTarget();
                }
                catch (IOException exception){
                    Log.e("THREADCONNEXION -> RUN"," CHAUSSETTE IMPOSSIBLE A FERMER");
                }
                return;
            }

            thread_connecte = new ThreadConnecte(chaussette);
            thread_connecte.run();
        }

        public void cancel(){
            try{
                chaussette.close();
            }
            catch (IOException e){
                Log.e("THREADCONNEXION -> CANCEL"," : CHAUSSETTE IMPOSSIBLE A FERMER");
            }
        }
    }

    public static class ThreadConnecte extends Thread{
        private final BluetoothSocket autre_chaussette;
        private final InputStream flux_entrant;
        private final OutputStream flux_sortant;

        public ThreadConnecte(BluetoothSocket c){
            autre_chaussette = c;
            InputStream i = null;
            OutputStream o = null;

            try{
                i = c.getInputStream();
                o = c.getOutputStream();
            }
            catch (IOException e){
                Log.e("THREADCONNECTE -> CONESTRUCTEUR"," : INPUT OUTPUT IMPOSSIBLE A CHERCHER");
            }
            flux_entrant = i;
            flux_sortant = o;
        }

        public  void run(){
            byte[] buffer = new byte[1024];
            int bytes = 0;
            while(true){
                try{
                    buffer[bytes] = (byte) flux_entrant.read();
                    String message_recu;
                    if(buffer[bytes] == '\0'){
                        message_recu = new String(buffer,0,bytes);
                        Log.e("THREADCONNECTE -> RUN","READ : "+message_recu);
                        handler.obtainMessage(STATE_READ,message_recu);
                        bytes=0;
                    }
                    else{
                        bytes++;
                    }
                }
                catch(IOException e){
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void write(String s){
            byte[] bytes = s.getBytes();
            try{
                flux_sortant.write(bytes);
            }
            catch(IOException e){
                Log.e("THREADCONNECTE -> WRITE","MESSAGE NON ENVOYE : "+s);
            }
        }

        public void cancel(){
            try{
                autre_chaussette.close();
            }
            catch (IOException e){
                Log.e("THREADCONNECTE -> CANCEL","CHAUSSETTE NON FERMEE");
            }
        }
    }
}