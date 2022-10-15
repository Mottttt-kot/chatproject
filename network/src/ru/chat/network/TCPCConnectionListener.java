package ru.chat.network;

public interface TCPCConnectionListener {

    void onConnectionReady(TCPCConnection tcpcConnection);
    void onReceiveString(TCPCConnection tcpcConnection,String value);
    void onDisconnect(TCPCConnection tcpcConnection);
    void onExeption(TCPCConnection tcpcConnection,Exception exception);
}
