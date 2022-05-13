package com.example.pfr3;

import static android.bluetooth.BluetoothProfile.STATE_CONNECTED;
import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTING;

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
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    //Données qu'on prend du design
    DrawerLayout dLayout;
    NavigationView nView;
    androidx.appcompat.widget.Toolbar tBar;
    String dernierFragement;
    UUID telephone;

    //variable bluetooth
    BluetoothManager bluetoothManager;
    Set<BluetoothDevice> pairedDevices = new HashSet<>();
    Set<BluetoothDevice> notPairedDevices = new HashSet<>();
    IntentFilter intentFilter;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(context, "nom : "+device.getName()+"\n@MAC : "+device.getAddress(),Toast.LENGTH_LONG).show();
                //on initialise le set contenant les devices inconnus
                notPairedDevices.add(device);
                //Toast.makeText(context, "taille unpaired : "+notPairedDevices.size(),Toast.LENGTH_SHORT).show();
            }
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                Toast.makeText(context, "FIN : unpaired : "+notPairedDevices.size(),Toast.LENGTH_SHORT).show();
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

        telephone = getDeviceID(this);

        //initialisation du filtre de discovery
        //et on lance la recherche de nouveaux appareils
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, intentFilter);
    }

    public static UUID getDeviceID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tmDevice = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                tmDevice = mTelephony.getDeviceId();
            } else {
                tmDevice = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }
        //num IMEI de mon tel : 861758042792177 ou 861758043102178
        tmSerial = "861758042792177";
        androidId = android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid;
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
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGpsEnabled) {
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        }
        Toast.makeText(this,"CONNEXIONBLUETOOTH : unpaired : "+notPairedDevices.toString(),Toast.LENGTH_SHORT).show();
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

    //Methode du clic des boutons de navigation
    public void cliqueNaviguer(View view){
        //verification de la connexion bluetooth

        //envoyer au robot
        switch (view.getId()){
            case R.id.buttonBas:
                //envoyer le message necessaire
                Toast.makeText(this,"clic clic bouton bas",Toast.LENGTH_SHORT);
                break;
            case R.id.buttonHaut:

                break;
            case R.id.buttonDroite:

                break;
            case R.id.buttonGauche:
                //envoyer le message necessaire
                Toast.makeText(this,"clic clic bouton gauche",Toast.LENGTH_SHORT);
                break;
        }
    }


    private void affichagePeripheriques() throws IOException {
        String p = "";
        BluetoothDevice jimmy = null;
        int index = 0;
        for(BluetoothDevice d : notPairedDevices){
            p+=d.getAddress()+'\n';
            //if(d.getAddress()=="98:D3:91:FD:AD:50"){
                jimmy = d;
           // }
        }
        for(BluetoothDevice d : pairedDevices){
            p+=d.getAddress()+'\n';
            if(d.getAddress()=="98:D3:91:FD:AD:50"){
                jimmy = d;
            }
        }
        FragementPeripheriques f = new FragementPeripheriques(p);
        ThreadConnexion thread;
        if(jimmy!= null){
            thread = new ThreadConnexion(jimmy,telephone);
            thread.run();
        }

        //lance le fragment des périphériques
        //LaunchFragment(f,"FragementPeripheriques");
    }

    private void LaunchFragment(Fragment frag, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //set the transition
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        //if the container of the fragment is not empty
        if(!fragmentTransaction.isEmpty()){
            //we suppress the last fragment
            fragmentTransaction.remove(fragmentManager.findFragmentByTag(dernierFragement)).commit();
        }
        //we add a fragment to the container
        fragmentTransaction.add(R.id.fragment_container,frag,tag);
        //then we commit
        fragmentTransaction.commit();
        //we update lastFragment
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
                            Toast.makeText(MainActivity.this, "Start discovery : "+a, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Start discovery : " + a, Toast.LENGTH_SHORT).show();
                } else {
                    //exit application or do the needful
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public class ThreadConnexion extends Thread{
        private final BluetoothDevice device;
        private final BluetoothSocket chaussette;
        private final UUID telephone;
        private final InputStream inputStream;
        private final OutputStream outputStream;
        private byte[] buffer;
        private Handler handler;
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        private byte[] navigation;


        public ThreadConnexion(BluetoothDevice bd, UUID telephone) throws IOException {
            device = bd;
            this.telephone = telephone;
            BluetoothSocket chaussure = null;
            InputStream tmpInput = null;
            OutputStream tmpOutput = null;

            try{
                //ajouter mon UUID pour pouvoir se connceter (celui de l'app)
                chaussure = device.createRfcommSocketToServiceRecord(telephone);
            }
            catch (IOException e){
                //ERREUR
            }
            chaussette=chaussure;
            try{
                tmpInput = chaussette.getInputStream();
                tmpOutput = chaussette.getOutputStream();
            }
            catch(IOException e){
                //Erreur
            }
            inputStream = tmpInput;
            outputStream=tmpOutput;
            navigation = new byte[1024];
        }

        @Override
        public void run(){
            try{
                chaussette.connect();
            }
            catch (IOException e){
                try{
                    chaussette.close();
                }
                catch (IOException e_close){
                    //ERREUR
                }
                return;
            }
            gestionConnexion(navigation);
        }

        private void gestionConnexion(byte[] bytes) {
            buffer = new byte[1024];
            int numBytes;
            ecrire(bytes);
            while(true){
                try{
                    numBytes = inputStream.read(buffer);
                    Message message = handler.obtainMessage(MESSAGE_READ,numBytes,-1,buffer);
                    message.sendToTarget();
                }
                catch (IOException e){
                    //erreur
                    break;
                }
            }
        }

        public void ecrire(byte[] bytes){
            try {
                outputStream.write(bytes);
                Message message = handler.obtainMessage(MESSAGE_WRITE,-1,-1,buffer);
                message.sendToTarget();
            }
            catch (IOException e){
                //ERREUR
            }
        }

        public void cancel(){
            try{
                chaussette.close();
            }
            catch (IOException e){
                //ERREUR
            }
        }
    }

}