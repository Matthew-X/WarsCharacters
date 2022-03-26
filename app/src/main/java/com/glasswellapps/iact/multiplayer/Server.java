package com.glasswellapps.iact.multiplayer;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;

public class Server extends Thread {
    private BluetoothServerSocket serverSocket;
    private BluetoothManager manager;
    private boolean isRunning = true;

    public Server(BluetoothManager manager) {
        System.out.println("SERVER STARTED");
        this.manager = manager;
        try {
            serverSocket=manager.getBluetoothAdapter().listenUsingRfcommWithServiceRecord(manager.getAppName(), manager.getMyUuid());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void run() {
        BluetoothSocket socket=null;
        while (isRunning)
        {
            try {
                manager.UpdateState(BluetoothState.CONNECTING);
                socket=serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                manager.UpdateState(BluetoothState.CONNECTION_FAILED);
            }
            if(socket!=null)
            {
                for (Connection connection: manager.getConnections())
                {
                    if(connection.getBluetoothSocket().equals(serverSocket))
                    {
                        closeServerSocket();
                    }
                }

                manager.UpdateState(BluetoothState.CONNECTED);
                Connection newConnection = new Connection(socket, manager);
                newConnection.start();
                manager.addConnection(newConnection);
            }
        }
    }
    public void closeServerSocket() {
        try
        {
            serverSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stopServer() {
        isRunning = false;
        closeServerSocket();
        try
        {
            this.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
