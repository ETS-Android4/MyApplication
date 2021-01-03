package com.example.william.my.network.socket;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class SocketServer extends WebSocketServer {

    private static final String TAG = "ServerSocket";

    public static final int DEFAULT_SERVER_PORT = 5567;

    public SocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart");
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.i(TAG, "onOpen");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.i(TAG, "OnMessage:" + message);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.i(TAG, "onStart");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.i(TAG, "Socket Exception:" + ex.toString());
    }
}
