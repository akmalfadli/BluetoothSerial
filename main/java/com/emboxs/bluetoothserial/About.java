package com.emboxs.bluetoothserial;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by nbeone on 20/03/2015.
 */
  public class About extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        ImageView m = (ImageView) findViewById(R.id.imageView);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Rumah Pintar V 1.0 ",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
  }
