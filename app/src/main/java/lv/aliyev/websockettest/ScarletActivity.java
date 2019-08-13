package lv.aliyev.websockettest;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tinder.scarlet.Scarlet;

import org.java_websocket.WebSocketFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class ScarletActivity extends AppCompatActivity {
    @BindView(R.id.text) EditText mMessage;
    @BindView(R.id.log) TextView mTextLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    private void writeToLog(final String text) {
        runOnUiThread(() -> {
            mTextLog.append("\n");
            mTextLog.append(text);
        });
    }

    @OnClick(R.id.button) void sendMessage() {
        //TODO send message
        mMessage.setText("");
    }




}
