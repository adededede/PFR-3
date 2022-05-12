package com.example.pfr3;

import static androidx.core.content.ContextCompat.getSystemService;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.util.UUID;

public class ThreadConnexion extends Thread{
    private final BluetoothDevice device;
    private final BluetoothSocket chaussette;
    private final UUID telephone;

    public ThreadConnexion(BluetoothDevice bd, UUID telephone){
        device = bd;
        this.telephone = telephone;
        BluetoothSocket chaussure = null;
        try{
            //ajouter mon UUID pour pouvoir se connceter (celui de l'app)
            chaussure = device.createRfcommSocketToServiceRecord(telephone);
        }
        catch (IOException e){
           //ERREUR
        }
        chaussette=chaussure;
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
        gestionConnexion(chaussette);
    }

    private void gestionConnexion(BluetoothSocket chaussette) {
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
