package com.foo.garosero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.foo.garosero.myUtil.ServerHelper;

public class InitialActivity extends AppCompatActivity {
    Button btn_care;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        ServerHelper.initServer();

        btn_care = findViewById(R.id.btn_care);
        btn_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InitialActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}