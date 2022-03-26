package com.glasswellapps.iact.multiplayer;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.glasswellapps.iact.ShortToast;

import java.util.Set;

public class DeviceFinder {
    BluetoothManager manager;
    Activity activity;
    public DeviceFinder(Activity activity, BluetoothManager manager) {
        this.activity = activity;
        this.manager = manager;
    }
    public void discoverDevices() {
        if(!manager.getBluetoothAdapter().isDiscovering()) {
            manager.clearDevices();
            manager.getBluetoothAdapter().startDiscovery();
            ShortToast.show(activity, "SCANNING FOR DEVICES");
            IntentFilter scanIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            activity.getApplicationContext().registerReceiver(bluetoothReceiver, scanIntent);
        }
    }
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(device == null) return;
                if(device.getName() == null) return;

                manager.addDevice(device);
                manager.onUpdate();
            }
        }
    };
    public BroadcastReceiver getBluetoothReciever() { return  bluetoothReceiver; }
    public void findBondedDevices() {
        manager.clearDevices();
        Set<BluetoothDevice> devices=manager.getBluetoothAdapter().getBondedDevices();
        manager.UpdateState(BluetoothState.LISTENING);
        if( devices.size()>0) {
            for(BluetoothDevice device : devices)
            {
               manager.addDevice(device);
            }
        }
        manager.onUpdate();
    }
}
