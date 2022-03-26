package com.glasswellapps.iact.multiplayer;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;

public class Client extends Thread {
    private BluetoothSocket socket;
    private BluetoothManager manager;
    public Client(BluetoothDevice device, BluetoothManager manager) {
        this.manager = manager;

        try {
            socket=device.createRfcommSocketToServiceRecord(manager.getMyUuid());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            for (Connection connection:manager.getConnections())
            {
                if(connection.getBluetoothSocket().equals(socket))
                {
                    manager.UpdateState(BluetoothState.ALREADY_CONNECTED);
                    closeClientSocket();
                    return;
                }
            }
            socket.connect();
            manager.UpdateState(BluetoothState.CONNECTED);
            Connection newConnection =new Connection(socket, manager);
            newConnection.start();
            manager.addConnection(newConnection);

        } catch (IOException e) {
            closeClientSocket();
            e.printStackTrace();
            manager.UpdateState(BluetoothState.CONNECTION_FAILED);
        }
    }
    public void closeClientSocket()
    {
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void stopClient()
    {
        closeClientSocket();
        try
        {
            this.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
