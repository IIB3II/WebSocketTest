package lv.aliyev.websockettest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    WebSocketClient mWebSocketClient;
    @BindView(R.id.text) EditText mMessage;
    @BindView(R.id.log) TextView mTextLog;
    private ChatViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupViewModel();

        //connectWebSocket();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mWebSocketClient.close();
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        mViewModel.loadWebSocket();

        final Observer<String> recieveObserver = message -> {
            // Update UI
            mTextLog.append("\n");
            mTextLog.append(message);
        };
        mViewModel.message().observe(this, recieveObserver);
    }


    //TODO SingleActivity
    //ChatFragment

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("wss://test.qwqer.com/api/chatmsg");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send("Hello from ");
            }

            @Override
            public void onMessage(String message) {
                writeToLog(message);
            }

            @Override
            public void onClose(int i, String message, boolean b) {
                Log.i("Websocket", "Closed " + message);
                writeToLog("Closed " + message);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
                writeToLog("Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    private void writeToLog(final String text) {
        runOnUiThread(() -> {
            mTextLog.append("\n");
            mTextLog.append(text);
        });
    }

    @OnClick(R.id.button) void sendMessage() {
        //mWebSocketClient.send(mMessage.getText().toString());
        mViewModel.send(mMessage.getText().toString());
        mMessage.setText("");
    }

}
