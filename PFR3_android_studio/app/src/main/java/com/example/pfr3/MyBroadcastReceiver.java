package com.example.pfr3;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class MyBroadcastReceiver  extends BroadcastReceiver {
    public static Set<BluetoothDevice> notPairedDevices = new HashSet<>();

    @Override
    public void onReceive(Context context, Intent intent) throws IllegalArgumentException{
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            notPairedDevices.add(device);
            if(device==null){
                throw new IllegalArgumentException("DEVICE NULL");
            }
            else{
                throw  new IllegalArgumentException("DEVICE : "+device.getName());
            }
        }

    }

    public Set<BluetoothDevice> getSet(){
        return notPairedDevices;
    }


}
