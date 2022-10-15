package ru.chat.server;


import ru.chat.network.TCPCConnection;
import ru.chat.network.TCPCConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ClassServer implements TCPCConnectionListener {
    public static void main(String[] args) {
        new ClassServer();

    }
    private  final ArrayList<TCPCConnection> connections = new ArrayList<>();
    private ClassServer()
    {
        System.out.println("Server running...");
         try (ServerSocket serverSocket = new ServerSocket(8189))
         {
             while (true)
             {
                 try
                 {
                     new TCPCConnection(this,serverSocket.accept());
                 }
                 catch (IOException e)
                 {
                     System.out.println("TCPConnection exeption: " + e);
                 }
             }
         }
         catch (IOException e)
         {
             throw  new RuntimeException(e);
         }
    }


    @Override
    public synchronized void onConnectionReady(TCPCConnection tcpcConnection) {
        connections.add(tcpcConnection);
        sendToAllClient("Client connected: " + tcpcConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPCConnection tcpcConnection, String value) {
        sendToAllClient(value);
    }

    @Override
    public synchronized void onDisconnect(TCPCConnection tcpcConnection) {
        connections.remove(tcpcConnection);
        sendToAllClient("Client disconnection: " + tcpcConnection);
    }

    @Override
    public synchronized void onExeption(TCPCConnection tcpcConnection, Exception exception) {
        System.out.println("TCPCConnection: " + exception);
    }

    private void sendToAllClient(String sms)
    {
        System.out.println(sms);
        ///////////////
        connections.forEach(it->it.sendMessage(sms));

    }
}
