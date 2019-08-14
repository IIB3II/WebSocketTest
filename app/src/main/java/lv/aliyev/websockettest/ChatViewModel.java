package lv.aliyev.websockettest;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class ChatViewModel extends ViewModel {
    private final static String TAG = ChatViewModel.class.getSimpleName();
    private final static String WEB_SOCKET_URL = "wss://test.qwqer.com/api/chatmsg";

    private WebSocketClient mWebSocketClient;
    private MutableLiveData<String> mMessage = new MutableLiveData<>();

    //TODO if you wont use constructor implement factory ViewModelProviders.of(this, factory)
    public ChatViewModel() {

    }

    //TODO
    //LOGIN sessionid naiti


    @Override protected void onCleared() {
        super.onCleared();
        if (mWebSocketClient != null) mWebSocketClient.close();
    }

    public void loadWebSocket() {
        URI uri;
        try {
            uri = new URI(WEB_SOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        mWebSocketClient = getChatWebSocketClient(uri);

        mWebSocketClient.connect();
    }

    LiveData<String> message() {
        return mMessage;
    }

    public void send(String message) {
        if (mWebSocketClient != null) mWebSocketClient.send(message);
    }

    private WebSocketClient getChatWebSocketClient(URI uri) {
        return new  WebSocketClient(uri) {
            @Override
            public void onOpen (ServerHandshake serverHandshake){
                Log.i("Websocket", "Opened");

                String openMsg = "{\"command\":\"LOGIN\",\"data\":{\"restsessionid\":\"M431r5b5sjeklg9n90bqe1grt2v4j0\"}}";
                mWebSocketClient.send(openMsg);
            }

            @Override
            public void onMessage (String message){
                Log.i("Websocket", message);
                mMessage.postValue(message);
            }

            @Override
            public void onClose ( int i, String message,boolean b){
                Log.i("Websocket", "Closed " + message);
                mMessage.postValue("Closed " + message);
            }

            @Override
            public void onError (Exception e){
                Log.i("Websocket", "Error " + e.getMessage());
                mMessage.postValue("Error " + e.getMessage());
            }
        };
    }
}
