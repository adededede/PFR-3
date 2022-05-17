package com.example.pfr3;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Connexion {
    private final Handler h;
    private final UUID APP_UUID;
    private Context c;

    //etats
    public static final int STATE_NONE = 0;
    public static  final int STATE_LISTEN = 1;
    public static  final int STATE_CONNECTING = 2;
    public static  final int STATE_CONNECTED = 3;
    private int state;

    //variable bluetooth
    private BluetoothAdapter bluetoothAdapter;

    //thread
    private ThreadConnexion thread_connexion;
    private ThreadAccepter thread_accepter;
    private ThreadConnecte thread_connecte;

    public Connexion(Context context, Handler handler,UUID uuid){
        this.h = handler;
        this.c=context;

        state = STATE_NONE;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        APP_UUID = uuid;
    }

    public  int getState(){
        return state;
    }

    public synchronized void setState(int state){
        this.state = state;
        //envoie le changement d'état à notre handler
        h.obtainMessage(MainActivity.MESSAGE_STATE_CHANGED,state,-1).sendToTarget();
    }

    private synchronized void start(){
        //permet de s'assurer que l'on part sur de nouvelles bases
        if(thread_connexion!=null){
            thread_connexion.cancel();
            thread_connexion=null;
        }
       if(thread_accepter == null){
           thread_accepter = new ThreadAccepter();
           thread_accepter.start();
       }
       if(thread_connecte != null){
           thread_connecte.cancel();
           thread_connecte = null;
       }

       setState(STATE_LISTEN);
    }

    public synchronized void stop(){
        if(thread_connexion!=null){
            thread_connexion.cancel();
            thread_connexion=null;
        }
        if (thread_accepter!=null){
            thread_accepter.cancel();
            thread_accepter=null;
        }
        if(thread_connecte != null){
            thread_connecte.cancel();
            thread_connecte = null;
        }

        setState(STATE_NONE);
    }

    public void connexion(BluetoothDevice device){
        if(state == STATE_CONNECTING){
            thread_connexion.cancel();
            thread_connexion = null;
        }
        thread_connexion = new ThreadConnexion(device);
        thread_connexion.start();
        if(thread_connecte != null){
            thread_connecte.cancel();
            thread_connecte = null;
        }

        setState(STATE_CONNECTING);
    }

    public void write(byte[] buffer){
        ThreadConnecte thread;
        synchronized (this){
            if(state != STATE_CONNECTED){
                return;
            }
            thread = thread_connecte;
        }
        thread.write(buffer);
    }

    private class ThreadAccepter extends Thread{
        private BluetoothServerSocket chaussette_serveuse;

        public ThreadAccepter(){
            BluetoothServerSocket chaussure_serveuse=null;
            try{
                chaussure_serveuse = bluetoothAdapter.listenUsingRfcommWithServiceRecord("GESTION DE JIMMY",APP_UUID);
            }
            catch(IOException e){
                Log.e("Accepter -> Constructeur",e.toString());
            }
            chaussette_serveuse=chaussure_serveuse;
        }

        public void run(){
            BluetoothSocket chaussette = null;
            try{
                chaussette = chaussette_serveuse.accept();
            }
            catch(IOException e){
                Log.e("Accepter -> Run",e.toString());
                try{
                    chaussette_serveuse.close();
                }
                catch (IOException exception){
                    Log.e("Accepter -> Run -> Close",e.toString());
                }
            }
            if(chaussette!=null){
                switch (state){
                    case STATE_LISTEN:
                    case STATE_CONNECTING:
                        connexion(chaussette,chaussette.getRemoteDevice());
                        break;
                    case STATE_NONE:
                    case STATE_CONNECTED:
                        try{
                            chaussette.close();
                        }
                        catch(IOException e){
                            Log.e("Accepter -> Run -> Switch -> Close",e.toString());
                        }
                        break;
                }
            }
        }

        public void cancel(){
            try{
                chaussette_serveuse.close();
            }
            catch (IOException e){
                Log.e("Accepter -> Cancel -> Close",e.toString());
            }
        }
    }

    private class  ThreadConnexion extends Thread {
        private final BluetoothSocket chaussette;
        private final BluetoothDevice device;

        public ThreadConnexion(BluetoothDevice d) {
            this.device = d;
            BluetoothSocket chaussure = null;
            try {
                chaussure = d.createInsecureRfcommSocketToServiceRecord(APP_UUID);
            } catch (IOException e) {
                Log.e("Connect -> Constructeur", e.toString());
            }
            chaussette = chaussure;
        }

        public void run() {
            try {
                chaussette.connect();
            } catch (IOException e) {
                Log.e("Connect -> Run", e.toString());
                try {
                    chaussette.close();
                } catch (IOException exception) {
                    Log.e("Connect -> Run -> Close", exception.toString());
                }
                echecConnexion();
                return;
            }
            synchronized (Connexion.this) {
                thread_connexion = null;
            }
            connexion(chaussette, device);
        }

        public void cancel() {
            try {
                chaussette.close();
            } catch (IOException e) {
                Log.e("Connect -> Cancel", e.toString());
            }
        }

    }
    private class ThreadConnecte extends Thread {
        private final BluetoothSocket chaussette;
        private final InputStream flux_entrant;
        private final OutputStream flux_sortant;

        public ThreadConnecte(BluetoothSocket chaussette) {
            this.chaussette = chaussette;

            InputStream tempInput = null;
            OutputStream tempOutput = null;
            try {
                tempInput = chaussette.getInputStream();
                tempOutput = chaussette.getOutputStream();
            } catch (IOException e) {
                Log.e("Connected -> Constructeur", e.toString());
            }
            flux_entrant = tempInput;
            flux_sortant = tempOutput;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            try {
                bytes = flux_entrant.read(buffer);
                h.obtainMessage(MainActivity.MESSAGE_STATE_READ, bytes, -1, buffer).sendToTarget();
            } catch (IOException e) {
                Log.e("Connected -> Run", e.toString());
                connexionPerdue();
            }
        }

        public void write(byte[] buffer) {
            try {
                flux_sortant.write(buffer);
                h.obtainMessage(MainActivity.MESSAGE_STATE_WRITE, -1, -1, buffer).sendToTarget();
            } catch (IOException e) {
                Log.e("Connected -> Write", e.toString());
            }
        }

        public void cancel() {
            try {
                chaussette.close();
            } catch (IOException e) {
                Log.e("Connected -> Cancel", e.toString());
            }
        }
    }

    private void connexionPerdue(){
        Connexion.this.start();
    }

    private synchronized  void echecConnexion(){
        Connexion.this.start();
    }

    private synchronized  void connexion(BluetoothSocket saussette,BluetoothDevice d){
        if(thread_connexion!=null){
            thread_connexion.cancel();
            thread_connexion = null;
        }
        if(thread_connecte != null){
            thread_connecte.cancel();
            thread_connecte = null;
        }
        thread_connecte = new ThreadConnecte(saussette);
        thread_connecte.start();

        Message message = h.obtainMessage(MainActivity.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.DEVICE_NAME,d.getAddress());
        message.setData(bundle);
        h.sendMessage(message);

        setState(STATE_CONNECTED);
    }

}

