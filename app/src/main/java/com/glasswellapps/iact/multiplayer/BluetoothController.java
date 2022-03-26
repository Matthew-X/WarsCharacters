package com.glasswellapps.iact.multiplayer;
import static android.app.Activity.RESULT_CANCELED;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.glasswellapps.iact.R;
import com.glasswellapps.iact.ShortToast;
import com.glasswellapps.iact.character_screen.CharacterScreen;
import com.glasswellapps.iact.character_screen.controllers.ButtonPressedHandler;
import com.glasswellapps.iact.characters.Character;
import com.glasswellapps.iact.effects.Sounds;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class BluetoothController implements Observer {
    private final CharacterScreen characterScreen;
    private Character character;
    private final ImageView findServerButton;
    private final String uuid = "dd8c0994-aa25-4698-8e30-56f286a98375";
    private BluetoothManager bluetoothManager = null;
    private Dialog devicesDialog;
    private RecyclerView devicesList;
    private ArrayList<BluetoothDevice> devices;
    private DeviceListAdapter deviceListAdapter;

    public BluetoothController(CharacterScreen characterScreen){
        this.characterScreen = characterScreen;
        character = characterScreen.getCharacter();
        devicesDialog = new Dialog(characterScreen);
        devicesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        devicesDialog.setCancelable(false);
        devicesDialog.setContentView(R.layout.dialog_bluetooth_devices);
        devicesDialog.setCanceledOnTouchOutside(true);
        devicesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        devicesList = devicesDialog.findViewById(R.id.devices_list);

        findServerButton = characterScreen.findViewById(R.id.find_server_button);
        findServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBluetoothButton();
            }
        });
    }
    public void onDeviceSelected(DeviceViewHolder viewHolder) {
        devicesDialog.dismiss();
        Sounds.INSTANCE.selectSound();
        System.out.println("DEVICE SELECTED: "+devices.get(viewHolder.getAdapterPosition()).getName());
        bluetoothManager.startClient(viewHolder.getAdapterPosition());
    }
    public void onBluetoothButton(){
        Sounds.INSTANCE.selectSound();
        ButtonPressedHandler.onButtonPressed(findServerButton);
        if(bluetoothManager == null) {
            bluetoothManager = new BluetoothManager(characterScreen, "IATracker", uuid);
            bluetoothManager.addObserver(this::update);
        }
        bluetoothManager.requestPermissions(characterScreen);
        bluetoothManager.discoverDevices();
        devices = bluetoothManager.getDevices();
        deviceListAdapter= new DeviceListAdapter(devices, characterScreen,this);
        devicesList.setAdapter(deviceListAdapter);
        devicesList.setLayoutManager(new LinearLayoutManager(characterScreen));
        devicesDialog.show();
    }
    public void onResult(int requestCode, int resultCode, Intent data){
        if (requestCode == BluetoothManager.REQUEST_ENABLE_DISCOVERY) {
            if (resultCode == RESULT_CANCELED) {
                //Toast.makeText(characterScreen, "Discovery not enabled", Toast.LENGTH_SHORT)
                // .show();
            } else {
                //Toast.makeText(characterScreen, "Discovery enabled", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == BluetoothManager.REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_CANCELED) {
                ShortToast.show(characterScreen,"BLUETOOTH NOT ENABLED");
                bluetoothManager = null;
            } else {
                ShortToast.show(characterScreen,"BLUETOOTH ENABLED");
                bluetoothManager.discoverDevices();
            }
        }
    }

    public void onConnect(){
        ShortToast.show(characterScreen,"CONNECTED");
        sendCharacter(true);
    }
    public void sendCharacter(boolean updateImages){
        if(bluetoothManager!=null) {
            bluetoothManager.sendMessageToAll(NetworkProtocol.sendCharacter(bluetoothManager.id, character, updateImages));
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        deviceListAdapter.notifyDataSetChanged();
        deviceListAdapter.getItemCount();
        String status = "";
        Message msg = bluetoothManager.getCurrentMessage();
        if(msg!=null) {
            int state = msg.what;
            switch (state) {
                case BluetoothState.IDLE:
                    status = "Idle";
                    break;
                case BluetoothState.LISTENING:
                    status = "Listening";
                    break;
                case BluetoothState.CONNECTING:
                    status = "Connecting";
                    ShortToast.show(characterScreen,"CONNECTING...");
                    break;
                case BluetoothState.CONNECTED:
                    status = "Connected";
                    onConnect();
                    break;
                case BluetoothState.CONNECTION_FAILED:
                    status = "Connection Failed";
                    ShortToast.show(characterScreen,"CONNECTION FAILED");
                    break;
                case BluetoothState.MESSAGE_RECEIVED:
                    status = "Message Received";
                    onMessageReceived(msg);
                    break;
                case BluetoothState.ALREADY_CONNECTED:
                    status = "Already Connected";
                    break;
            }
        }

    }
    void onMessageReceived(Message msg){
        byte[] message = (byte[]) msg.obj;
        String str = new String(message) +" " + message .length;
        //ShortToast.show(characterScreen,str);

        int result = NetworkProtocol.onClientReceiveMessage(message,bluetoothManager);
        System.out.println(result);
        //ShortToast.show(characterScreen, "RESULT: "+result);
        switch (result){
            case Codes.DISCONNECT: onDisconnected();break;
            case Codes.GAMEOVER:  characterScreen.onGameOver();break;
        }
    }

    public void onDisconnected() {
        if(bluetoothManager!=null) {
            if(bluetoothManager.isConnected()) {
                NetworkProtocol.disconnect(bluetoothManager);
                bluetoothManager.stopService(Codes.DISCONNECT);
                ShortToast.show(characterScreen, "DISCONNECTED");
            }
        }
    }
}

class DeviceListAdapter extends RecyclerView.Adapter<DeviceViewHolder> {

    ArrayList<BluetoothDevice> list = new ArrayList<>();

    Context context;
    BluetoothController controller;

    public DeviceListAdapter(ArrayList<BluetoothDevice> list, Context context,
                             BluetoothController controller) {
        this.list = list;
        this.context = context;
        this.controller = controller;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder viewHolder, int position) {
        String name = list.get(position).getName();
        viewHolder.deviceName.setText(name.substring(0,Math.min(name.length(),10)));
        viewHolder.deviceStatus.setText(getBondedState(list.get(position).getBondState()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.onDeviceSelected(viewHolder);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public String getBondedState(int state){
        switch (state){
            case BluetoothDevice.BOND_BONDED: return "bonded";
            case BluetoothDevice.BOND_BONDING: return "bonding";
            case BluetoothDevice.BOND_NONE: return "not bonded";
        }
        return "";
    }
}

class DeviceViewHolder extends RecyclerView.ViewHolder {
    public TextView deviceName;
    public TextView deviceStatus;
    public View view;

    DeviceViewHolder(View itemView) {
        super(itemView);
        deviceName = (TextView) itemView.findViewById(R.id.device_name);
        deviceStatus = (TextView) itemView.findViewById(R.id.device_status);
        view = itemView;
    }
}