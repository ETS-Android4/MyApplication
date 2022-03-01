package com.example.william.my.module.network.socket;

import com.example.william.my.module.utils.L;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class SocketServer extends WebSocketServer {

    private static final String TAG = "Socket";

    public static final int DEFAULT_SERVER_PORT = 5567;

    public SocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onStart() {
        L.i(TAG, "onStart");
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        L.i(TAG, "onOpen");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        L.i(TAG, "OnMessage:" + message);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        L.i(TAG, "onStart");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        L.e(TAG, "Socket Exception:" + ex.toString());
    }
}
