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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //variable
    boolean clicManuel,clicCommencer = false;
    boolean clicAutomatique = true;
    String dernierFragement;
    int dessin = 0;
    float x_depart = 0,x_arrive = 0,y_depart = 0,y_arrive = 0;
    String direction = "y-";
    Bitmap b;
    int compteur_erreur_depart=0;

    //Données qu'on prend du design
    ImageView cartographie;
    DrawerLayout dLayout;
    NavigationView nView;
    androidx.appcompat.widget.Toolbar tBar;
    TextView texte_nom,texte_vitesse,texte_position,texte_distance;
    Button btnGauche,btnDroit, btnBas, btnHaut,btnCommencer,btnRecommencer,btnArreter;
    LocalDateTime date_cartographie;

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
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
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
        cartographie = findViewById(R.id.view_cartographie);
        //on enlève la bar de chargement
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        bluetoothManager = getSystemService(BluetoothManager.class);
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message){
                switch(message.what){
                    case STATE_CONNEXION:
                        switch (message.arg1){
                            case 1:
                                Toast.makeText(getApplicationContext(),"CONNECTE A "+connecte_a,Toast.LENGTH_SHORT).show();
                                //Connecté à
                                texte_nom.setText("Nom : "+connecte_a);
                                break;
                            case -1:
                                //echec de la connexion
                                break;
                        }
                        break;
                    case STATE_READ:
                        Character recu = (Character) message.obj;
                        //on dessine ce que l'on a recu
                        dessiner(recu);
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
                clicCommencer = !clicCommencer;
                if(clicCommencer==false){
                    btnCommencer.setBackgroundResource(R.mipmap.ic_commencer);
                    btnArreter.setVisibility(View.GONE);
                    btnRecommencer.setVisibility(View.GONE);
                }
                else{
                    //on envois c pour lancer le cartographie
                    if(thread_connecte!=null){
                        if(btnHaut.getVisibility()!=View.VISIBLE){
                            thread_connecte.write('c');
                        }
                        else{
                            //on est en mode manuel
                            thread_connecte.write('m');
                        }
                    }
                    else{
                        //erreur
                    }
                    btnCommencer.setBackgroundResource(R.mipmap.ic_pause);
                    btnArreter.setVisibility(View.VISIBLE);
                    btnRecommencer.setVisibility(View.VISIBLE);
                }
            }
        });
        btnRecommencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicCommencer==false){
                    btnCommencer.setBackgroundResource(R.mipmap.ic_pause);
                    clicCommencer=!clicCommencer;
                    //on vide l'image
                    cartographie.setImageBitmap(null);
                    //on relance la cartographie
                    dessin = 0;
                    if(thread_connecte!=null){
                        thread_connecte.write('c');
                    }
                    else{
                        //erreur
                    }
                }
                else{
                    btnCommencer.setBackgroundResource(R.mipmap.ic_commencer);
                    clicCommencer=!clicCommencer;
                    btnArreter.setVisibility(View.GONE);
                    btnRecommencer.setVisibility(View.GONE);
                }
            }
        });
        btnArreter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicCommencer==false){
                    btnCommencer.setBackgroundResource(R.mipmap.ic_pause);
                    clicCommencer=!clicCommencer;
                }
                else{
                    btnCommencer.setBackgroundResource(R.mipmap.ic_commencer);
                    clicCommencer=!clicCommencer;
                    dessin = 0;
                    //j'envois m pour passer en mode manunel et stopper la cartographie
                    if(thread_connecte!=null){
                        thread_connecte.write('m');
                    }
                    else{

                    }
                }
            }
        });
        btnDroit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on envoie à l'appareil bluetooth le signal pour aller à droite
                if(thread_connecte!=null){
                    //Toast.makeText(getApplicationContext(),"DROITE",Toast.LENGTH_SHORT).show();
                    thread_connecte.write('d');
                    dessiner('d');
                }
            }
        });
        btnGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on envoie à l'appareil bluetooth le signal pour aller à gauche
                if(thread_connecte!=null){
                    //Toast.makeText(getApplicationContext(),"GAUCHE",Toast.LENGTH_SHORT).show();
                    thread_connecte.write('q');
                    dessiner('q');
                }
            }
        });
        btnHaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on envoie à l'appareil bluetooth le signal pour aller tout droit
                if(thread_connecte!=null){
                    //Toast.makeText(getApplicationContext(),"AVANT",Toast.LENGTH_SHORT).show();
                    thread_connecte.write('z');
                    dessiner('z');
                }
            }
        });
        btnBas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on envoie à l'appareil bluetooth le signal pour aller en arrière
                if(thread_connecte!=null){
                    //Toast.makeText(getApplicationContext(),"ARRIERE",Toast.LENGTH_SHORT).show();
                    thread_connecte.write('s');
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
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                affichagePeripherique();
                break;
            //mode automatique
            case R.id.navigation_toolbar_automatique:
                item.setChecked(false);
                clicAutomatique = !clicAutomatique;
                if(clicAutomatique==true){
                    item.setIcon(R.mipmap.ic_mode_on);
                    this.btnBas.setVisibility(View.GONE);
                    this.btnHaut.setVisibility(View.GONE);
                    this.btnGauche.setVisibility(View.GONE);
                    this.btnDroit.setVisibility(View.GONE);
                    //modre automatique
                    if(thread_connecte!=null){
                        //Toast.makeText(getApplicationContext(),"c",Toast.LENGTH_SHORT).show();
                        thread_connecte.write('c');
                    }
                }
                else{
                    //remettre l'icon du mode manuel en on
                    item.setIcon(null);
                    this.btnBas.setVisibility(View.VISIBLE);
                    this.btnHaut.setVisibility(View.VISIBLE);
                    this.btnGauche.setVisibility(View.VISIBLE);
                    this.btnDroit.setVisibility(View.VISIBLE);
                    //modre manuel
                    if(thread_connecte!=null){
                        //Toast.makeText(getApplicationContext(),"m",Toast.LENGTH_SHORT).show();
                        thread_connecte.write('m');
                    }
                }
                break;
            //mode manuel
            case R.id.navigation_toolbar_manuel:
                item.setChecked(false);
                clicManuel = !clicManuel;
                if(clicManuel==false){
                    //remettre l'icon du mode automatique en on
                    item.setIcon(null);
                    this.btnBas.setVisibility(View.GONE);
                    this.btnHaut.setVisibility(View.GONE);
                    this.btnGauche.setVisibility(View.GONE);
                    this.btnDroit.setVisibility(View.GONE);
                    //modre automatique
                    if(thread_connecte!=null){
                        //Toast.makeText(getApplicationContext(),"c",Toast.LENGTH_SHORT).show();
                        thread_connecte.write('c');
                    }
                }
                else{
                    item.setIcon(R.mipmap.ic_mode_on);
                    this.btnBas.setVisibility(View.VISIBLE);
                    this.btnHaut.setVisibility(View.VISIBLE);
                    this.btnGauche.setVisibility(View.VISIBLE);
                    this.btnDroit.setVisibility(View.VISIBLE);
                    //modre manuel
                    if(thread_connecte!=null){
                        //Toast.makeText(getApplicationContext(),"m",Toast.LENGTH_SHORT).show();
                        thread_connecte.write('m');
                    }
                }
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
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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

    private void dessiner(Character c){
        //si nous sommes en mode manuel ou que le compteur d'erreur a comptabilisé les 3 mouvements d'erreur de départ
        //on lance le dessin
        if((btnGauche.getVisibility()==View.VISIBLE && btnDroit.getVisibility()==View.VISIBLE && btnHaut.getVisibility()==View.VISIBLE && btnBas.getVisibility()==View.VISIBLE) || compteur_erreur_depart>=3){
            //le repère est a lenvers sur une image!! le y+ est vers le bas et le y- vers le haut (visuellement)
            //DESSINER SUR IMAGE
            if(dessin==0){
                date_cartographie = LocalDateTime.now();
                b = Bitmap.createBitmap(cartographie.getWidth(),cartographie.getHeight(),Bitmap.Config.ARGB_8888);
            }
            else{
                b =  Bitmap.createBitmap(((BitmapDrawable)cartographie.getDrawable()).getBitmap());
                b = b.copy(Bitmap.Config.ARGB_8888, true);
            }
            Canvas tempCanvas = new Canvas(b);
            LocalDateTime date_courante;
            Paint peinture = new Paint();
            peinture.setColor(Color.BLACK);
            peinture.setStyle(Paint.Style.STROKE);
            peinture.setStrokeWidth(5);
            //63dp = 1cm (environ) sachant que l'on prend comme echelle 1cm sur l'écran = 250cm dans la réalité
            //vu qu'on parcours environ 50cm par seconde
            //en 1 sec  il parcours 50 cm soit 12.6dp
            //et que la plupart des pièces on des largeurs et longueurs de 8m environ

            //si on recupère z, pour avancer
            if(c.equals('z')||c.equals('Z')){
                date_cartographie = LocalDateTime.now();
                if(dessin==0){
                    //on définit la position de départ
                    x_depart=(cartographie.getWidth())/2;
                    y_depart=(cartographie.getHeight())/2;
                }
                else{
                    x_depart=x_arrive;
                    y_depart=y_arrive;
                }
            }
            //si on recupère q, pour gauche
            else if(c.equals('q')||c.equals('Q')){
                //on calcule en fonction du temps passer
                date_courante = LocalDateTime.now();
                float temps_ecoule = date_courante.getSecond()-date_cartographie.getSecond();
                switch (direction){
                    case "x+":
                        //se déplace sur l'axe des x vers les positif
                        x_arrive=(float)12.6*temps_ecoule+x_depart;
                        y_arrive=y_depart;
                        direction="y-";
                        break;
                    case "x-":
                        //se déplace sur l'axe des x vers les positif
                        x_arrive=x_depart-(float)12.6*temps_ecoule;
                        y_arrive=y_depart;
                        direction="y+";
                        break;
                    case "y+":
                        x_arrive=x_depart;
                        y_arrive=y_depart+(float)12.6*temps_ecoule;
                        direction="x+";
                        break;
                    case "y-":
                        x_arrive=x_depart;
                        y_arrive=y_depart-(float)12.6*temps_ecoule;
                        direction="x-";
                        break;
                }
                //on dessine une droite vers la gauche
                tempCanvas.drawLine(x_depart,y_depart,x_arrive,y_arrive,peinture);
                cartographie.setImageBitmap(b);
            }
            //si on recupère d, pour avancer droite
            else if(c.equals('d')||c.equals('D')){
                //on calcule en fonction du temps passer
                date_courante = LocalDateTime.now();
                float temps_ecoule = date_courante.getSecond()-date_cartographie.getSecond();
                switch (direction){
                    case "x+":
                        //se déplace sur l'axe des x vers les positif
                        x_arrive=(float)12.6*temps_ecoule+x_depart;
                        y_arrive=y_depart;
                        direction="y+";
                        break;
                    case "x-":
                        //se déplace sur l'axe des x vers les positif
                        x_arrive=x_depart-(float)12.6*temps_ecoule;
                        y_arrive=y_depart;
                        direction="y-";
                        break;
                    case "y+":
                        x_arrive=x_depart;
                        y_arrive=y_depart+(float)12.6*temps_ecoule;
                        direction="x-";
                        break;
                    case "y-":
                        x_arrive=x_depart;
                        y_arrive=y_depart-(float)12.6*temps_ecoule;
                        direction="x+";
                        break;
                }
                //on dessine une droite vers la gauche
                tempCanvas.drawLine(x_depart,y_depart,x_arrive,y_arrive,peinture);
                cartographie.setImageBitmap(b);
            }
            dessin++;
        }
        //sinon on incrémente le compteur d'erreur
        else{
            compteur_erreur_depart++;
        }
    }

    private void affichagePeripherique() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //si la localisation n'est pas activé on demande l'autorisation pour l'activer
        if (!isGpsEnabled) {
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        }
        //si le bluetooth n'est pas activé
        if(!bluetoothManager.getAdapter().isEnabled()){
            Toast.makeText(this,"Votre Bluetooth n'est pas activé",Toast.LENGTH_SHORT).show();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            return;
        }
        //si le bluetooth est activé
        else{
            //initialisation du set avec les devices connus
            pairedDevices = bluetoothManager.getAdapter().getBondedDevices();
            //si le discovery est activé
            if(bluetoothManager.getAdapter().isDiscovering()){
                //Toast.makeText(this, "discovery en cours ...",Toast.LENGTH_SHORT).show();
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
                            break;
                    }
                }
            }
            // TEST REUSSI
            //for(BluetoothDevice bd : pairedDevices){
            //    Toast.makeText(this, "nom : "+bd.getName()+"\n@MAC : "+bd.getAddress(),Toast.LENGTH_SHORT).show();
            //}

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
                } else {
                    //erreur
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

    //FORTE INSPIRATION : https://github.com/Hype47/droiduino/tree/master/DroiduinoBluetoothConnection/app/src/main
    //CONCERNANT LES DEUX CLASSES CI-DESSOUS
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
            byte buffer = 0;
            Log.e("THREADCONNECTE -> RUN","AVANT WHILE");
            while(true){
                try{
                    buffer = (byte) flux_entrant.read();
                    char message_recu = (char)buffer;
                    Log.e("THREADCONNECTE -> RUN","READ : "+message_recu);
                    handler.obtainMessage(STATE_READ,message_recu).sendToTarget();
                }
                catch(IOException e){
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void write(char c){
            byte bytes = (byte)c;
            try{
                flux_sortant.write(bytes);
            }
            catch(IOException e){
                Log.e("THREADCONNECTE -> WRITE","MESSAGE NON ENVOYE : "+c);
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