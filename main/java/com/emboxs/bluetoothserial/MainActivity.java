package com.emboxs.bluetoothserial;


import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;
    Button send;
    EditText edit;
    TextView textview;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case Bluetooth.SUCCESS_CONNECT:
                    Bluetooth.connectedThread = new Bluetooth.ConnectedThread((BluetoothSocket) msg.obj);
                    Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_SHORT).show();
                    Bluetooth.connectedThread.start();
                    break;
                case Bluetooth.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String strIncom = new String(readBuf);
                    textview.append(strIncom);

            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupWidget();
        Bluetooth.gethandler(mHandler);

    }

    private void setupWidget() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        send = (Button) findViewById(R.id.button);
        send.setOnClickListener(this);
        edit = (EditText) findViewById(R.id.edit);
        textview = (TextView) findViewById(R.id.textView);
        textview.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SubActivity.class));
                break;
            case R.id.bt_connect:
                startActivity(new Intent(this, Bluetooth.class));
                break;
            case R.id.bt_disconnect:
                try {
                    Bluetooth.connectedThread.cancel();
                    Toast.makeText(getApplicationContext(), "disconected !", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "not connected !", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.about:
                startActivity(new Intent(this, About.class));
                break;
            case R.id.save:
                String data = textview.getText().toString();
                String time = java.text.DateFormat.getDateTimeInstance().format(new Date());
                String direktori = new String(String.valueOf(Environment.getExternalStorageDirectory()));
                write(data, "BT_S_"+time +".txt");
                Toast.makeText(this, "File Saved ! \n"+"BT_S"+direktori+time+".txt", Toast.LENGTH_LONG).show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                String pesan = edit.getText().toString();
                edit.setText("");
                kirim(pesan);
                break;
        }


    }

    private void kirim(String data) {
        if (Bluetooth.connectedThread != null)
            Bluetooth.connectedThread.write(data);
    }

    private void write (String data, String namafile) {
        File file = new File(Environment.getExternalStorageDirectory(),
                namafile);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
