package ru.chat.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPCConnection {

    private final Socket socket;
    private final Thread thread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private TCPCConnectionListener eventListener;
    public TCPCConnection(TCPCConnectionListener eventListener, String ipAddr, int port) throws IOException
    {
        this(eventListener,new Socket(ipAddr,port));
    }
    public TCPCConnection(TCPCConnectionListener tcpcConnectionListener, Socket socket1) throws IOException
    {
        this.eventListener = tcpcConnectionListener;
        this.socket = socket1;
        in = new BufferedReader( new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),Charset.forName("UTF-8")));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    eventListener.onConnectionReady(TCPCConnection.this);
                    while(!thread.isInterrupted())
                    {
                        String msg = in.readLine();
                        eventListener.onReceiveString(TCPCConnection.this,msg);
                    }

                } catch (IOException e) {
                    eventListener.onExeption(TCPCConnection.this,e);
                }
                finally {
                    eventListener.onDisconnect(TCPCConnection.this);
                }

            }
        });
        thread.start();
    }

    public synchronized void sendMessage(String st)
    {
        try {
            out.write(st + "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onExeption(TCPCConnection.this,e);
            disconnect();
        }

    }

    public synchronized void disconnect()
    {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onExeption(TCPCConnection.this,e);
        }
    }

    @Override
    public String toString() {
        return "TCCPConnection: "+ socket.getInetAddress() + ": " + socket.getPort();
    }
}
