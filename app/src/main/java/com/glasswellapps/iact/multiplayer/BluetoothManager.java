package com.glasswellapps.iact.multiplayer;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import androidx.core.content.ContextCompat;

import com.glasswellapps.iact.ShortToast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.UUID;

public class BluetoothManager extends Observable {
    public static final int REQUEST_ENABLE_BLUETOOTH=1;
    public static final int REQUEST_ENABLE_DISCOVERY=2;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<BluetoothDevice> foundDevices=new ArrayList<BluetoothDevice>();
    private ArrayList<String> deviceNames=new ArrayList<String>();
    private ArrayList<String> deviceAddresses=new ArrayList<String>();
    private ArrayList<Connection> connections = new ArrayList<>();
    private String appName;
    private UUID uuid;
    private Activity activity;
    private DeviceFinder deviceFinder;
    private Server server;
    private Client client;
    private Message currentMessage;
    public String id;
    public static final int DISCOVERY_TIME = 180;
    static final int REQUEST_COARSE_LOCATION = 3;
    static final int REQUEST_FINE_LOCATION = 4;
    static final int REQUEST_SCAN = 5;
    static final int REQUEST_CONNECT = 6;

    public BluetoothManager(Activity activity, String appName, String uuid) {
        this.activity = activity;
        this.appName = appName;
        this.uuid = UUID.fromString(uuid);
        //stopService(Codes.DISCONNECT);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null) {
            ShortToast.show(activity, "BLUETOOTH UNSUPPORTED");
            UpdateState(BluetoothState.UNSUPPORTED);
        }
        else if(!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent,REQUEST_ENABLE_BLUETOOTH);
        }
        id = NetworkProtocol.createID();
        deviceFinder = new DeviceFinder(activity,this);
    }



    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            currentMessage = msg;
            onUpdate();
            return true;
        }
    });

    public void enableDiscovery() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERY_TIME);
        activity.startActivityForResult(intent, REQUEST_ENABLE_DISCOVERY);
    }
    public void discoverDevices() {
        deviceFinder.discoverDevices();
    }
    public void findBondedDevices() {
        deviceFinder.findBondedDevices();
    }
    public void clearDevices() {
        deviceNames.clear();
        deviceAddresses.clear();
        foundDevices.clear();
    }
    public void addDevice(BluetoothDevice device) {
        if(foundDevices.contains(device)){
            return;
        }
        foundDevices.add(device);
        deviceNames.add(device.getName());
        deviceAddresses.add(device.getAddress());
    }

    public void startServer() {
        if(server == null) {
            server = new Server(this);
            server.start();
        }
    }
    public void startClient(int serverDeviceIndex) {
        bluetoothAdapter.cancelDiscovery();
        client = new Client(foundDevices.get(serverDeviceIndex), this);
        client.start();
        ShortToast.show(activity, "CONNECTING");
    }

    public void stopService(int disconnectCode) {
        System.out.println("STOPPED");

        for (Connection connection: connections)
        {
            //connection.stopConnection();
        }

        if(client!=null) client.stopClient();
        client = null;
        if(server!=null) server.stopServer();
        server = null;
        connections.clear();
        foundDevices.clear();
        deviceNames.clear();
        deviceAddresses.clear();
    }

    public void sendMessageToAll(byte[] data) {
        for (Connection connection: connections) {
            connection.write(data);
        }
    }
    public Connection getConnection(String id){
        for (Connection connection: connections) {
            if(connection.getID() == id) {
                return connection;
            }
        }
        return null;
    }
    public void receiveMessage(int bytes, byte[] buffer) {
        handler.obtainMessage(BluetoothState.MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
    }
    public Message getCurrentMessage() { return currentMessage; }
    public BroadcastReceiver getBluetoothReciever() { return  deviceFinder.getBluetoothReciever(); }
    public BluetoothAdapter getBluetoothAdapter(){ return bluetoothAdapter;}
    public ArrayList<String> getDeviceNames(){ return deviceNames; }
    public ArrayList<BluetoothDevice> getDevices(){ return foundDevices; }
    public String getAppName(){ return appName; }
    public UUID getMyUuid(){ return uuid; }
    public ArrayList<Connection> getConnections(){ return connections; }
    public void addConnection(Connection connection){ connections.add(connection); }
    public void removeConnection(Connection connection) {
        if(connections.contains(connection))
            connections.remove(connection);
    }
    public boolean isConnected(){
        return connections.size() > 0;
    }

    public void disconnect(String id){
        NetworkProtocol.disconnect(this);
        Connection toRemove = getConnection(id);
        if(toRemove != null) {
            toRemove.stopConnection();
            connections.remove(toRemove);
        }
    }

    public void onUpdate() {
        setChanged();
        notifyObservers();
    }
    public void UpdateState(int state) {
        Message message=Message.obtain();
        message.what=state;

        handler.sendMessage(message);
    }

    public void requestPermissions(Activity context) {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                context.requestPermissions(
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CONNECT);

            }
        }
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                context.requestPermissions(
                        new String[]{Manifest.permission.BLUETOOTH_SCAN}, REQUEST_SCAN);

            }
        }
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_COARSE_LOCATION);
            }
        }
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_FINE_LOCATION);
            }
        }
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(
                        new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                        REQUEST_ENABLE_DISCOVERY);
            }
        }
    }
}

/* ADD TO ACTIVITY
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == BluetoothManager.REQUEST_ENABLE_DISCOVERY)
        {
            if (resultCode == RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(),"Discovery not enabled",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Discovery enabled",Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == BluetoothManager.REQUEST_ENABLE_BLUETOOTH)
        {
            if (resultCode == RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(),"Bluetooth not enabled",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Bluetooth enabled",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothManager.getBluetoothReciever());
    }
 */