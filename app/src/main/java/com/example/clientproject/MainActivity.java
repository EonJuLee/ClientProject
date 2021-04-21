package com.example.clientproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private static String KEY = "KEY";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        ClientThread thread = new ClientThread();
        thread.start();
    }

    public void initView(){
        textView = findViewById(R.id.textView);
    }

    public class ClientThread extends Thread{

        private String IP_ADDRESS = "192.168.43.18";
        private int PORT_NUMBER = 5550;
        private ThreadHandler handler;

        ClientThread(){
            handler = new ThreadHandler();
        }

        @Override
        public void run() {
            try{
                Socket clientSocket = new Socket(IP_ADDRESS, PORT_NUMBER);

                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                String inputString = (String) in.readObject();

                if(in==null){
                    Log.d("Socket","in is null");
                }
                Log.d("Socket","inputString : "+inputString);

                Bundle bundle = new Bundle();
                bundle.putString(KEY, inputString);

                Message msg = handler.obtainMessage();
                msg.setData(bundle);
                handler.sendMessage(msg);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public class ThreadHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d("Socket","in Handler");
            textView.setText(msg.getData().getString(KEY));
        }
    }
}