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

    public static final int STATE_NONE = 0;
    public static  final int STATE_LISTEN = 1;
    public static  final int STATE_CONNECTING = 2;
    public static  final int STATE_CONNECTED = 3;
    private ThreadConnexion connexion;
    private ThreadAccepter accepter;
    private ThreadConnecte connecte;
    private int state;
    private BluetoothAdapter bluetoothAdapter;

    public Connexion(Context context, Handler handler){
        this.h = handler;
        this.c=context;

        state = STATE_NONE;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        APP_UUID = MainActivity.getDeviceId(c);
    }

    public  int getState(){
        return state;
    }

    public synchronized void setState(int state){
        this.state = state;
        this.h.obtainMessage(MainActivity.MESSAGE_STATE_CHANGED,state,-1).sendToTarget();
    }

    private synchronized void start(){
        if(connexion!=null){
            connexion.cancel();
            connexion=null;
        }
        if(accepter == null){
            accepter = new ThreadAccepter();
            accepter.start();
        }
        if(connecte != null){
            connecte.cancel();
            connecte = null;
        }

        setState(STATE_LISTEN);
    }

    public synchronized void stop(){
        if(connexion!=null){
            connexion.cancel();
            connexion=null;
        }

        if (accepter!=null){
            accepter.cancel();
            accepter=null;
        }

        if(connecte != null){
            connecte.cancel();
            connecte = null;
        }

        setState(STATE_NONE);
    }

    public void connexion(BluetoothDevice device){
        if(state == STATE_CONNECTING){
            connexion.cancel();
            connexion = null;
        }
        connexion = new ThreadConnexion(device);
        connexion.start();
        if(connecte != null){
            connecte.cancel();
            connecte = null;
        }
        setState(STATE_CONNECTING);
    }

    public void write(byte[] buffer){
        ThreadConnecte c;
        synchronized (this){
            if(state != STATE_CONNECTED){
                return;
            }
            c = connecte;
        }
        c.write(buffer);
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
            if(chaussette_serveuse!=null){
                switch (state){
                    case STATE_LISTEN:
                    case STATE_CONNECTING:
                        connexion(chaussette.getRemoteDevice());
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

    private class  ThreadConnexion extends Thread{
        private final BluetoothSocket chaussette;
        private final BluetoothDevice device;

        public ThreadConnexion(BluetoothDevice d){
            this.device = d;

            BluetoothSocket chaussure = null;
            try{
                chaussure =  d.createInsecureRfcommSocketToServiceRecord(APP_UUID);
            }
            catch(IOException e){
                Log.e("Connect -> Constructeur",e.toString());
            }
            chaussette=chaussure;
            Toast.makeText(c, chaussette.toString(), Toast.LENGTH_SHORT).show();
        }

        public void run(){
            try {
                chaussette.connect();
            }
            catch(IOException e){
                Log.e("Connect -> Run",e.toString());
                try{
                    chaussette.close();
                }
                catch (IOException exception){
                    Log.e("Connect -> Run -> Close",exception.toString());
                }
                echecConnexion();
                return;
            }
            synchronized (Connexion.this){
                connexion = null;
            }

            connexion(device);
        }

        public void cancel(){
            try{
                chaussette.close();
            }
            catch(IOException e){
                Log.e("Connect -> Cancel",e.toString());
            }
        }

        private synchronized  void echecConnexion(){
            Connexion.this.start();
        }

        private synchronized  void connexion(BluetoothDevice d){
            if(connexion!=null){
                connexion.cancel();
                connexion = null;
            }
            if(connecte != null){
                connecte.cancel();
                connecte = null;
            }
            connecte = new ThreadConnecte(chaussette);
            connecte.start();
            Message message = h.obtainMessage(MainActivity.MESSAGE_DEVICE_NAME);
            Bundle bundle = new Bundle();
            bundle.putString(MainActivity.DEVICE_NAME,d.getName());
            message.setData(bundle);
            h.sendMessage(message);

            setState(STATE_CONNECTED);
        }
    }

    private class ThreadConnecte extends Thread{
        private final BluetoothSocket chaussette;
        private  final InputStream flux_entrant;
        private  final  OutputStream flux_sortant;

        public ThreadConnecte(BluetoothSocket chaussette){
            this.chaussette = chaussette;

            InputStream tempInput = null;
            OutputStream tempOutput = null;
            try{
                tempInput = chaussette.getInputStream();
                tempOutput = chaussette.getOutputStream();
            }
            catch (IOException e){
                Log.e("Connected -> Constructeur",e.toString());
            }
            flux_entrant=tempInput;
            flux_sortant=tempOutput;
        }

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;
            try{
                bytes = flux_entrant.read(buffer);
                h.obtainMessage(MainActivity.MESSAGE_STATE_READ, bytes,-1,buffer).sendToTarget();
            }
            catch (IOException e){
                Log.e("Connected -> Run",e.toString());
                Connexion.this.start();
            }
        }

        public void write(byte[] buffer){
            try{
                flux_sortant.write(buffer);
                h.obtainMessage(MainActivity.MESSAGE_STATE_WRITE,-1,-1,buffer).sendToTarget();
            }
            catch (IOException e){
                Log.e("Connected -> Write",e.toString());
            }
        }

        public void cancel(){
            try{
                chaussette.close();
            }
            catch (IOException e){
                Log.e("Connected -> Cancel",e.toString());
            }
        }
    }
}

