package lv.aliyev.websockettest;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;

public class ChatWebScocketClient extends WebSocketClient {

    OnError mOnError = null;
    OnClose mOnClose = null;

    public ChatWebScocketClient(URI serverUri) {
        super(serverUri);
    }

    public ChatWebScocketClient onError(OnError onError) {
        mOnError = onError;
        return this;
    }

    public ChatWebScocketClient onClose(OnClose onClose) {
        mOnClose = onClose;
        return this;
    }

    //TODO const with param names
    public void send(String command, JSONObject params) {
        JSONObject commandJson = new JSONObject();
        try {
            commandJson.put("command", command);
            if (params != null) {
                commandJson.put("data", params);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.send(commandJson.toString());
    }

    public void login(String sessionId) {
        JSONObject params = new JSONObject();
        try {
            params.put("restsessionid", sessionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override public void onOpen(ServerHandshake handshakedata) {
        login("some session id");
    }

    @Override public void onMessage(String message) {
        Log.i("Websocket", message);
    }

    @Override public void onClose(int code, String reason, boolean remote) {
        Log.i("Websocket", "Closed " + reason);
    }

    @Override public void onError(Exception e) {
        Log.i("Websocket", "Error " + e.getMessage());
    }

    interface OnError {
        void error(Exception e);
    }

    interface OnClose {
        void close();
    }

    interface onMessage {
        void onMessage(String command, String message);
    }

}
