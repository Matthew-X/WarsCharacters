package com.glasswellapps.iact.multiplayer;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Connection extends Thread {
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private BluetoothManager manager;
    private String id = "";
    public Connection(BluetoothSocket socket, BluetoothManager manager) {
        bluetoothSocket=socket;
        this.manager = manager;
        InputStream tempIn=null;
        OutputStream tempOut=null;
        try {
            tempIn=bluetoothSocket.getInputStream();
            tempOut=bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            closeConnectionSocket();
            e.printStackTrace();
        }
        inputStream=tempIn;
        outputStream=tempOut;
    }
    public void run() {
        byte[] buffer=new byte[1024];
        int bytes;

        while (true)
        {
            try {
                bytes=inputStream.read(buffer);
                if(id.isEmpty()) {
                    id = NetworkProtocol.getIDFromMessage(buffer);
                }
                manager.receiveMessage(bytes, buffer);
            } catch (IOException e) {
                stopConnection();
                e.printStackTrace();
            }
        }
    }
    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeConnectionSocket() {
        try
        {
            bluetoothSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void stopConnection() {
        closeConnectionSocket();
        try
        {
            manager.removeConnection(this);
            this.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    public String getID(){
        return id;
    }
    public BluetoothSocket getBluetoothSocket(){ return bluetoothSocket;}
}